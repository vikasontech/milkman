package com.example.demo.web.rest.route

import com.example.demo.KotlinMockUtils
import com.example.demo.UserConfigControllerHandler
import com.example.demo.documents.MilkConfig
import com.example.demo.documents.UserConfig
import com.example.demo.facade.impl.CalculateFeeFacadeImpl
import com.example.demo.service.UserConfigService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.math.BigDecimal

@RunWith(SpringRunner::class)
@WebFluxTest
class UserConfigControllerRouteTest {
  @Autowired
  lateinit var webTestClient: WebTestClient
  @MockBean
  lateinit var userConfigControllerHandler: UserConfigControllerHandler
  @MockBean
  lateinit var calculateFeeFacadeImpl: CalculateFeeFacadeImpl

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

    `when`(userConfigControllerHandler.createUserConfig(KotlinMockUtils.any()))
        .thenReturn(ServerResponse.ok().body(
            BodyInserters.fromObject(userConfig.copy(id ="some value"))))

    webTestClient.post()
        .uri("/api/user-config")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
//        .accept(MediaType.APPLICATION_JSON_UTF8)
        .body(Mono.just(userConfig), UserConfig::class.java)
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM)
        .expectStatus().isOk
        .expectBody()
        .jsonPath("$.id").isEqualTo("some value")
        .json("{\"id\":\"some value\",\"userId\":\"\"," +
            "\"vendorName\":\"vikas\",\"pricePerLtr\":1,\"milkConfigs\":[{\"userId\":\"\",\"day\":1,\"quantity\":1}]}")
        .consumeWith{ println(it)}
  }
}
