package com.example.demo.service.impl

import com.example.demo.documents.UserConfig
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class CalculateFeeServiceImplTest {

  @InjectMocks
  lateinit var calculateFeeService: CalculateFeeServiceImpl

  @Test
  fun calculateMonthlyPriceTest() {
    val calculateMonthlyPriceMono= calculateFeeService.calculateMonthlyPrice(Mono.justOrEmpty(getMockUserConfigData()))
    StepVerifier.create(calculateMonthlyPriceMono)
        .expectNextMatches { e -> e.descriptions.isNotEmpty() }
        .verifyComplete()
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
}

