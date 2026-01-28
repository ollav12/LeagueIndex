package com.leaguetracker.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leaguetracker.app.model.Summoner;

@Repository
public interface SummonerRepository extends JpaRepository<Summoner, String> {
}