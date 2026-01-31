package com.leaguetracker.app.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.response.RiotMatchResponse;
import com.leaguetracker.app.dto.response.RiotMatchResponse.InfoDto;
import com.leaguetracker.app.dto.response.RiotMatchResponse.MetadataDto;
import com.leaguetracker.app.model.MatchDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RiotMatchMapper {

    RiotMatchMapper INSTANCE = Mappers.getMapper(RiotMatchMapper.class);

    @Mapping(target = "info", source = "infoJson", qualifiedByName = "jsonToInfoDto")
    @Mapping(target = "metadata", source = "metadataJson", qualifiedByName = "jsonToMetadataDto")
    RiotMatchResponse toRiotMatchResponse(MatchDetails match);

    @Named("jsonToInfoDto")
    default InfoDto jsonToInfoDto(String infoJson) {
        if (infoJson == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(infoJson, InfoDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize infoJson to InfoDto", e);
        }
    }

    @Named("jsonToMetadataDto")
    default MetadataDto jsonToMetadataDto(String metadataJson) {
        if (metadataJson == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(metadataJson, MetadataDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize metadataJson to MetadataDto", e);
        }
    }
}
