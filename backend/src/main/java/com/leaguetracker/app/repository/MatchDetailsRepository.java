package com.leaguetracker.app.repository;

import com.leaguetracker.app.model.MatchDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchDetailsRepository extends JpaRepository<MatchDetails, Long> {
    MatchDetails findByMatchId(String matchId);
}
