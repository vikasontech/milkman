package com.example.demo.web.rest.route

import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


class MyController {

  fun hello(request: ServerRequest): Mono<ServerResponse> {
    return ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.justOrEmpty("vedanta"), String::class.java)
  }
}

@Component
internal class HandlerFunction {

  var handler = MyController()

  @Bean
  fun router(): RouterFunction<ServerResponse> {
    return route(GET("/hello").and(accept(APPLICATION_JSON)), HandlerFunction { handler.hello(it) })
  }
}

