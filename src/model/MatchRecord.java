package model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import enums.MatchResult;


public class MatchRecord {
    private String matchId;
    private LocalDate date;
    private String opponentName;
    private List<Player> players = new ArrayList<>();
    private List<Hero> heroesUsed = new ArrayList<>();
    private MatchResult result;

    public MatchRecord() {
    }

    public MatchRecord(String matchId, LocalDate date, String opponentName, MatchResult result) {
        this.matchId = matchId;
        this.date = date;
        this.opponentName = opponentName;
        this.result = result;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players == null ? new ArrayList<>() : players;
    }

    public List<Hero> getHeroesUsed() {
        return heroesUsed;
    }

    public void setHeroesUsed(List<Hero> heroesUsed) {
        this.heroesUsed = heroesUsed == null ? new ArrayList<>() : heroesUsed;
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public void addPlayer(Player player) {
        if (player != null) {
            players.add(player);
        }
    }

    public void addHeroUsed(Hero hero) {
        if (hero != null) {
            heroesUsed.add(hero);
        }
    }

    @Override
    public String toString() {
        return "MatchRecord{" +
                "matchId='" + matchId + '\'' +
                ", date=" + date +
                ", opponentName='" + opponentName + '\'' +
                ", result=" + result +
                '}';
    }
}
