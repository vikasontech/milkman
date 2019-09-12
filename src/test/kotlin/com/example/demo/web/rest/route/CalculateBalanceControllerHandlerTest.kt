package com.example.demo.web.rest.route

import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.example.demo.facade.impl.CalculateFeeFacadeImpl
import com.example.demo.service.impl.Utils
import org.hamcrest.Matchers
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.codec.support.DefaultServerCodecConfigurer
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.coroutines.experimental.buildIterator
import org.springframework.web.server.session.DefaultWebSessionManager
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.web.server.adapter.DefaultServerWebExchange
import org.springframework.web.server.ServerWebExchange
import org.springframework.mock.http.server.reactive.MockServerHttpResponse
import org.springframework.web.server.i18n.FixedLocaleContextResolver
import org.springframework.web.server.i18n.LocaleContextResolver


@RunWith(MockitoJUnitRunner::class)
class CalculateBalanceControllerHandlerTest {
  @Mock
  lateinit var calculateFeeFacade: CalculateFeeFacadeImpl
  @InjectMocks
  lateinit var calculateBalanceControllerHandler: CalculateBalanceControllerHandler

  @Test
  fun test_calculate() {
    val request = CalculateMonthlyInvoiceRequest(day = 0,
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

    Mockito.`when`(calculateFeeFacade.calculateMonthlyPrice(request))
        .thenReturn(Mono.just(expectedResult))

    val calculate = calculateBalanceControllerHandler.calculate_v2(serverRequest)
    StepVerifier.create(calculate.log("checking: "))
        .expectSubscription()
        .consumeNextWith {it ->
          Assert.assertTrue(it.entity() == expectedResult)
        }
        .verifyComplete()

    Assert.assertTrue(serverRequest.bodyToMono(CalculateMonthlyInvoiceRequest::class.java)
        .block() == request)

    Assert.assertTrue(
        calculateFeeFacade.calculateMonthlyPrice(request).block() == expectedResult)
  }

  @Test
  fun dummy() {
    val request = CalculateMonthlyInvoiceRequest(day = 0,
        userId = "",
        year = 2019,
        datesMilkNotTaken = emptyList(),
        extraMilk = 3,
        month = 8)

    println(Utils.objectMapper()?.writeValueAsString(request))
  }
}