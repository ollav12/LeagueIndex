package com.leaguetracker.app.config;

import com.leaguetracker.app.config.env.EnvConfig;
import com.leaguetracker.app.config.web.WebClientConfig;
import com.leaguetracker.app.service.riot.endpoint.RiotEndpoint;

public class TestWebClientConfig extends WebClientConfig {
    public TestWebClientConfig() {
        super(new EnvConfig());
    }

    @Override
    public String createUrl(RiotEndpoint endpoint, String region, Object... params) {
        // Return only the path so calls go to the WebClient baseUrl (WireMock)
        return "/" + endpoint.format(params);
    }
}
