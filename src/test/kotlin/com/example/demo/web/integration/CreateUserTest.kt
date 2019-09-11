package com.example.demo.web.integration

import com.example.demo.KotlinMockUtils
import com.example.demo.documents.MilkConfig
import com.example.demo.documents.UserConfig
import com.example.demo.service.impl.Utils
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
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
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

    val returnResult = webTestClient.post().uri("/api/user-config").contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(userConfig), UserConfig::class.java).exchange().expectHeader().contentType("text/event-stream;charset=UTF-8").expectStatus().isOk.returnResult<ServerResponse>()

    StepVerifier.create(returnResult.responseBody)
        .expectNextCount(1)
        .verifyComplete()


  }

  @Test
  fun printJson() {

    val userConfig = UserConfig(pricePerLtr = BigDecimal.ONE, vendorName = "vikas", milkConfigs = listOf(MilkConfig(day = 1, quantity = 1, userId = "")), userId = "", id = "")

    println(Utils.objectMapper()?.writeValueAsString(userConfig))
  }
  }
