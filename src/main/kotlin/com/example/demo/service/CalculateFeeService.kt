package com.example.demo.service

import com.example.demo.documents.UserConfig
import com.example.demo.domain.Invoice
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

interface CalculateFeeService {
  fun calculateMonthlyPrice(userConfig: Publisher<UserConfig>): Mono<Invoice>
}
