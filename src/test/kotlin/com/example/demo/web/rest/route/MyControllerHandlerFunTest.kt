package com.example.demo.web.rest.route

import com.example.demo.documents.MilkConfig
import com.example.demo.documents.SampleRequest
import com.example.demo.documents.UserConfig
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
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
import java.math.BigDecimal

@RunWith(SpringRunner::class)
@WebFluxTest
class MyControllerHandlerFunTest {
  //  @Autowired
//  lateinit var controllerHandlerFun: MyControllerRoutingFun
  @Autowired
  lateinit var webTestClient: WebTestClient
  @MockBean
  lateinit var myControllerRoutingFun: MyControllerHandlerFun

  fun test() {
    webTestClient.get()
        .uri("/api/hello")
        .exchange()
        .expectStatus().isOk
  }

  fun createNewUser() {

    val userConfig: UserConfig =
        UserConfig(pricePerLtr = BigDecimal.ONE,
            vendorName = "vikas",
            milkConfigs = listOf(MilkConfig(day = 1, quantity = 1, userId = "")), userId = "", id = "")


    webTestClient.post()
        .uri("/api/user-config")
        .body(Mono.just(userConfig), UserConfig::class.java)
        .exchange()
        .expectStatus().isOk
  }

  @Test
  fun testUserConfig() {
    val userConfig = SampleRequest(name = "vikas")
    webTestClient.post()
        .uri("/api/test")
        .body(BodyInserters.fromObject(userConfig))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk
  }
}