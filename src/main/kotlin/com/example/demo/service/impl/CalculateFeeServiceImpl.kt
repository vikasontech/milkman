package com.example.demo.service.impl

import com.example.demo.documents.UserConfig
import com.example.demo.domain.Invoice
import com.example.demo.domain.InvoiceDetail
import com.example.demo.service.CalculateFeeService
import com.example.demo.service.WeekDays
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Collectors

@Service
class CalculateFeeServiceImpl : CalculateFeeService {

  override fun calculateMonthlyPrice(userConfig: Publisher<UserConfig>): Mono<Invoice> {
    val extraMilk = 3L
    val invoiceDetails = userConfig.toMono().zipWith(getMilkConfigDetails(userConfig)) { a, b ->
      calculateInvoiceDetails(mapConfig = b, price = a.pricePerLtr, year = 2019,
          month = 7, milkNotTaken = emptyList())
    }

    // per month
    val totalCost = invoiceDetails.flatMap { e -> getTotalCost(e) }

    val invoiceDetailsFlux = invoiceDetails.flatMap { e -> e.collect(Collectors.toList()) }

    return Mono.zip(userConfig.toMono(), totalCost, invoiceDetailsFlux).map { tuple ->
      Invoice(name = tuple.t1.userId, pricePerLtr = tuple.t1.pricePerLtr,
          year = 2019, month = 7, billingDate = LocalDate.now(), descriptions = tuple.t3,
          vendorName = tuple.t1.vendorName, extraMilkPerLtr = extraMilk.toInt(),
          totalAmount = tuple.t2.add((tuple.t1.pricePerLtr.multiply(BigDecimal.valueOf(extraMilk)))))
    }
  }

  private fun getTotalCost(invoiceDetail: Flux<InvoiceDetail>): Mono<BigDecimal> {
    return invoiceDetail.map { e -> e.totalCostPerDay }.reduce { a, b -> a.add(b) }
  }

  fun calculateInvoiceDetails(mapConfig: Map<Int, Long>, milkNotTaken: List<Int>,
                              price: BigDecimal, year: Int, month: Int): Flux<InvoiceDetail> {

    val localDate = LocalDate.of(year, month, 1)
    val totalDays = localDate.lengthOfMonth()
    var tempStartDay = localDate.dayOfWeek.value

    var totalPrice = BigDecimal.ZERO
    val invoiceDetails = mutableListOf<InvoiceDetail>()

    for (date in 1..totalDays) {

      val milkPerDay = mapConfig.getOrDefault(tempStartDay, 0L)

      val pricePerDay = if (milkNotTaken.contains(date)) BigDecimal.ZERO
      else price.multiply(BigDecimal.valueOf(milkPerDay))

      invoiceDetails.add(InvoiceDetail(date = date, dayInWord = WeekDays.new(tempStartDay).name, milkPerDay = milkPerDay.toInt(), totalCostPerDay = pricePerDay))

      tempStartDay = if ((tempStartDay + 1) > 6) 0 else tempStartDay + 1
      totalPrice += pricePerDay
    }
    return Flux.fromIterable(invoiceDetails)
  }

  fun getMilkConfigDetails(userConfig: Publisher<UserConfig>): Mono<Map<Int, Long>> {
    val map = mutableMapOf<Int, Long>()
    userConfig.toMono().map { e -> e.milkConfigs }.map { e -> e.forEach { x -> map[x.day] = x.quantity.toLong() } }.block()
    return Mono.justOrEmpty(map.toMap())
  }
}
