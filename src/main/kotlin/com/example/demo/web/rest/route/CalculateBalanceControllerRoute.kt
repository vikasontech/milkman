package com.example.demo.web.rest.route

import com.example.demo.domain.Invoice
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.EntityResponse
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import org.springframework.web.reactive.function.server.HandlerFunction as HandlerFunction

@Controller
class CalculateBalanceControllerRoute(val calculateBalanceControllerHandler:CalculateBalanceControllerHandler) {
  @Bean
  fun calculating(): RouterFunction<EntityResponse<Invoice>> {
    return route(POST("/api/calculate")
        .and(contentType(MediaType.APPLICATION_JSON))
        .and(accept(MediaType.APPLICATION_JSON)),
        HandlerFunction{ calculateBalanceControllerHandler.calculate_v2(it) })
  }

}