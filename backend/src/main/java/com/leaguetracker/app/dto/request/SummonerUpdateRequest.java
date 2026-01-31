package com.leaguetracker.app.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record SummonerUpdateRequest(
        String puuid,
        String region,
        String summonerName,
        String tag,
        String lastMatchId,
        @Min(value = 1, message = "Limit must be at least 1") @Max(value = 100, message = "Limit cannot exceed 100")
                Integer limit) {}
