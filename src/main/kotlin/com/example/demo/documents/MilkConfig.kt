package com.example.demo.documents

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class MilkConfig(
    @Id val userId: String,
    val day: Int,
    val quantity: Int
)