package com.example.demo.service.impl

import com.example.demo.documents.UserConfig
import com.example.demo.documents.UserConfigRepo
import com.example.demo.service.UserConfigService
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Service
class UserConfigServiceImpl(val repo: UserConfigRepo) : UserConfigService {

  override fun saveUserConfig(userDetail: Publisher<UserConfig>): Mono<UserConfig> {
    return userDetail
        .toMono()
        .map { e -> e.copy(id = Utils.getID()) }
        .flatMap { e -> saveUserDetails(e) }
        .log()
  }

  override fun queryUserConfigData(userId: String): Mono<UserConfig> {
    return repo.findByUserId(userId)
  }

  override fun update(userDetail: Publisher<UserConfig>): Mono<UserConfig> {
    return userDetail.toMono()
        .flatMap { e -> saveUserDetails(e) }
  }

  private fun saveUserDetails(userDetail: UserConfig): Mono<UserConfig> {
    return Mono.justOrEmpty(userDetail)
        .flatMap { e -> repo.save(e) }
  }
}

