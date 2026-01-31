package com.leaguetracker.app.exception;

public class SummonerNotFoundException extends RuntimeException {
    public SummonerNotFoundException(String message) {
        super(message);
    }

    public SummonerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
