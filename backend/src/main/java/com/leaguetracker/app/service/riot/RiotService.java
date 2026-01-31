package com.leaguetracker.app.service.riot;

import com.leaguetracker.app.service.riot.endpoint.AccountEndpoint;
import com.leaguetracker.app.service.riot.endpoint.LeagueEndpoint;
import com.leaguetracker.app.service.riot.endpoint.MatchEndpoint;
import com.leaguetracker.app.service.riot.endpoint.SummonerEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiotService {

    public final AccountEndpoint Account;
    public final SummonerEndpoint Summoner;
    public final LeagueEndpoint League;
    public final MatchEndpoint Match;
}
