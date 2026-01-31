package com.leaguetracker.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RiotAccountMapper {

    RiotAccountMapper INSTANCE = Mappers.getMapper(RiotAccountMapper.class);

    // iotAccountResponse toRiotAccountResponse(Account accoont)
}
