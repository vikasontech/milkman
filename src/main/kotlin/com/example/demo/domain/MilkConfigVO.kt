package com.example.demo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class MilkConfigVO(
    val day: Int,
    val quantity: Int
)