package com.leaguetracker.app.config.web;

import com.leaguetracker.app.config.env.EnvConfig;
import com.leaguetracker.app.service.riot.endpoint.RiotEndpoint;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

    private final EnvConfig envConfig;

    @Bean
    public RestTemplate riotRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add Riot API Key header to every request
        ClientHttpRequestInterceptor interceptor = new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                request.getHeaders().add("X-Riot-Token", envConfig.getApiKey());
                return execution.execute(request, body);
            }
        };

        restTemplate.getInterceptors().add(interceptor);

        return restTemplate;
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
