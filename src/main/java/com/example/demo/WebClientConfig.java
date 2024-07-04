package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Component
@Slf4j
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl("https://dog.ceo/api/breeds/image/random")
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(requestFilterFunction());
                    exchangeFilterFunctions.add(responseFilterFunction());
                })
                .build();
    }

    private ExchangeFilterFunction requestFilterFunction() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {

            final String uri = clientRequest.method().name() + " " + clientRequest.url();
            log.info("WEBCLIENT REQUEST => {}", uri);

            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction responseFilterFunction() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {

            final int statusCode = clientResponse.statusCode().value();
            assert HttpStatus.resolve(statusCode) != null;
            final String reasonPhrase = HttpStatus.resolve(statusCode).getReasonPhrase();

            log.info("WEBCLIENT RESPONSE STATUS => {} {}", statusCode, reasonPhrase);

            return clientResponse
                    .bodyToMono(DataBuffer.class)
                    .map(dataBuffer -> {
                        ClientResponse clonedClientResponse = clientResponse.mutate().body(Flux.just(dataBuffer)).build();

                        log.info("WEBCLIENT RESPONSE BODY => {}", dataBuffer.toString(Charset.defaultCharset()));

                        return clonedClientResponse;
                    });
        });
    }
}
