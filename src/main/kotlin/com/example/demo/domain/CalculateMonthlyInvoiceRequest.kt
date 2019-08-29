package com.example.demo.domain

import kotlin.system.measureNanoTime

data class CalculateMonthlyInvoiceRequest (
    val day: Int = 0,
    val month: Int,
    val year: Int,
    val userId: String,
    val extraMilk: Int,
    val datesMilkNotTaken: List<Int>
){

}
