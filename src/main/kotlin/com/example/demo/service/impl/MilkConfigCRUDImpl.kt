package com.example.demo.service.impl

import com.example.demo.documents.UserConfig
import com.example.demo.documents.UserConfigRepo
import com.example.demo.service.MilkConfigCRUD
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.*

@Service
class MilkConfigCRUDImpl(val repo: UserConfigRepo) : MilkConfigCRUD {

  override fun saveUserConfig(userDetails: Publisher<UserConfig>): Mono<UserConfig> {
    return userDetails
        .toMono()
        .map { e -> e.copy(id = Utility.getID()) }
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

object Utility {
  fun getID(): String {
    return UUID.randomUUID().toString()
  }
}
