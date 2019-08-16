package com.example.demo.service.impl

import com.example.demo.documents.MilkConfig
import com.example.demo.documents.UserConfig
import com.example.demo.service.CalculateFeeService
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Service
class CalculateFeeServiceImpl : CalculateFeeService {

  override fun calculateMonthlyPrice(userConfig: Publisher<UserConfig>): Mono<UserConfig> {

    userConfig.toMono()
        .map { e -> e.milkConfigs }
        .map { e ->  Flux.fromIterable(e)}
        .map {  e->  e.map {
//          e -> e.
        } }


    return Mono.empty();
  }
}