package com.leaguetracker.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.response.RiotMatchListResponse;
import com.leaguetracker.app.dto.response.RiotMatchResponse;
import com.leaguetracker.app.mapper.RiotMatchMapper;
import com.leaguetracker.app.model.Match;
import com.leaguetracker.app.model.MatchDetails;
import com.leaguetracker.app.repository.MatchDetailsRepository;
import com.leaguetracker.app.repository.MatchRepository;
import com.leaguetracker.app.service.riot.RiotService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MatchDetailsRepository matchDetailsRepository;
    private final MatchRepository matchRepository;
    private final RiotService riotService;

    public enum MatchListMode {
        FIRST_LOAD,
        LIGHT
    }

    public RiotMatchResponse getMatch(String matchId, String region) {
        return riotService.Match.findByMatchId(matchId, region);
    }

    public List<String> getNextMatchIds(String puuid, String lastMatchId, int limit) {
        if (puuid == null) {
            System.out.println("PUUID cannot be null");
            return new ArrayList<>();
        }

        try {
            // Add count check before query
            long matchCount = matchRepository.count();
            System.out.println("Total matches in database: " + matchCount);

            if (lastMatchId != null) {
                System.out.println(
                        "Fetching matches for puuid: " + puuid + ", lastMatchId: " + lastMatchId + ", limit: " + limit);
                List<String> matchIds = matchRepository.getNextMatchIds(puuid, lastMatchId, PageRequest.of(0, limit));
                System.out.println("Found " + matchIds.size() + " matches after lastMatchId: " + lastMatchId);
                return matchIds;
            } else {
                System.out.println("Initial load for puuid: " + puuid + ", limit: " + limit);
                List<String> matchIds = matchRepository.findAllByPuuidOrderByMatchIdDesc(puuid, limit);
                System.out.println("Initial load found " + matchIds.size() + " matches");
                return matchIds;
            }
        } catch (Exception e) {
            System.out.println("Error fetching next match IDs for puuid: " + puuid);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<RiotMatchResponse> getMatches(String region, List<String> matchIds) {
        List<RiotMatchResponse> matches = new ArrayList<>();

        for (String matchId : matchIds) {
            MatchDetails existingMatch = matchDetailsRepository.findByMatchId(matchId);

            if (existingMatch != null) {

                RiotMatchResponse response = RiotMatchMapper.INSTANCE.toRiotMatchResponse(existingMatch);

                matches.add(response);
                System.out.println("Retrieved match from database: " + matchId);
            } else {
                fetchAndSaveMatch(matchId, region, matches);
            }
        }
        return matches;
    }

    public List<RiotMatchResponse> loadMoreMatches(String region, List<String> matchIds) {
        return getMatches(region, matchIds);
    }

    /** Helper method to fetch match from API and save to database */
    private void fetchAndSaveMatch(String matchId, String region, List<RiotMatchResponse> matches) {
        try {
            RiotMatchResponse matchDto = riotService.Match.findByMatchId(matchId, region);
            if (matchDto != null) {
                matches.add(matchDto);

                // Save to database for future requests
                MatchDetails matchEntity = new MatchDetails();
                matchEntity.setMatchId(matchId);

                // Extract metadata and info separately
                String metadataJson = objectMapper.writeValueAsString(matchDto.metadata());
                String infoJson = objectMapper.writeValueAsString(matchDto.info());

                matchEntity.setMetadataJson(metadataJson);
                matchEntity.setInfoJson(infoJson);

                matchDetailsRepository.save(matchEntity);
                System.out.println("Fetched and saved match to database: " + matchId);
            }
        } catch (Exception e) {
            System.err.println("Error fetching match from API: " + matchId + ", " + e.getMessage());
        }
    }

    public List<MatchDetails> getSummonersRanks(String matchId) {
        return null;
    }

    public void saveMatchList(List<String> matchIds, String puuid) {
        matchIds.stream()
                .filter(matchId -> matchRepository.findByMatchIdAndPuuid(matchId, puuid) == null)
                .map(matchId -> Match.builder().matchId(matchId).puuid(puuid).build())
                .forEach(matchRepository::save);
    }

    public List<Match> getMatchListByPuuid(String puuid) {
        return matchRepository.findByPuuid(puuid);
    }

    public List<String> updateMatchList(String puuid, String region, MatchListMode mode) {
        List<Match> matchListTemp = matchRepository.findByPuuid(puuid);
        List<String> matchList = new ArrayList<String>();

        if (!matchListTemp.isEmpty()
                && mode == MatchListMode.FIRST_LOAD) { // Don't load new matches if summoner matches are already present
            System.out.println("Match list already exists for puuid: " + puuid + ", skipping first load update.");
            for (Match match : matchListTemp) {
                matchList.add(match.getMatchId());
            }
            return matchList;
        }

        for (Match match : matchListTemp) {
            matchList.add(match.getMatchId());
        }

        int start = matchList.size();
        int count = 100;
        boolean shouldContinueFetching = true;

        while (shouldContinueFetching) {
            RiotMatchListResponse newMatchList = riotService.Match.findByPuuid(puuid, region, start, count);
            System.out.println("Fetching matchlist");
            if (newMatchList == null || newMatchList.matchIds().isEmpty()) {
                saveMatchList(matchList, puuid);
                return matchList;
            }

            matchList.addAll(newMatchList.matchIds());

            start += count;

            if (mode == MatchListMode.LIGHT || mode == MatchListMode.FIRST_LOAD) {
                shouldContinueFetching = false;
            }
        }
        System.out.println("Saving matchlist");
        saveMatchList(matchList, puuid);
        return matchList;
    }

    public void saveMatches(List<String> matchList, String puuid) {
        for (String match : matchList) {
            Match newMatch = Match.builder().puuid(puuid).matchId(match).build();
            matchRepository.save(newMatch);
        }
    }
}
