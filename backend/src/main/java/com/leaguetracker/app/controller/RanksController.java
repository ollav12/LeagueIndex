package com.leaguetracker.app.controller;

import com.leaguetracker.app.service.RankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ranks")
public class RanksController {

    private final RankService rankService;

    @GetMapping("/{puuid}/region/{region}")
    public ResponseEntity<?> getRankByPuuid(@PathVariable String puuid, @PathVariable String region) {
        return ResponseEntity.ok(rankService.fetchSummonerLeague(puuid, region));
    }
}
