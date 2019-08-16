package com.example.demo.service.impl

import com.example.demo.documents.MilkConfig
import com.example.demo.documents.UserConfig
import com.example.demo.documents.UserConfigRepo
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class MilkConfigCRUDImplTest {

  @Mock
  lateinit var repo: UserConfigRepo

  @InjectMocks
  lateinit var milkConfigCRUD: MilkConfigCRUDImpl

  @Test
  fun saveUserConfig() {
    //when
    val userConfig = UserConfig(
        id = "id",
        pricePerLtr = BigDecimal.ONE,
        userId = "user-id",
        vendorName = "vender name",
        milkConfigs = listOf(
            MilkConfig(userId = "user-id",
                day = 1,
                quantity = 1))
    )

    val userConfigMono = Mono.just(userConfig)
    Mockito.`when`(repo.save(any(UserConfig::class.java))).thenReturn(userConfigMono);

    StepVerifier.create(
        milkConfigCRUD.saveUserConfig(userConfigMono))
        .expectNext(userConfig)
        .verifyComplete()


    StepVerifier.create(
        milkConfigCRUD.update(userConfigMono))
        .expectNext(userConfig)
        .verifyComplete()

  }

  @Test
  fun queryUserConfigData() {
    val userId = "user-id"
    val userConfig = UserConfig(
        id = "id",
        pricePerLtr = BigDecimal.ONE,
        userId = userId,
        vendorName = "vender name",
        milkConfigs = listOf(
            MilkConfig(userId = userId,
                day = 1,
                quantity = 1))
    )
    val userConfigMono = Mono.just(userConfig)
    Mockito.`when`(repo.findByUserId(userId)).thenReturn(userConfigMono);
    StepVerifier.create(
        milkConfigCRUD.queryUserConfigData(userId))
        .expectNext(userConfig)
        .verifyComplete()
  }
}