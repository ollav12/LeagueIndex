package com.leaguetracker.app.service.riot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.WebClientConfig;
import com.leaguetracker.app.service.riot.endpoint.RiotEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class RiotRequest<T> {

    private final RiotRateLimiter rateLimiter;
    private final WebClientConfig webClientConfig;
    private final WebClient webClient;
    private final RiotEndpoint endpoint;
    private final String region;
    private final ObjectMapper mapper;
    private final Class<T> responseType;
    private final Object[] params;

    public RiotRequest(
            RiotEndpoint endpoint,
            String region,
            ObjectMapper mapper,
            Class<T> responseType,
            WebClientConfig webClientConfig,
            WebClient webClient,
            RiotRateLimiter rateLimiter,
            Object... params) {
        this.endpoint = endpoint;
        this.region = region;
        this.mapper = mapper;
        this.responseType = responseType;
        this.webClientConfig = webClientConfig;
        this.webClient = webClient;
        this.rateLimiter = rateLimiter;
        this.params = params;
    }


    public T execute() {
        rateLimiter.acquire();

        return webClient.get()
                .uri(webClientConfig.createUrl(endpoint, region, params))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
}