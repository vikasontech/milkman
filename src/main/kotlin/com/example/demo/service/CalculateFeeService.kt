package com.example.demo.service

import com.example.demo.documents.UserConfig
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

interface CalculateFeeService {
  fun calculateMonthlyPrice(userConfig: Publisher<UserConfig>): Mono<UserConfig>
}
