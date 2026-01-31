package com.leaguetracker.app.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record RiotLeagueResponse(List<RiotLeagueEntry> leagues) {

    @JsonCreator
    public RiotLeagueResponse(List<RiotLeagueEntry> leagues) {
        this.leagues = leagues != null ? leagues : List.of();
    }

    @JsonValue
    public List<RiotLeagueEntry> leagues() {
        return leagues;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    public static record RiotLeagueEntry(
            String queueType,
            String tier,
            String rank,
            String puuid,
            int leaguePoints,
            int wins,
            int losses,
            boolean veteran,
            boolean inactive,
            boolean freshBlood,
            boolean hotStreak) {}
}
