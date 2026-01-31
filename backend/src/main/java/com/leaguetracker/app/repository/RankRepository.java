package com.leaguetracker.app.repository;

import com.leaguetracker.app.model.Rank;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends JpaRepository<Rank, String> {
    List<Rank> findAllByPuuid(String puuid);
}
