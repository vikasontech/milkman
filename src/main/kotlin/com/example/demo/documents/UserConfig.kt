package com.example.demo.documents

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Document
data class UserConfig(
    @Id val id: String,
    val userId: String,
    val vendorName: String,
    val pricePerLtr: BigDecimal,
    val milkConfigs: List<MilkConfig>
)

@Document
data class SampleRequest (
    val name:String
)
@Repository
interface UserConfigRepo : ReactiveCrudRepository<UserConfig, String> {
  fun findByUserId(userId: String): Mono<UserConfig>
}