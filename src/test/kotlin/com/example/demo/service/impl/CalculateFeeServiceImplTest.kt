package com.example.demo.service.impl

import com.example.demo.service.WeekDays
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.coyote.http11.Constants.a
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import java.math.BigDecimal
import java.time.LocalDate
import java.util.function.BiFunction

@RunWith(MockitoJUnitRunner::class)
class CalculateFeeServiceImplTest {

  @Test
  fun getTotalNoOFDays() {
    val ld = LocalDate.of(2019, 5, 1)
    println(ld.lengthOfMonth())
//    calculateInvoiceDetails(noOfDays = ld.lengthOfMonth())
  }


  @Test
  fun randomTest() {

    val mapConfig = mutableMapOf<Int, Long>()
    mapConfig[0] = 0
    mapConfig[1] = 0
    mapConfig[2] = 2
    mapConfig[3] = 0
    mapConfig[4] = 2
    mapConfig[5] = 0
    mapConfig[6] = 3

    //    val invoice = Invoice(name = "vikas", vendorName = "vendor", billingDate = LocalDate.now(),
//        descriptions = invoiceDetails, month = 0, year = 0)
//    println(invoice)

    calculateInvoiceDetails(mapConfig= mapConfig,
        price = BigDecimal.valueOf(37),
        month = 7,
        year = 2019)
        .map { e -> {
          println(e)
          val om = ObjectMapper()
          om.writeValueAsString(e)
        } }
        .subscribe { e-> println(e)}
  }

  @Test
  fun testGetTotalCost() {

  }
  private fun getTotalCost(invoiceDetail: Flux<InvoiceDetail>): Mono<BigDecimal> {
    return invoiceDetail
        .map { e -> e.totalCostPerDay }
        .reduce{ a, b -> a.add(b)}
  }

  private fun calculateInvoiceDetails(mapConfig:Map<Int, Long>,
                                      price: BigDecimal,
                                      year: Int,
                                      month: Int): Flux<InvoiceDetail> {

    val localDate = LocalDate.of(year, month, 1)
    val totalDays = localDate.lengthOfMonth()
    var tempStartDay = localDate.dayOfWeek.value

    var totalPrice = BigDecimal.ZERO
    val invoiceDetails = mutableListOf<InvoiceDetail>()
    for (i in 1..totalDays) {

      val milkPerDay = mapConfig.get(tempStartDay)
      val pricePerDay = price.multiply(BigDecimal.valueOf(milkPerDay!!))
      totalPrice += pricePerDay

      invoiceDetails.add(InvoiceDetail(date = i, dayInWord = WeekDays.new(tempStartDay),
          milkPerDay = milkPerDay.toInt(), totalCostPerDay = pricePerDay))

      tempStartDay = if ((tempStartDay + 1) > 6) 0 else tempStartDay + 1
    }
    return Flux.fromIterable(invoiceDetails)
  }
}

data class InvoiceDetail(
    val date: Int,
    val dayInWord: WeekDays,
    val milkPerDay: Int,
    val totalCostPerDay: BigDecimal
)

data class Invoice(
    val name: String,
    val month: Int,
    val year: Int,
    val vendorName: String,
    val billingDate: LocalDate,
    val descriptions: List<InvoiceDetail>
)