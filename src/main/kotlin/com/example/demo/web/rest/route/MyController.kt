package com.example.demo.web.rest.route

import com.example.demo.UserConfigControllerHandler
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Mono

@Controller
class UserConfigControllerRoute(val handler: UserConfigControllerHandler) {
  @Bean
  fun routing(): RouterFunction<ServerResponse> {
    return route(POST("/api/user-config")
            .and(contentType(MediaType.APPLICATION_JSON)),
            HandlerFunction { handler.createUserConfig(it)})
  }
}
