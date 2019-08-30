package com.example.demo.web.rest.route

import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.example.demo.facade.impl.CalculateFeeFacadeImpl
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Controller
class CalculateBalanceControllerHandler(val calculateFeeFacade: CalculateFeeFacadeImpl) {

  fun calculate(serverRequest: ServerRequest): Mono<ServerResponse> {
    val result = serverRequest.bodyToMono(CalculateMonthlyInvoiceRequest::class.java)
                .flatMap{ calculateFeeFacade.calculateMonthlyPrice(it) }

    return ServerResponse.ok()
        .headers { it.contentType = MediaType.APPLICATION_JSON }
        .body(result, Invoice::class.java)
  }

}


