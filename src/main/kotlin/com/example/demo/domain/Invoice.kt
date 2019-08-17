package com.example.demo.domain

import java.math.BigDecimal
import java.time.LocalDate

data class Invoice(val name: String,
                   val month: Int,
                   val year: Int,
                   val vendorName: String,
                   val billingDate: LocalDate,
                   val totalAmount: BigDecimal,
                   val pricePerLtr: BigDecimal,
                   val descriptions: List<InvoiceDetail>)