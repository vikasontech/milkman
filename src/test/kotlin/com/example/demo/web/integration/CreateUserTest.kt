package com.example.demo.web.integration

import com.example.demo.KotlinMockUtils
import com.example.demo.documents.MilkConfig
import com.example.demo.documents.UserConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureWebTestClient
@RunWith(SpringRunner::class)
class CreateUserTest {

  @Autowired
  lateinit var webTestClient:WebTestClient

  @Test
  fun createNewUser() {

    val userConfig: UserConfig =
        UserConfig(pricePerLtr = BigDecimal.ONE,
            vendorName = "vikas",
            milkConfigs = listOf(MilkConfig(day = 1, quantity = 1, userId = "")), userId = "", id = "")

    webTestClient.post()
        .uri("/api/user-config")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .body(Mono.just(userConfig), UserConfig::class.java)
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .jsonPath("$.id").isNotEmpty
  }

}