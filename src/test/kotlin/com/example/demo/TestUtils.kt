package com.example.demo

import org.junit.Test
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

class TestUtils {
  @Test
  fun testSubscribeOn() {
    val scheduler = Schedulers.newParallel("parallel-scheduler", 4)

    val flux = Flux.range(1, 2)
        .map { i -> 10 + i }
        .subscribeOn(scheduler)
        .map { i -> "${Thread.currentThread().name} - value: $i" }
        .log("flux log: ")

    Thread{ flux.subscribe{println(it)}}
  }

}