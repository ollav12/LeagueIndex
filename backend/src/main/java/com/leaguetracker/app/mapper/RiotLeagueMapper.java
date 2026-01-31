package com.leaguetracker.app.mapper;

import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.dto.response.RiotLeagueResponse.RiotLeagueEntry;
import com.leaguetracker.app.model.Rank;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RiotLeagueMapper {

    RiotLeagueMapper INSTANCE = Mappers.getMapper(RiotLeagueMapper.class);

    RiotLeagueEntry toRiotLeagueEntry(Rank rank);

    default RiotLeagueResponse toResponse(List<Rank> ranks) {
        return RiotLeagueResponse.builder()
                .leagues(ranks.stream()
                        .map(this::toRiotLeagueEntry)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList())
                .build();
    }

    default Rank toRank(RiotLeagueEntry rank) {
        String currentRank = rank.tier() + " " + rank.rank() + " " + rank.leaguePoints();
        return Rank.builder()
                .currentRank(currentRank)
                .lowestRank("")
                .peakRank("")
                .puuid(rank.puuid())
                .wins(rank.wins())
                .losses(rank.losses())
                .freshBlood(rank.freshBlood())
                .hotStreak(rank.hotStreak())
                .inactive(rank.inactive())
                .queueType(rank.queueType())
                .build();
    }
    ;

    default List<Rank> toRanks(RiotLeagueResponse ranks) {
        return ranks.leagues().stream()
                .map(this::toRank)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
