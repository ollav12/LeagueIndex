package com.leaguetracker.app.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record RiotAccountResponse(String puuid, String gameName, String tagLine) {}
