package com.example.demo

import com.example.demo.documents.MilkConfig
import com.example.demo.documents.UserConfig
import com.example.demo.service.UserConfigService
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class UserConfigControllerHandlerTest {
  @InjectMocks
  lateinit var userConfigControllerHandler: UserConfigControllerHandler

  @Mock
  lateinit var userConfigService: UserConfigService

  @Test
  fun createUserConfig() {

    val userConfigMono = Mono.just(UserConfig(pricePerLtr = BigDecimal.ONE,
        vendorName = "vikas",
        milkConfigs = listOf(MilkConfig(day = 1, quantity = 1, userId = "")), userId = "", id = ""))

    val serverRequest = MockServerRequest.builder()
        .body(userConfigMono)

    Mockito.`when`( userConfigService.saveUserConfig(KotlinMockUtils.any()))
        .thenReturn(userConfigMono)

    val serverResponseMono = userConfigControllerHandler.createUserConfig(serverRequest)

    StepVerifier.create(serverResponseMono)
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }
}