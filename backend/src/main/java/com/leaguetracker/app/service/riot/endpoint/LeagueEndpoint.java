package com.leaguetracker.app.service.riot.endpoint;

import com.leaguetracker.app.service.riot.RiotRateLimiter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.web.HttpClientConfig;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
@RequiredArgsConstructor
public class LeagueEndpoint {

    private final RestTemplate restTemplate;
    private final HttpClientConfig httpClientConfig;
    private final ObjectMapper objectMapper;
    private final RiotRateLimiter rateLimiter;

    public RiotLeagueResponse findByPuuid(String puuid, String region) {
        RiotRequest<RiotLeagueResponse> request = new RiotRequest<>(
                RiotEndpoint.LEAGUE_BY_PUUID,
                region,
                objectMapper,
                RiotLeagueResponse.class,
                httpClientConfig,
                restTemplate,
                rateLimiter,
                puuid);
        return request.execute();
    }

    public RiotLeagueResponse findBySummonerId(String summonerId, String region) {
        RiotRequest<RiotLeagueResponse> request = new RiotRequest<>(
                RiotEndpoint.LEAGUE_BY_SUMMONER,
                region,
                objectMapper,
                RiotLeagueResponse.class,
                httpClientConfig,
                restTemplate,
                rateLimiter,
                summonerId);
        return request.execute();
    }

}
