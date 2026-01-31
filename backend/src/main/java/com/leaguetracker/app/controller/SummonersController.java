package com.leaguetracker.app.controller;

import com.leaguetracker.app.dto.request.SummonerLookupRequest;
import com.leaguetracker.app.dto.request.SummonerMatchesRequest;
import com.leaguetracker.app.dto.request.SummonerUpdateRequest;
import com.leaguetracker.app.dto.response.SummonerLookupResponse;
import com.leaguetracker.app.dto.response.SummonerMatchesResponse;
import com.leaguetracker.app.dto.response.SummonerUpdateResponse;
import com.leaguetracker.app.service.SummonerInfoService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summoners")
public class SummonersController {

    private final SummonerInfoService summonerInfoService;

    @GetMapping("/{region}/{summonerName}-{tag}")
    public ResponseEntity<SummonerLookupResponse> getSummoner(
            @NonNull @PathVariable String region,
            @NonNull @PathVariable String summonerName,
            @NonNull @PathVariable String tag) {

        SummonerLookupRequest request = SummonerLookupRequest.builder()
                .region(region)
                .summonerName(summonerName)
                .tag(tag)
                .build();
        SummonerLookupResponse summoner = summonerInfoService.getSummonerDetails(request);
        return ResponseEntity.ok(summoner);
    }

    @GetMapping("/{puuid}/match-history")
    public ResponseEntity<SummonerMatchesResponse> getMatchHistory(@Valid SummonerMatchesRequest request) {
        return ResponseEntity.ok(summonerInfoService.getMatchHistory(request));
    }

    @GetMapping("/{puuid}/match-history/load-more")
    public ResponseEntity<SummonerMatchesResponse> loadMoreMatches(@Valid SummonerMatchesRequest request) {
        return ResponseEntity.ok(summonerInfoService.loadMoreMatches(request));
    }

    @GetMapping("/{puuid}/update")
    public ResponseEntity<SummonerUpdateResponse> updateSummoner(@Valid SummonerUpdateRequest request) {
        return ResponseEntity.ok(summonerInfoService.updateSummoner(request));
    }
}
