package com.example.demo.facade

import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import reactor.core.publisher.Mono

interface CalculateFeeFacade {
  fun calculateMonthlyPrice(calculateMonthlyInvoiceRequest: CalculateMonthlyInvoiceRequest): Mono<Invoice>
}
