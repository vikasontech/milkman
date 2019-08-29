package com.example.demo.service.impl

import com.example.demo.documents.UserConfig
import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import org.reactivestreams.Publisher
import org.springframework.util.Assert
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.test.StepVerifier
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Collectors

@RunWith(MockitoJUnitRunner::class)
class CalculateFeeServiceImplTest {

  @InjectMocks
  lateinit var calculateFeeService: CalculateFeeServiceImpl

  @Test
  fun testCalculateMonthlyPrice_v2() {

  }

  fun calculateMonthlyPrice_v2(userConfig: UserConfig, extraMilk: Int, year: Int, month: Int, day: Int): Mono<Invoice> {
    val invoiceDetails = userConfig.toMono().zipWith(calculateFeeService.getMilkConfigDetails(userConfig)) { a, b ->
      calculateFeeService.calculateInvoiceDetails(mapConfig = b, price = a.pricePerLtr, year = year, month = month, milkNotTaken = emptyList(), day = 0)
    }

    // per month
    val totalCost = invoiceDetails.flatMap { e -> calculateFeeService.getTotalCost(e) }

    val invoiceDetailsFlux = invoiceDetails.flatMap { e -> e.collect(Collectors.toList()) }

    return Mono.zip(userConfig.toMono(), totalCost, invoiceDetailsFlux).map { tuple ->
      Invoice(name = tuple.t1.userId, pricePerLtr = tuple.t1.pricePerLtr, year = 2019, month = 7, billingDate = LocalDate.now(), descriptions = tuple.t3, vendorName = tuple.t1.vendorName, extraMilkPerLtr = extraMilk.toInt(), totalAmount = tuple.t2.add((tuple.t1.pricePerLtr.multiply(BigDecimal.valueOf(extraMilk.toLong())))))
    }
  }

  @Test
  fun calculateMonthlyPriceTest() {
    val request = CalculateMonthlyInvoiceRequest(day = 0, month = 7, extraMilk = 3, datesMilkNotTaken = emptyList(),
        year = 2019, userId = "")
    val calculateMonthlyPriceMono = calculateFeeService.calculateMonthlyPrice(userConfig = getMockUserConfigData(),
        calculateMonthlyInvoiceRequest = request )

    calculateMonthlyPriceMono.subscribe { e ->
      Assert.isTrue(e.totalAmount == BigDecimal.valueOf(1221), "Total amount should be 1221")
    }
  }

  @Test
  fun getMilkConfigDetailsTest() {

    val userConfig = getMockUserConfigData()

    val map = calculateFeeService.getMilkConfigDetails(userConfig)
    val mutableMapOf = mutableMapOf<Int, Long>()
    mutableMapOf[0] = 0
    mutableMapOf[1] = 0
    mutableMapOf[2] = 2
    mutableMapOf[3] = 0
    mutableMapOf[4] = 2
    mutableMapOf[5] = 0
    mutableMapOf[6] = 3

    StepVerifier.create(map).expectNext(mutableMapOf.toMap()).verifyComplete()
  }

  private fun getMockUserConfigData(): UserConfig {
    return ObjectMapper().registerModule(KotlinModule()).readValue(File("src/test/resources/UserConfig.json"))
  }

//  private fun getStringUserConfigData(): String {
//    val userConfig = UserConfig(id = "id", pricePerLtr = BigDecimal.ONE, userId = "user-id", vendorName = "vender name", milkConfigs = listOf(MilkConfig(userId = "user-id", day = 1, quantity = 1)))
//
//    return ObjectMapper().writeValueAsString(userConfig)
//
//  }

  @Test
  fun test_calculateInvoiceDetails_forfirst10days() {
    val milkNotTaken = listOf(1, 2, 3, 4)
    val extraMilk = 3

    val mapConfig = mutableMapOf<Int, Long>()
    mapConfig[0] = 0
    mapConfig[1] = 0
    mapConfig[2] = 2
    mapConfig[3] = 0
    mapConfig[4] = 2
    mapConfig[5] = 0
    mapConfig[6] = 3

    val calculateInvoiceDetails = calculateFeeService.calculateInvoiceDetails(mapConfig = mapConfig,
        month = 7, year = 2019, price = BigDecimal.valueOf(37),
        milkNotTaken = listOf(1, 2, 9).toList(), day = 10)

    calculateInvoiceDetails.map { e -> e.totalCostPerDay }.reduce(BigDecimal::add).subscribe {
      Assert.isTrue(it == BigDecimal(185), "Milk calculation error")
    }
  }

  @Test
  fun test_calculateInvoiceDetails() {
    val milkNotTaken = listOf(1, 2, 3, 4)
    val extraMilk = 3

    val mapConfig = mutableMapOf<Int, Long>()
    mapConfig[0] = 0
    mapConfig[1] = 0
    mapConfig[2] = 2
    mapConfig[3] = 0
    mapConfig[4] = 2
    mapConfig[5] = 0
    mapConfig[6] = 3

    val calculateInvoiceDetails = calculateFeeService.calculateInvoiceDetails(mapConfig = mapConfig,
        month = 7, year = 2019, price = BigDecimal.valueOf(37),
        milkNotTaken = listOf(1, 2, 9).toList(), day = 0)

    calculateInvoiceDetails.map { e -> e.totalCostPerDay }.reduce(BigDecimal::add).subscribe {
      Assert.isTrue(it == BigDecimal(962), "Milk calculation error")
    }
  }

}

