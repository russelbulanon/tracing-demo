package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class ApiService {

    private WebClient webClient;

    public Mono<ApiResponse> getHello() {
        log.info("SERVICE start => getHello");
        return webClient
                .get()
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .doOnNext(response -> log.info("SERVICE status => {}", response.getStatus()));
    }
}
