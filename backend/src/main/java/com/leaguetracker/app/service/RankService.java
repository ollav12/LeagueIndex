package com.leaguetracker.app.service;

import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.dto.response.RiotLeagueResponse.RiotLeagueEntry;
import com.leaguetracker.app.mapper.RiotLeagueMapper;
import com.leaguetracker.app.model.Rank;
import com.leaguetracker.app.repository.RankRepository;
import com.leaguetracker.app.service.riot.RiotService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankService {

    private final RankRepository rankRepository;
    private final RiotService riotService;

    public void saveLeagueDto(RiotLeagueResponse ranks) {
        for (RiotLeagueEntry rank : ranks.leagues()) {
            Rank newRank = RiotLeagueMapper.INSTANCE.toRank(rank);
            newRank.setSeason("2025 Season One");
            newRank.setLowestRank(newRank.getCurrentRank());
            newRank.setPeakRank(newRank.getCurrentRank());
            rankRepository.save(newRank);
        }
    }

    public Rank saveRank(Rank rank) {
        return rankRepository.save(rank);
    }

    public List<Rank> getRanks() {
        return rankRepository.findAll();
    }

    public List<Rank> getRankByPuuid(String puuid) {
        return rankRepository.findAllByPuuid(puuid);
    }

    public List<Rank> getRanked(String puuid, String region) {
        List<Rank> ranks = rankRepository.findAllByPuuid(puuid);

        if (ranks != null && !ranks.isEmpty()) {
            System.out.println("Retrieved ranked data from database: " + ranks.size() + " entries");
            return ranks;
        }

        RiotLeagueResponse leaguesFetched = riotService.League.findByPuuid(puuid, region);
        System.out.println("Fetched ranked data from Riot API entries");

        saveLeagueDto(leaguesFetched);
        return RiotLeagueMapper.INSTANCE.toRanks(leaguesFetched);
    }

    public RiotLeagueResponse fetchSummonerLeague(String puuid, String region) {
        return riotService.League.findByPuuid(puuid, region);
    }

    public List<Rank> updateRanked(String puuid, String region) {
        RiotLeagueResponse leagues = riotService.League.findByPuuid(puuid, region);
        saveLeagueDto(leagues);
        return RiotLeagueMapper.INSTANCE.toRanks(leagues);
    }
}
