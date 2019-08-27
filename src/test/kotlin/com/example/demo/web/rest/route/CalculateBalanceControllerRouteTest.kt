package com.example.demo.web.rest.route

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@RunWith(SpringRunner::class)
@WebFluxTest
class CalculateBalanceControllerRouteTest(val webTestClient: WebTestClient) {

  @Test
  fun calculating() {
    webTestClient
  }
}