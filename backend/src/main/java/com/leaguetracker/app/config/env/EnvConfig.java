package com.leaguetracker.app.config.env;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Configuration
@ConfigurationProperties(prefix = "riot")
@Validated
@Getter
public class EnvConfig {

    @NotBlank
    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
