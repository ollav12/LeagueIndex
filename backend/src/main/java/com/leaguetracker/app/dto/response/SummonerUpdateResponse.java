package com.leaguetracker.app.dto.response;

import lombok.Builder;

@Builder
public record SummonerUpdateResponse(
        SummonerLookupResponse summonerLookupResponse, SummonerMatchesResponse summonerMatchesResponse) {}
