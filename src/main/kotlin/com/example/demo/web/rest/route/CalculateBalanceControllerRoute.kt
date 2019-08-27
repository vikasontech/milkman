package com.example.demo.web.rest.route

import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Controller
class CalculateBalanceControllerRoute {
  @Bean
  fun calculating() {
    route(GET("/api/calculate").and(contentType(MediaType.APPLICATION_JSON)),
        HandlerFunction { ServerResponse.ok().body(fromObject("this is response")) })
  }
}