package com.leaguetracker.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SummonerLookupRequest(
        @NotBlank(message = "Region is required")
                @Pattern(
                        regexp = "^(br1|eun1|euw1|jp1|kr|la1|la2|na1|oc1|tr1|ru|ph2|sg2|th2|tw2|vn2)$",
                        message = "Invalid region code")
                String region,
        @NotBlank(message = "Summoner name is required")
                @Size(min = 3, max = 20, message = "Summoner name must be between 3 and 20 characters")
                String summonerName,
        @NotBlank(message = "Tag is required")
                @Size(min = 1, max = 10, message = "Tag must be between 1 and 10 characters")
                @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Tag can only contain alphanumeric characters")
                String tag) {}
