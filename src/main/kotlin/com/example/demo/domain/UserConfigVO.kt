package com.example.demo.domain

import com.example.demo.documents.UserConfig
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.math.BigDecimal


data class UserConfigVO(
    @Id val id: String,
    val userId: String,
    val vendorName: String,
    val pricePerLtr: BigDecimal,
    val milkConfigs: List<MilkConfigVO>
)

