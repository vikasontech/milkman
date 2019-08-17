package com.example.demo.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.*

object Utils {
  fun getID(): String {
    return UUID.randomUUID().toString()
  }

  fun objectMapper(): ObjectMapper? {
    return ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(JavaTimeModule())
  }
}
