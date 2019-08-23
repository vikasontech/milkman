package com.example.demo.web.rest.route

import com.example.demo.documents.SampleRequest
import com.example.demo.documents.UserConfig
import com.example.demo.service.UserConfigService
import org.reactivestreams.Publisher
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import javax.print.attribute.standard.Media

@Component
class MyControllerHandlerFun(val userConfigService: UserConfigService) {
//  fun createUserConfigData(userConfig: Publisher<UserConfig>): Mono<UserConfig> {
//    return userConfigService.saveUserConfig(userConfig.toMono())
//  }

  fun createUserConfig(serverRequest: ServerRequest): Mono<ServerResponse> {
    val userConfig: Mono<UserConfig> = serverRequest.bodyToMono(UserConfig::class.java)
    userConfig.subscribe { println(it) }
    return ServerResponse.ok().build();
  }

  fun testConfig(serverRequest: ServerRequest): Mono<ServerResponse> {
    val userConfig: Mono<SampleRequest> = serverRequest.bodyToMono(SampleRequest::class.java)
//    userConfig.subscribe { println(it) }
    return ServerResponse.ok().body(userConfig, SampleRequest::class.java);
  }
}
@Controller
class MyControllerRoutingFun(val myControllerHandlerFun: MyControllerHandlerFun) {
  @Bean
  fun routing(): RouterFunction<ServerResponse> {
    return route(GET("/api/hello"),
        HandlerFunction { ServerResponse.ok().bodyToServerSentEvents(Mono.justOrEmpty("Jai Shri Ram!")) })
        // create new user
        .andRoute(POST("/api/user-config")
            .and(contentType(MediaType.APPLICATION_JSON)),
            HandlerFunction { it -> myControllerHandlerFun.createUserConfig(it)})
        .andRoute(POST("/api/test")
            .and(contentType(MediaType.APPLICATION_JSON)),
            HandlerFunction { myControllerHandlerFun.testConfig(it)})

  }
}
