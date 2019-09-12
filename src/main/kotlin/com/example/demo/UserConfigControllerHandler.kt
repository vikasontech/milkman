package com.example.demo

import com.example.demo.documents.MilkConfig
import com.example.demo.documents.UserConfig
import com.example.demo.domain.UserConfigVO
import com.example.demo.service.UserConfigService
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Mono

@Controller
class UserConfigControllerHandler(private val userConfigService: UserConfigService) {

  fun createUserConfig(serverRequest: ServerRequest): Mono<ServerResponse> {
    return serverRequest.bodyToMono(UserConfigVO::class.java)
        .map { toDoc(it) }
        .flatMap { ServerResponse.ok().bodyToServerSentEvents(userConfigService.saveUserConfig(it)) }
  }

  fun toDoc(userConfigVO: UserConfigVO): Mono<UserConfig> {
    val userConfigVOMono = Mono.justOrEmpty(userConfigVO)
    val milkConfig = userConfigVOMono.flatMapIterable { it.milkConfigs }
        .map { MilkConfig(userId = "", day = it.day, quantity = it.quantity) }
        .map { it }
        .collectList()
        .zipWith(userConfigVOMono) { a, b ->
          a.map { MilkConfig(userId = b.userId, day = it.day, quantity = it.quantity) }
        }

    return userConfigVOMono.zipWith(milkConfig) { a, b ->
      UserConfig(id = a.id, userId = a.userId, milkConfigs = b.toList(), vendorName = a.vendorName,
          pricePerLtr = a.pricePerLtr)
    }
  }
}