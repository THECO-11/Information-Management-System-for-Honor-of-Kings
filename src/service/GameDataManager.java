package service;

import java.util.ArrayList;
import java.util.List;
import model.Admin;
import model.Equipment;
import model.Hero;
import model.MatchRecord;
import model.Person;
import model.Player;
import model.Team;

public class GameDataManager {
    private List<Person> users = new ArrayList<>();
    private List<Admin> admins = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<Hero> heroes = new ArrayList<>();
    private List<Equipment> equipmentItems = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();
    private List<MatchRecord> matchRecords = new ArrayList<>();

    public List<Person> getUsers() {
        return users;
    }

    public void setUsers(List<Person> users) {
        this.users = users;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public List<Equipment> getEquipmentItems() {
        return equipmentItems;
    }

    public void setEquipmentItems(List<Equipment> equipmentItems) {
        this.equipmentItems = equipmentItems;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<MatchRecord> getMatchRecords() {
        return matchRecords;
    }

    public void setMatchRecords(List<MatchRecord> matchRecords) {
        this.matchRecords = matchRecords;
    }

    public void addPlayer(Player player) {
        throw new UnsupportedOperationException("Player creation is not implemented yet.");
    }

    public void deletePlayer(String playerId) {
        throw new UnsupportedOperationException("Player deletion is not implemented yet.");
    }

    public void addHero(Hero hero) {
        throw new UnsupportedOperationException("Hero creation is not implemented yet.");
    }

    public void addEquipment(Equipment equipment) {
        throw new UnsupportedOperationException("Equipment creation is not implemented yet.");
    }

    public void addTeam(Team team) {
        throw new UnsupportedOperationException("Team creation is not implemented yet.");
    }

    public void addMatchRecord(MatchRecord matchRecord) {
        throw new UnsupportedOperationException("Match record creation is not implemented yet.");
    }
}
