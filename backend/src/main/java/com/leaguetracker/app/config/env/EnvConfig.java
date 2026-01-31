package com.leaguetracker.app.config.env;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Setter
@Configuration
@ConfigurationProperties(prefix = "riot")
@Validated
@Getter
public class EnvConfig {

    @NotBlank
    private String apiKey;
}
