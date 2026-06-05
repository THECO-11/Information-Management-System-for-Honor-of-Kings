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
        this.players = players;
    }

    public List<Hero> getHeroesUsed() {
        return heroesUsed;
    }

    public void setHeroesUsed(List<Hero> heroesUsed) {
        this.heroesUsed = heroesUsed;
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }
}
