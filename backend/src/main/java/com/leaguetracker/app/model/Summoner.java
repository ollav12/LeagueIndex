package com.leaguetracker.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "summoner")
@EntityListeners(AuditingEntityListener.class)
public class Summoner {

    @Id
    private String puuid;

    private String summonerName;
    private String tagLine;
    private String region;
    private int profileIconId;
    private int summonerLevel;
    private long revisionDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
