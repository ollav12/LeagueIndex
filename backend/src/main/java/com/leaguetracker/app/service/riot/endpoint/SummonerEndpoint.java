package com.leaguetracker.app.service.riot.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.web.HttpClientConfig;
import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.service.riot.RiotRateLimiter;
import com.leaguetracker.app.service.riot.RiotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SummonerEndpoint {

    private final RestTemplate restTemplate;
    private final HttpClientConfig httpClientConfig;
    private final ObjectMapper objectMapper;
    private final RiotRateLimiter rateLimiter;

    public RiotSummonerResponse findByPuuid(String puuid, String region) {
        RiotRequest<RiotSummonerResponse> request = new RiotRequest<>(
                RiotEndpoint.SUMMONER_BY_PUUID,
                region,
                objectMapper,
                RiotSummonerResponse.class,
                httpClientConfig,
                restTemplate,
                rateLimiter,
                puuid);
        return request.execute();
    }
}
