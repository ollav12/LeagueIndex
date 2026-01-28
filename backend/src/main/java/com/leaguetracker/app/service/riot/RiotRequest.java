package com.leaguetracker.app.service.riot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.web.HttpClientConfig;
import com.leaguetracker.app.service.riot.endpoint.RiotEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RiotRequest<T> {

    private final RiotRateLimiter rateLimiter;
    private final HttpClientConfig httpClientConfig;
    private final RestTemplate restTemplate;
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
            HttpClientConfig httpClientConfig,
            RestTemplate restTemplate,
            RiotRateLimiter rateLimiter,
            Object... params) {
        this.endpoint = endpoint;
        this.region = region;
        this.mapper = mapper;
        this.responseType = responseType;
        this.httpClientConfig = httpClientConfig;
        this.restTemplate = restTemplate;
        this.rateLimiter = rateLimiter;
        this.params = params;
    }

    public T execute() {
        rateLimiter.acquire();

        String url = httpClientConfig.createUrl(endpoint, region, params);

        try {
            return restTemplate.getForObject(url, responseType);
        } catch (Exception e) {
            log.error("Failed to fetch Riot data from {}", url, e);
            throw e;
        }
    }
}