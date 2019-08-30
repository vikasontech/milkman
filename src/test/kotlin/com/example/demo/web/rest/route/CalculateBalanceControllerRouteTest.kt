package com.example.demo.web.rest.route

import com.example.demo.KotlinMockUtils.any
import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.example.demo.facade.CalculateFeeFacade
import com.example.demo.facade.impl.CalculateFeeFacadeImpl
import com.example.demo.service.UserConfigService
import com.example.demo.service.impl.Utils
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDate

@RunWith(SpringRunner::class)
@WebFluxTest
class CalculateBalanceControllerRouteTest {

  @Autowired
  lateinit var webTestClient: WebTestClient

  @MockBean
  lateinit var userConfigService: UserConfigService

  @MockBean
  lateinit var calculateFeeFacade: CalculateFeeFacadeImpl

  @Test
  fun print_json() {

    println(Utils.objectMapper()?.writeValueAsString(CalculateMonthlyInvoiceRequest(day = 0,
        userId = "xy",
        year = 2019,
        datesMilkNotTaken = emptyList(),
        extraMilk = 3,
        month = 8)))
  }

  @Test
  fun calculating() {

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

    Mockito.`when`(calculateFeeFacade.calculateMonthlyPrice(any()))
        .thenReturn(Mono.just(expectedResult))

    webTestClient.post()
        .uri("/api/calculate")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(request))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .json(Utils.objectMapper()!!.writeValueAsString(expectedResult))
  }
}