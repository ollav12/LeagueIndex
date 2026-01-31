package com.leaguetracker.app.service.riot.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.web.HttpClientConfig;
import com.leaguetracker.app.dto.response.RiotMatchListResponse;
import com.leaguetracker.app.dto.response.RiotMatchResponse;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.service.riot.RiotRateLimiter;
import com.leaguetracker.app.service.riot.RiotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MatchEndpoint {

    private final RestTemplate restTemplate;
    private final HttpClientConfig httpClientConfig;
    private final ObjectMapper objectMapper;
    private final RiotRateLimiter rateLimiter;

    public RiotMatchResponse findByMatchId(String matchId, String region) {
        RiotRequest<RiotMatchResponse> request = new RiotRequest<>(
                RiotEndpoint.MATCH_BY_ID,
                Helper.getRiotApiRegion(region),
                objectMapper,
                RiotMatchResponse.class,
                httpClientConfig,
                restTemplate,
                rateLimiter,
                matchId);
        return request.execute();
    }

    public RiotMatchListResponse findByPuuid(String puuid, String region, int start, int count) {
        RiotRequest<RiotMatchListResponse> request = new RiotRequest<>(
                RiotEndpoint.MATCH_LIST_BY_PUUID,
                Helper.getRiotApiRegion(region),
                objectMapper,
                RiotMatchListResponse.class,
                httpClientConfig,
                restTemplate,
                rateLimiter,
                puuid,
                start,
                count);
        return request.execute();
    }
}
