package com.example.demo.service.impl

import com.example.demo.documents.UserConfig
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.util.Assert
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.File
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class CalculateFeeServiceImplTest {

  @InjectMocks
  lateinit var calculateFeeService: CalculateFeeServiceImpl

  @Test
  fun calculateMonthlyPriceTest() {
    val calculateMonthlyPriceMono = calculateFeeService.calculateMonthlyPrice(Mono.justOrEmpty(getMockUserConfigData()))
    calculateMonthlyPriceMono
        .subscribe { e ->
          Assert.isTrue(e.totalAmount == BigDecimal.valueOf(1221),
              "Total amount should be 1221")
        }
  }

  @Test
  fun getMilkConfigDetailsTest() {

    val userConfig = getMockUserConfigData()

    val map = calculateFeeService.getMilkConfigDetails(Mono.just(userConfig))
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
  fun test_calculateInvoiceDetailsV2() {
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

    val calculateInvoiceDetailsV2 = calculateFeeService.calculateInvoiceDetails(mapConfig = mapConfig, month = 7, year = 2019, price = BigDecimal.valueOf(37), milkNotTaken = listOf(1, 2, 9).toList())
    calculateInvoiceDetailsV2.map { e -> e.totalCostPerDay }
        .reduce (BigDecimal::add)
        .subscribe {
            Assert.isTrue(it == BigDecimal(962),"Milk calculation error")
        }
  }
}

