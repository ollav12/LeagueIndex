package com.leaguetracker.app.service;

import com.leaguetracker.app.dto.request.SummonerLookupRequest;
import com.leaguetracker.app.dto.request.SummonerMatchesRequest;
import com.leaguetracker.app.dto.request.SummonerUpdateRequest;
import com.leaguetracker.app.dto.response.*;
import com.leaguetracker.app.exception.SummonerRecentlyUpdatedException;
import com.leaguetracker.app.mapper.RiotSummonerMapper;
import com.leaguetracker.app.model.Rank;
import com.leaguetracker.app.model.Summoner;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummonerInfoService {

    private final SummonerService summonerService;
    private final MatchService matchService;
    private final AccountService accountService;
    private final RankService rankService;
    private final StatsService statsService;

    public SummonerLookupResponse getSummonerDetails(SummonerLookupRequest request) {
        RiotAccountResponse account =
                accountService.getAccount(request.region(), request.summonerName(), request.tag());
        RiotSummonerResponse summoner = summonerService.getSummoner(account, request);
        List<Rank> league = rankService.getRanked(summoner.puuid(), request.region());

        // Fetch first 100 matches
        matchService.updateMatchList(account.puuid(), request.region(), MatchService.MatchListMode.FIRST_LOAD);

        return RiotSummonerMapper.INSTANCE.toSummonerLookupResponse(account, summoner, league);
    }

    public SummonerMatchesResponse getMatchHistory(SummonerMatchesRequest request) {

        List<String> matchIds = matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit());
        List<RiotMatchResponse> matchDetails = matchService.getMatches(request.region(), matchIds);
        Map<String, Object> stats = statsService.getSummonerStats(request.puuid());

        return SummonerMatchesResponse.builder()
                .matchIds(matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit()))
                .matchDetails(matchDetails)
                .stats(stats)
                .build();
    }

    public SummonerMatchesResponse loadMoreMatches(SummonerMatchesRequest request) {
        List<String> matchIds = matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit());

        if (matchIds.isEmpty()) {
            matchService.updateMatchList(request.puuid(), request.region(), MatchService.MatchListMode.LIGHT);
            matchIds = matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit());
        }

        List<RiotMatchResponse> matchDetails = matchService.getMatches(request.region(), matchIds);
        Map<String, Object> stats = statsService.getSummonerStats(request.puuid());

        return SummonerMatchesResponse.builder()
                .matchIds(matchIds)
                .matchDetails(matchDetails)
                .stats(stats)
                .build();
    }

    public SummonerUpdateResponse updateSummoner(SummonerUpdateRequest request) {
        Summoner oldSummoner = summonerService.getSummoner(request.puuid());
        LocalDateTime lastModified = oldSummoner.getLastModifiedDate();
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);

        if (!lastModified.isBefore(threshold)) {
            throw new SummonerRecentlyUpdatedException("Summoner with puuid "
                    + request.puuid()
                    + " was updated recently. Please wait before trying again.");
        }
        RiotAccountResponse account = RiotAccountResponse.builder()
                .gameName(request.summonerName())
                .puuid(request.puuid())
                .tagLine(request.tag())
                .build();

        RiotSummonerResponse summoner = summonerService.updateSummoner(request.puuid(), request.region());
        List<Rank> league = rankService.updateRanked(summoner.puuid(), request.region());

        // Fetch first 100 matches
        matchService.updateMatchList(account.puuid(), request.region(), MatchService.MatchListMode.LIGHT);

        SummonerLookupResponse lookup = RiotSummonerMapper.INSTANCE.toSummonerLookupResponse(account, summoner, league);

        List<String> matchIds = matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit());
        List<RiotMatchResponse> matchDetails = matchService.getMatches(request.region(), matchIds);
        Map<String, Object> stats = statsService.getSummonerStats(request.puuid());

        SummonerMatchesResponse matchHistory = SummonerMatchesResponse.builder()
                .matchIds(matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit()))
                .matchDetails(matchDetails)
                .stats(stats)
                .build();

        return SummonerUpdateResponse.builder()
                .summonerLookupResponse(lookup)
                .summonerMatchesResponse(matchHistory)
                .build();
    }
}
