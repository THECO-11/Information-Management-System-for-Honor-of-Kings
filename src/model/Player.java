package model;

import java.util.ArrayList;
import java.util.List;
import enums.Role;


public class Player extends Person {

    private int level;
    private double winRate;
    private List<Hero> heroes = new ArrayList<>();
    private List<MatchRecord> matchHistory = new ArrayList<>();

    public Player() {
    }

    public Player(String id, String name, String username, String password, int level, double winRate) {
        super(id, name, username, password);
        this.level = level;
        this.winRate = winRate;
    }

    @Override
    public Role getRole() {
        return Role.PLAYER;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes == null ? new ArrayList<>() : heroes;
    }

    public List<MatchRecord> getMatchHistory() {
        return matchHistory;
    }

    public void setMatchHistory(List<MatchRecord> matchHistory) {
        this.matchHistory = matchHistory == null ? new ArrayList<>() : matchHistory;
    }

    public void addHero(Hero hero) {
        if (hero != null) {
            heroes.add(hero);
        }
    }

    public void addMatchRecord(MatchRecord matchRecord) {
        if (matchRecord != null) {
            matchHistory.add(matchRecord);
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", level=" + level +
                ", winRate=" + winRate +
                '}';
    }
}
