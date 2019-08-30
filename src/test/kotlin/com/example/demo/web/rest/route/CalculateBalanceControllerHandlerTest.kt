package com.example.demo.web.rest.route

import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.example.demo.facade.impl.CalculateFeeFacadeImpl
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class CalculateBalanceControllerHandlerTest{
  @Mock
  lateinit var calculateFeeFacade: CalculateFeeFacadeImpl
  @InjectMocks
  lateinit var calculateBalanceControllerHandler:CalculateBalanceControllerHandler

  @Test
  fun test_calculate() {

    val request = CalculateMonthlyInvoiceRequest(day =0,
        userId = "",
        year = 2019,
        datesMilkNotTaken = emptyList(),
        extraMilk = 3,
        month = 8)

    val expectedResult = Invoice(name = "vikas", year = 2019,
        month = 8, vendorName = "", pricePerLtr = BigDecimal.ONE,
        billingDate = LocalDate.now(), descriptions = emptyList(),
        extraMilkPerLtr = 0, totalAmount = BigDecimal.ZERO)


    val serverRequest = MockServerRequest.builder()
        .body(Mono.just(request))

    val result = calculateBalanceControllerHandler.calculate(serverRequest)

    val expectedServerResponse = ServerResponse.ok()
        .body(BodyInserters.fromObject(Mono.just("")))

    StepVerifier.create(result.log("vikas: "))
        .expectSubscription()
        .assertNext { Assert.assertEquals(it.statusCode(), expectedServerResponse.block()?.statusCode()) }
        .verifyComplete()




  }
}