package com.example.demo.web.rest.route

import com.example.demo.documents.UserConfig
import com.example.demo.domain.CalculateMonthlyInvoiceRequest
import com.example.demo.domain.Invoice
import com.example.demo.facade.CalculateFeeFacade
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Mono

@Controller
class CalculateBalanceControllerHandler(val calculateFeeFacade: CalculateFeeFacade) {

  fun calculate(serverRequest: ServerRequest): Mono<ServerResponse> {

    return ServerResponse.ok()
        .headers { it.contentType = MediaType.APPLICATION_JSON }
        .bodyToServerSentEvents(
            serverRequest.bodyToMono(CalculateMonthlyInvoiceRequest::class.java)
                .flatMap{ calculateFeeFacade.calculateMonthlyPrice(it) }
        )
  }

}


