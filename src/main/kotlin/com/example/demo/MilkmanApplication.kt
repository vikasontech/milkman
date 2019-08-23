package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class MilkmanApplication

fun main(args: Array<String>) {
  runApplication<MilkmanApplication>(*args)
}
