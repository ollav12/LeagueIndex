package com.leaguetracker.app.service;

import com.leaguetracker.app.dto.request.SummonerLookupRequest;
import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.mapper.RiotSummonerMapper;
import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.repository.SummonerRepository;
import com.leaguetracker.app.service.riot.RiotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummonerService {
    private final SummonerRepository summonerRepository;
    private final RiotService riotService;

    public RiotSummonerResponse getSummoner(String puuid, String name, String tag) {
        Summoner summoner = summonerRepository.findById(puuid).orElse(null);

        if (summoner == null) {
            log.info("Summoner not found in database, fetching from Riot API");
            return riotService.Summoner.findByPuuid(puuid, tag);
        }
        log.info("Retrieved summoner from database: {}", summoner.getSummonerName());
        return RiotSummonerMapper.INSTANCE.toRiotSummonerResponse(summoner);
    }

    public RiotSummonerResponse getSummoner(RiotAccountResponse account, SummonerLookupRequest request) {
        Summoner summoner = summonerRepository.findById(account.puuid()).orElse(null);

        if (summoner == null) {
            log.info("Summoner not found in database, fetching from Riot API");
            RiotSummonerResponse riotSummoner = riotService.Summoner.findByPuuid(account.puuid(), request.region());
            Summoner newSummoner = RiotSummonerMapper.INSTANCE.toSummoner(riotSummoner);
            newSummoner.setRegion(request.region());
            newSummoner.setSummonerName(request.summonerName());
            newSummoner.setTagLine(account.tagLine());
            saveSummoner(newSummoner);
            return riotSummoner;
        }
        log.info("Retrieved summoner from database: {}", summoner.getSummonerName());
        return RiotSummonerMapper.INSTANCE.toRiotSummonerResponse(summoner);
    }

    @Transactional(readOnly = true)
    public Summoner getSummoner(String puuid) {
        return summonerRepository.findById(puuid).orElse(null);
    }

    public RiotSummonerResponse updateSummoner(String puuid, String region) {
        RiotSummonerResponse riotSummoner = riotService.Summoner.findByPuuid(puuid, region);
        Summoner updatedSummoner = saveSummoner(riotSummoner);
        return RiotSummonerMapper.INSTANCE.toRiotSummonerResponse(updatedSummoner);
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }

    @Transactional
    public Summoner saveSummoner(RiotSummonerResponse riotSummoner) {
        Summoner existingSummoner =
                summonerRepository.findById(riotSummoner.puuid()).orElse(null);
        existingSummoner.setLastModifiedDate(null);
        Summoner newSummoner = RiotSummonerMapper.INSTANCE.map(existingSummoner, riotSummoner);
        log.info("Saving summoner to database: {}", newSummoner);
        return summonerRepository.save(newSummoner);
    }
}
