package com.example.demo.facade.impl

import com.example.demo.KotlinMockUtils.any
import com.example.demo.documents.MilkConfig
import com.example.demo.documents.UserConfig
import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.example.demo.service.CalculateFeeService
import com.example.demo.service.UserConfigService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoOperator
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class CalculateFeeFacadeImplTest{

  @InjectMocks
  lateinit var calculateFeeFacade:CalculateFeeFacadeImpl
  @Mock
  lateinit var calculateFeeService: CalculateFeeService
  @Mock
  lateinit var userConfigService: UserConfigService

  @Test
  fun testCalculateMonthlyPrice_no_user_details() {

    val calculateMonthlyInvoiceRequest= CalculateMonthlyInvoiceRequest(day = 0, month = 7, extraMilk = 3, datesMilkNotTaken = emptyList(),
        year = 2019, userId = "")

    Mockito.`when`(userConfigService.queryUserConfigData(any()))
        .thenReturn(Mono.empty())

    val resultMono = calculateFeeFacade
        .calculateMonthlyPrice(calculateMonthlyInvoiceRequest)

    resultMono.subscribe{println("Result details: $it")}

    StepVerifier.create(resultMono.log("result: "))
        .expectSubscription()
        .verifyComplete()
  }


  @Test
  fun testCalculateMonthlyPrice() {

    val calculateMonthlyInvoiceRequest= CalculateMonthlyInvoiceRequest(day = 0, month = 7, extraMilk = 3, datesMilkNotTaken = emptyList(),
        year = 2019, userId = "")
    val userConfigMono = Mono.just(UserConfig(pricePerLtr = BigDecimal.ONE,
        vendorName = "vikas",
        milkConfigs = listOf(MilkConfig(day = 1, quantity = 1, userId = "")), userId = "", id = ""))

    Mockito.`when`(userConfigService.queryUserConfigData(any()))
        .thenReturn(userConfigMono)
    val expectedResult = Invoice(name = "vikas", year = 2019,
        month = 8, vendorName = "", pricePerLtr = BigDecimal.ONE,
        billingDate = LocalDate.now(), descriptions = emptyList(),
        extraMilkPerLtr = 0, totalAmount = BigDecimal.ZERO)

    Mockito.`when`(calculateFeeService.calculateMonthlyPrice(any(), any()))
        .thenReturn(Mono.just(expectedResult))

    val resultMono = calculateFeeFacade
        .calculateMonthlyPrice(calculateMonthlyInvoiceRequest)

    StepVerifier.create(resultMono.log("result: "))
        .expectSubscription()
        .expectNext(expectedResult)
        .verifyComplete()
  }

}
