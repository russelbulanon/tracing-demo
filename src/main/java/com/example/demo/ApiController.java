package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@AllArgsConstructor
public class ApiController {
    private ApiService apiService;

    @GetMapping("/hello")
    public Mono<ApiResponse> getHello() {
        log.info("CONTROLLER => getHello");
        return apiService.getHello();
    }
}
