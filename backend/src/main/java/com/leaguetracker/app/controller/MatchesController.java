package com.leaguetracker.app.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.leaguetracker.app.dto.response.RiotMatchResponse;
import com.leaguetracker.app.model.MatchDetails;
import com.leaguetracker.app.service.MatchService;
import com.leaguetracker.app.service.MatchService.MatchListMode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matches")
public class MatchesController {

    private final MatchService matchService;

    @GetMapping("/{matchId}/regions/{region}")
    public ResponseEntity<RiotMatchResponse> getMatch(
            @PathVariable
            @NotBlank(message = "matchId is required")
            @Size(max = 78, message = "matchId too long")
            @Pattern(regexp = "[a-zA-Z0-9_-]+") String matchId,
            @PathVariable
            @NotBlank(message = "region is required")
            @Size(max = 20, message = "region too long")
            @Pattern(regexp = "[a-zA-Z]+") String region) {
        return ResponseEntity.ok(matchService.getMatch(matchId, region));
    }

    @GetMapping("/{matchId}/ranks")
    public ResponseEntity<List<MatchDetails>> getSummonersRanks(
            @PathVariable
            @NotBlank(message = "matchId is required")
            @Size(max = 78, message = "matchId too long")
            @Pattern(regexp = "[a-zA-Z0-9_-]+", message = "matchId contains invalid characters")
            String matchId) {
        return ResponseEntity.ok(matchService.getSummonersRanks(matchId));
    }

    @GetMapping("/matchlist")
    public ResponseEntity<List<String>> updateMatchList(
            @RequestParam
            @NotBlank(message = "puuid is required")
            @Size(max = 78, message = "puuid too long")
            @Pattern(regexp = "[a-zA-Z0-9_-]+", message = "puuid contains invalid characters")
            String puuid,

            @RequestParam
            @NotBlank(message = "region is required")
            @Size(max = 20, message = "region too long")
            @Pattern(regexp = "[a-zA-Z]+", message = "region contains invalid characters")
            String region,

            @RequestParam
            @NonNull
            MatchListMode mode) {
        return ResponseEntity.ok(matchService.updateMatchList(puuid, region, mode));
    }
}