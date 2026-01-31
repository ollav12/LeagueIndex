package com.leaguetracker.app.mapper;

import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.dto.response.SummonerLookupResponse;
import com.leaguetracker.app.model.Rank;
import com.leaguetracker.app.model.Summoner;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RiotSummonerMapper {

    RiotSummonerMapper INSTANCE = Mappers.getMapper(RiotSummonerMapper.class);

    @Mapping(target = "puuid", source = "account.puuid")
    @Mapping(target = "ranked", source = "ranks")
    SummonerLookupResponse toSummonerLookupResponse(
            RiotAccountResponse account, RiotSummonerResponse response, List<Rank> ranks);

    RiotSummonerResponse toRiotSummonerResponse(Summoner summoner);

    Summoner toSummoner(RiotSummonerResponse summoner);

    @Mapping(target = "puuid", source = "puuid")
    @Mapping(target = "profileIconId", source = "profileIconId")
    @Mapping(target = "revisionDate", source = "revisionDate")
    @Mapping(target = "summonerLevel", source = "summonerLevel")
    @Mapping(target = "summonerName", ignore = true)
    @Mapping(target = "tagLine", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    // Managed by JPA
    Summoner map(@MappingTarget Summoner existingSummoner, RiotSummonerResponse riotSummoner);
}
