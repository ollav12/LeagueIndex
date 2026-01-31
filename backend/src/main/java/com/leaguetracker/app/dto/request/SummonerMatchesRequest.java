package com.leaguetracker.app.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SummonerMatchesRequest(
        @NotBlank(message = "PUUID is required") String puuid,
        @NotBlank(message = "Region is required")
                @Pattern(
                        regexp = "^(br1|eun1|euw1|jp1|kr|la1|la2|na1|oc1|tr1|ru|ph2|sg2|th2|tw2|vn2)$",
                        message = "Invalid region code")
                String region,
        String lastMatchId,
        @Min(value = 1, message = "Limit must be at least 1") @Max(value = 100, message = "Limit cannot exceed 100")
                Integer limit) {}
