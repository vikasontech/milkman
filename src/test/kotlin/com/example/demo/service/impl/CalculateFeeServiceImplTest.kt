package com.example.demo.service.impl

import com.example.demo.service.WeekDays
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class CalculateFeeServiceImplTest {

  @Test
  fun getTotalNoOFDays() {
    val ld = LocalDate.of(2019, 5, 1)
    println(ld.lengthOfMonth())
//    random(noOfDays = ld.lengthOfMonth())
  }

  @Test
  fun random() {
    val startDay = 3
    var tempStartDay = startDay
    val totalDays = 31
    val price = BigDecimal.valueOf(37)
    val mapConfig = mutableMapOf<Int, Long>()
    mapConfig[0] = 0
    mapConfig[1] = 0
    mapConfig[2] = 2
    mapConfig[3] = 0
    mapConfig[4] = 2
    mapConfig[5] = 0
    mapConfig[6] = 3

    var totalPrice = BigDecimal.ZERO
    for (i in 1..totalDays) {

      val milkPerDay = mapConfig.get(tempStartDay)
      val pricePerDay = price.multiply(BigDecimal.valueOf(milkPerDay!!))
      totalPrice += pricePerDay

      println("Date : $i, ${WeekDays.new(tempStartDay)}," +
          "Milk per day: ${milkPerDay},t" +
          "total Cost per day: $pricePerDay")

      tempStartDay = if ((tempStartDay + 1) > 6) 0 else tempStartDay + 1
    }
    println(totalPrice)
  }
}
