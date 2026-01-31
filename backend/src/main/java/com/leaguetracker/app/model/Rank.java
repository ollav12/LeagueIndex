package com.leaguetracker.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "rank",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"puuid", "queueType", "season"})})
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String puuid;
    private String queueType;
    private String currentRank;
    private String peakRank;
    private String lowestRank;
    private String season;
    private int wins;
    private int losses;
    private boolean veteran;
    private boolean inactive;
    private boolean freshBlood;
    private boolean hotStreak;
}
