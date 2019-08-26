package com.example.demo.web.rest.route

import com.example.demo.documents.MilkConfig
import com.example.demo.documents.SampleRequest
import com.example.demo.documents.UserConfig
import com.example.demo.service.UserConfigService
import com.example.demo.service.impl.Utils
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import reactor.test.publisher.TestPublisher
import java.math.BigDecimal
import java.util.concurrent.Flow

@RunWith(SpringRunner::class)
@WebFluxTest
class MyControllerHandlerFunTest {
  @Autowired
  lateinit var webTestClient: WebTestClient
  @Autowired
  lateinit var myControllerRoutingFun: MyControllerHandlerFun

  @MockBean
  lateinit var userConfigService: UserConfigService

  fun test() {
    webTestClient.get()
        .uri("/api/hello")
        .exchange()
        .expectStatus().isOk
  }

  @Test
  fun createNewUser() {

    val userConfig: UserConfig =
        UserConfig(pricePerLtr = BigDecimal.ONE,
            vendorName = "vikas",
            milkConfigs = listOf(MilkConfig(day = 1, quantity = 1, userId = "")), userId = "", id = "")

    val mono = Mono.just(userConfig)
    Mockito.`when`(userConfigService.saveUserConfig(any))
        .thenReturn(Mono.just(userConfig.copy(id = "some value")))

    webTestClient.post()
        .uri("/api/user-config")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .body(mono, UserConfig::class.java)
//        .body(userConfigPublisher, UserConfig::class.java)
        .exchange()
        .expectStatus().isOk
//        .expectBody()
//        .jsonPath("$.id").isNotEmpty
  }
}