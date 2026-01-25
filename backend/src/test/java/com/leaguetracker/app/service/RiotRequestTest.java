package com.leaguetracker.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.leaguetracker.app.config.TestWebClientConfig;
import com.leaguetracker.app.config.WebClientConfig;
import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.service.riot.RiotRateLimiter;
import com.leaguetracker.app.service.riot.RiotRequest;
import com.leaguetracker.app.service.riot.endpoint.RiotEndpoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RiotRequestTest {

    private WireMockServer wireMockServer;
    private ObjectMapper objectMapper;
    private WebClient webClient;
    private WebClientConfig webClientConfig;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8099);
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        objectMapper = new ObjectMapper();
        webClient = WebClient.builder().baseUrl("http://localhost:" + wireMockServer.port()).build();
        webClientConfig = new TestWebClientConfig();
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    //@Test
    void testRateLimitRetry() {

    }
}