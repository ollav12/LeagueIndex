package com.leaguetracker.app.helper;

import lombok.Getter;

public class Helper {

    public Helper() {}

    @Getter
    public enum LeagueRegion {
        BRAZIL("br1"),
        EUROPE_NORTHEAST("eun1"),
        EUROPE_WEST("euw1"),
        KOREA("kr"),
        LATIN_AMERICA_NORTH("la1"),
        LATIN_AMERICA_SOUTH("la2"),
        NORTH_AMERICA("na1"),
        OCEANIA("oc1"),
        RUSSIA("ru"),
        TURKEY("tr1"),
        JAPAN("jp1"),
        PHILIPPINES("ph2"),
        SINGAPORE("sg2"),
        THAILAND("th2"),
        TAIWAN("tw2"),
        VIETNAM("vn2");

        private final String regionCode;

        LeagueRegion(String regionCode) {
            this.regionCode = regionCode;
        }

        // Helper method to find a LeagueRegion by its region code
        public static LeagueRegion fromRegionCode(String regionCode) {
            for (LeagueRegion region : LeagueRegion.values()) {
                if (region.getRegionCode().equalsIgnoreCase(regionCode)) {
                    return region;
                }
            }
            throw new IllegalArgumentException("Unknown region code: " + regionCode);
        }
    }

    @Getter
    public enum RiotRegion {
        AMERICAS("americas"),
        ASIA("asia"),
        EUROPE("europe"),
        SEA("sea");

        private final String regionName;

        RiotRegion(String regionName) {
            this.regionName = regionName;
        }
    }

    /**
     * Get Riot API region from a League region code (e.g., "euw1" -> "europe")
     *
     * @param regionCode The League region code (e.g., "euw1", "na1")
     * @return The corresponding Riot API region (e.g., "europe", "americas")
     */
    public static String getRiotApiRegion(String regionCode) {
        // Convert region code to LeagueRegion enum
        LeagueRegion leagueRegion = LeagueRegion.fromRegionCode(regionCode);

        // Map LeagueRegion to RiotRegion
        return switch (leagueRegion) {
            case NORTH_AMERICA, BRAZIL, LATIN_AMERICA_NORTH, LATIN_AMERICA_SOUTH -> RiotRegion.AMERICAS.getRegionName();
            case OCEANIA, PHILIPPINES, SINGAPORE, THAILAND, TAIWAN, VIETNAM -> RiotRegion.SEA.getRegionName();
            case KOREA, JAPAN -> RiotRegion.ASIA.getRegionName();
            case EUROPE_NORTHEAST, EUROPE_WEST, TURKEY, RUSSIA -> RiotRegion.EUROPE.getRegionName();
            default -> throw new IllegalArgumentException("Unknown LeagueRegion: " + leagueRegion);
        };
    }
}
