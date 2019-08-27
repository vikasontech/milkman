package com.example.demo

import org.mockito.Mockito


object KotlinMockUtils {

// This class is required to handle NPE when using mockito.any for mocking
//  pls see https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791 for more details
  fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
  }
  fun <T> any(x: Class<T>): T {
    Mockito.any<T>(x)
    return uninitialized()
  }
  fun <T> uninitialized():T = null as T

}