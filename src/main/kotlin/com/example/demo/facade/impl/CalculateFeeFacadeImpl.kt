package com.example.demo.facade.impl

import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.example.demo.facade.CalculateFeeFacade
import com.example.demo.service.CalculateFeeService
import com.example.demo.service.UserConfigService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
class CalculateFeeFacadeImpl(private val calculateFeeService: CalculateFeeService,
                             private val userConfigService: UserConfigService) : CalculateFeeFacade {

  override fun calculateMonthlyPrice(calculateMonthlyInvoiceRequest: CalculateMonthlyInvoiceRequest): Mono<Invoice> {

    return userConfigService.queryUserConfigData(calculateMonthlyInvoiceRequest.userId)
        .flatMap{ e ->
          calculateFeeService
              .calculateMonthlyPrice(userConfig = e,
                  calculateMonthlyInvoiceRequest = calculateMonthlyInvoiceRequest)
        }
  }

}