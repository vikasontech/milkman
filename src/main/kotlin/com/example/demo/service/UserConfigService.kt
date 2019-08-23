package com.example.demo.service

import com.example.demo.documents.UserConfig
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

interface UserConfigService {
  fun saveUserConfig(userDetail: Publisher<UserConfig>): Mono<UserConfig>
  fun queryUserConfigData(userId: String): Mono<UserConfig>
  fun update(userDetail: Publisher<UserConfig>): Mono<UserConfig>
}
