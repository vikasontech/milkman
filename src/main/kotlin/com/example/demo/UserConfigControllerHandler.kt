package com.example.demo

import com.example.demo.documents.UserConfig
import com.example.demo.service.UserConfigService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Mono

@Component
class UserConfigControllerHandler(private val userConfigService: UserConfigService) {

  fun createUserConfig(serverRequest: ServerRequest): Mono<ServerResponse> {
    val userConfig: Mono<UserConfig> = serverRequest.bodyToMono(UserConfig::class.java)
    return ServerResponse.ok().bodyToServerSentEvents(userConfigService.saveUserConfig(userConfig))
  }
}