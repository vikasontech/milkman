package com.example.demo.web.rest.route

import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.example.demo.facade.impl.CalculateFeeFacadeImpl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.EntityResponse
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Controller
class CalculateBalanceControllerHandler(val calculateFeeFacade: CalculateFeeFacadeImpl) {

  fun calculate(serverRequest: ServerRequest): Mono<EntityResponse<Mono<Invoice>>> {
    val result = serverRequest.bodyToMono(CalculateMonthlyInvoiceRequest::class.java)
                .flatMap{ calculateFeeFacade.calculateMonthlyPrice(it) }

    return EntityResponse.fromObject(result)
        .status(HttpStatus.OK)
        .build();
  }


  fun calculate_v2(serverRequest: ServerRequest): Mono<EntityResponse<Invoice>> {


    return serverRequest.bodyToMono(CalculateMonthlyInvoiceRequest::class.java)
        .flatMap{ calculateFeeFacade.calculateMonthlyPrice(it) }
        .switchIfEmpty (Mono.empty<Invoice>())
        .flatMap { EntityResponse.fromObject(it).status(HttpStatus.OK).build() }

  }
}


