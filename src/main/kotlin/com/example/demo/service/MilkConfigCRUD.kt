package com.example.demo.service

import com.example.demo.documents.UserConfig
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

interface MilkConfigCRUD {
  fun saveUserConfig(userDetails: Publisher<UserConfig>): Mono<UserConfig>
  fun queryUserConfigData(userId: String): Mono<UserConfig>
  fun update(userDetails: Publisher<UserConfig>): Mono<UserConfig>
}
