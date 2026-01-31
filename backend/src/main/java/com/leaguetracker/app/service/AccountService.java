package com.leaguetracker.app.service;

import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.service.riot.RiotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final RiotService riotService;

    @Cacheable(value = "accounts", key = "'account:' + #region + ':' + #summonerName + ':' + #tag")
    public RiotAccountResponse getAccount(String region, String summonerName, String tag) {
        log.info("Account not in cache, fetching from Riot API: {}/{}#{}", region, summonerName, tag);
        return riotService.Account.findByRiotId(region, summonerName, tag);
    }
}
