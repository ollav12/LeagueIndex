package com.leaguetracker.app.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leaguetracker.app.model.Rank;
import java.util.List;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record SummonerLookupResponse(String puuid, int profileIconId, int summonerLevel, List<Rank> ranked) {}
