package com.example.demo.service.impl

import com.example.demo.domain.InvoiceDetail
import com.example.demo.service.WeekDays
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Collectors

@RunWith(MockitoJUnitRunner::class)
class CalculateFeeServiceImplTest2 {

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


    calculateInvoiceDetails(mapConfig = mapConfig, price = BigDecimal.valueOf(37), month = 7, year = 2019).collect(Collectors.toList()).subscribe { e -> println(ObjectMapper().writeValueAsString(e)) }
  }

  @Test
  fun testGetTotalCost() {
    getTotalCost(Flux.fromIterable(dummyInvoiceDetails())).subscribe { e -> println(e) }
  }

  private fun getTotalCost(invoiceDetail: Flux<InvoiceDetail>): Mono<BigDecimal> {
    return invoiceDetail.map { e -> e.totalCostPerDay }.reduce { a, b -> a.add(b) }
  }

  private fun calculateInvoiceDetails(mapConfig: Map<Int, Long>, price: BigDecimal, year: Int, month: Int): Flux<InvoiceDetail> {

    val localDate = LocalDate.of(year, month, 1)
    val totalDays = localDate.lengthOfMonth()
    var tempStartDay = localDate.dayOfWeek.value

    var totalPrice = BigDecimal.ZERO
    val invoiceDetails = mutableListOf<InvoiceDetail>()
    for (i in 1..totalDays) {

      val milkPerDay = mapConfig.get(tempStartDay)
      val pricePerDay = price.multiply(BigDecimal.valueOf(milkPerDay!!))
      totalPrice += pricePerDay

      invoiceDetails.add(InvoiceDetail(date = i, dayInWord = WeekDays.new(tempStartDay).name, milkPerDay = milkPerDay.toInt(), totalCostPerDay = pricePerDay))

      tempStartDay = if ((tempStartDay + 1) > 6) 0 else tempStartDay + 1
    }
    return Flux.fromIterable(invoiceDetails)
  }

  private fun dummyInvoiceDetails(): List<InvoiceDetail> {
    return ObjectMapper().registerModule(KotlinModule()).readValue<List<InvoiceDetail>>(File("src/test/resources/InvoiceDetails.json"))
  }
}
