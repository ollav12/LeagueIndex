package com.leaguetracker.app.service.riot.endpoint;

import com.leaguetracker.app.service.riot.RiotRateLimiter;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.web.HttpClientConfig;
import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.service.riot.RiotRequest;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AccountEndpoint {

    private final RestTemplate restTemplate;
    private final HttpClientConfig httpClientConfig;
    private final ObjectMapper objectMapper;
    private final RiotRateLimiter rateLimiter;

    enum Account {
        findByRiotId,
        findByPuuid
    }

    public RiotAccountResponse findByRiotId(String region, String summonerName, String tag) {
        RiotRequest<RiotAccountResponse> request = new RiotRequest<>(
                RiotEndpoint.ACCOUNT_BY_RIOT_ID,
                Helper.getRiotApiRegion(region),
                objectMapper,
                RiotAccountResponse.class,
                httpClientConfig,
                restTemplate,
                rateLimiter,
                summonerName,
                tag
        );
        return request.execute();
    }

    public RiotAccountResponse findByPuuid(String puuid, String region) {
        RiotRequest<RiotAccountResponse> request = new RiotRequest<>(
                RiotEndpoint.ACCOUNT_BY_PUUID,
                Helper.getRiotApiRegion(region),
                objectMapper,
                RiotAccountResponse.class,
                httpClientConfig,
                restTemplate,
                rateLimiter,
                puuid
        );
        return request.execute();
    }
}