package com.leaguetracker.app.config.web;

import com.leaguetracker.app.config.env.EnvConfig;
import com.leaguetracker.app.service.riot.endpoint.RiotEndpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final EnvConfig envConfig;

    @Bean
    public WebClient riotWebClient() {
        return WebClient.builder()
                .defaultHeader("X-Riot-Token", envConfig.getApiKey())
                .build();
    }

    public String createUrl(RiotEndpoint endpoint, String region, Object... params) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(region + ".api.riotgames.com/")
                .path(endpoint.format(params))
                .build()
                .toUriString();
    }
}