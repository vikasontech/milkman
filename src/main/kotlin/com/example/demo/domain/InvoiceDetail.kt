package com.example.demo.domain

import java.math.BigDecimal

data class InvoiceDetail(val date: Int,
                         val dayInWord: String,
                         val milkPerDay: Int,
                         val totalCostPerDay: BigDecimal)
