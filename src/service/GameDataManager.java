package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        this.users = users == null ? new ArrayList<>() : users;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins == null ? new ArrayList<>() : admins;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players == null ? new ArrayList<>() : players;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes == null ? new ArrayList<>() : heroes;
    }

    public List<Equipment> getEquipmentItems() {
        return equipmentItems;
    }

    public void setEquipmentItems(List<Equipment> equipmentItems) {
        this.equipmentItems = equipmentItems == null ? new ArrayList<>() : equipmentItems;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams == null ? new ArrayList<>() : teams;
    }

    public List<MatchRecord> getMatchRecords() {
        return matchRecords;
    }

    public void setMatchRecords(List<MatchRecord> matchRecords) {
        this.matchRecords = matchRecords == null ? new ArrayList<>() : matchRecords;
    }

    public void addPlayer(Player player) {
        requireNotNull(player, "player");
        requireUniquePersonId(player.getId());
        players.add(player);
        users.add(player);
    }

    public void deletePlayer(String playerId) {
        Player player = getPlayerById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        players.remove(player);
        users.remove(player);
        for (Team team : teams) {
            team.getMembers().remove(player);
        }
        for (MatchRecord matchRecord : matchRecords) {
            matchRecord.getPlayers().remove(player);
        }
    }

    public void addHero(Hero hero) {
        requireNotNull(hero, "hero");
        requireUniqueHeroId(hero.getHeroId());
        heroes.add(hero);
    }

    public void addEquipment(Equipment equipment) {
        requireNotNull(equipment, "equipment");
        requireUniqueEquipmentId(equipment.getEquipmentId());
        equipmentItems.add(equipment);
    }

    public void addTeam(Team team) {
        requireNotNull(team, "team");
        requireUniqueTeamId(team.getTeamId());
        teams.add(team);
    }

    public void addMatchRecord(MatchRecord matchRecord) {
        requireNotNull(matchRecord, "matchRecord");
        requireUniqueMatchId(matchRecord.getMatchId());
        matchRecords.add(matchRecord);
        for (Player player : matchRecord.getPlayers()) {
            if (!player.getMatchHistory().contains(matchRecord)) {
                player.addMatchRecord(matchRecord);
            }
        }
    }

    public void addAdmin(Admin admin) {
        requireNotNull(admin, "admin");
        requireUniquePersonId(admin.getId());
        admins.add(admin);
        users.add(admin);
    }

    public Optional<Person> getUserById(String userId) {
        return users.stream()
                .filter(user -> equalsIgnoreCase(user.getId(), userId))
                .findFirst();
    }

    public Optional<Person> getUserByUsername(String username) {
        return users.stream()
                .filter(user -> equalsIgnoreCase(user.getUsername(), username))
                .findFirst();
    }

    public Optional<Player> getPlayerById(String playerId) {
        return players.stream()
                .filter(player -> equalsIgnoreCase(player.getId(), playerId))
                .findFirst();
    }

    public Optional<Admin> getAdminById(String adminId) {
        return admins.stream()
                .filter(admin -> equalsIgnoreCase(admin.getId(), adminId))
                .findFirst();
    }

    public Optional<Hero> getHeroById(String heroId) {
        return heroes.stream()
                .filter(hero -> equalsIgnoreCase(hero.getHeroId(), heroId))
                .findFirst();
    }

    public Optional<Equipment> getEquipmentById(String equipmentId) {
        return equipmentItems.stream()
                .filter(equipment -> equalsIgnoreCase(equipment.getEquipmentId(), equipmentId))
                .findFirst();
    }

    public Optional<Team> getTeamById(String teamId) {
        return teams.stream()
                .filter(team -> equalsIgnoreCase(team.getTeamId(), teamId))
                .findFirst();
    }

    public Optional<MatchRecord> getMatchRecordById(String matchId) {
        return matchRecords.stream()
                .filter(matchRecord -> equalsIgnoreCase(matchRecord.getMatchId(), matchId))
                .findFirst();
    }

    public Optional<Team> getTeamByPlayerId(String playerId) {
        return teams.stream()
                .filter(team -> team.getMembers().stream()
                        .anyMatch(player -> equalsIgnoreCase(player.getId(), playerId)))
                .findFirst();
    }

    public void updatePlayer(Player updatedPlayer) {
        requireNotNull(updatedPlayer, "updatedPlayer");
        Player existingPlayer = getPlayerById(updatedPlayer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + updatedPlayer.getId()));
        replaceById(players, updatedPlayer, updatedPlayer.getId(), "Player not found: ");
        replaceUser(updatedPlayer);
        replacePlayerReferences(existingPlayer, updatedPlayer);
    }

    public void updateAdmin(Admin updatedAdmin) {
        requireNotNull(updatedAdmin, "updatedAdmin");
        replaceById(admins, updatedAdmin, updatedAdmin.getId(), "Admin not found: ");
        replaceUser(updatedAdmin);
    }

    public void updateHero(Hero updatedHero) {
        requireNotNull(updatedHero, "updatedHero");
        Hero existingHero = getHeroById(updatedHero.getHeroId())
                .orElseThrow(() -> new IllegalArgumentException("Hero not found: " + updatedHero.getHeroId()));
        replaceHero(updatedHero);
        replaceHeroReferences(existingHero, updatedHero);
    }

    public void updateEquipment(Equipment updatedEquipment) {
        requireNotNull(updatedEquipment, "updatedEquipment");
        Equipment existingEquipment = getEquipmentById(updatedEquipment.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found: " + updatedEquipment.getEquipmentId()));
        replaceEquipment(updatedEquipment);
        replaceEquipmentReferences(existingEquipment, updatedEquipment);
    }

    public void updateTeam(Team updatedTeam) {
        requireNotNull(updatedTeam, "updatedTeam");
        replaceTeam(updatedTeam);
    }

    public void updateMatchRecord(MatchRecord updatedMatchRecord) {
        requireNotNull(updatedMatchRecord, "updatedMatchRecord");
        MatchRecord existingMatchRecord = getMatchRecordById(updatedMatchRecord.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Match record not found: " + updatedMatchRecord.getMatchId()));
        replaceMatchRecord(updatedMatchRecord);
        replaceMatchRecordReferences(existingMatchRecord, updatedMatchRecord);
    }

    public void deleteAdmin(String adminId) {
        Admin admin = getAdminById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found: " + adminId));
        admins.remove(admin);
        users.remove(admin);
    }

    public void deleteHero(String heroId) {
        Hero hero = getHeroById(heroId)
                .orElseThrow(() -> new IllegalArgumentException("Hero not found: " + heroId));

        heroes.remove(hero);
        for (Player player : players) {
            player.getHeroes().remove(hero);
        }
        for (MatchRecord matchRecord : matchRecords) {
            matchRecord.getHeroesUsed().remove(hero);
        }
    }

    public void deleteEquipment(String equipmentId) {
        Equipment equipment = getEquipmentById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found: " + equipmentId));

        equipmentItems.remove(equipment);
        for (Hero hero : heroes) {
            hero.getEquipmentList().remove(equipment);
        }
    }

    public void deleteTeam(String teamId) {
        Team team = getTeamById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));
        teams.remove(team);
    }

    public void deleteMatchRecord(String matchId) {
        MatchRecord matchRecord = getMatchRecordById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match record not found: " + matchId));

        matchRecords.remove(matchRecord);
        for (Player player : players) {
            player.getMatchHistory().remove(matchRecord);
        }
    }

    private void replaceUser(Person updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (equalsIgnoreCase(users.get(i).getId(), updatedUser.getId())) {
                users.set(i, updatedUser);
                return;
            }
        }
        users.add(updatedUser);
    }

    private void replacePlayerReferences(Player existingPlayer, Player updatedPlayer) {
        for (Team team : teams) {
            replaceListValue(team.getMembers(), existingPlayer, updatedPlayer);
        }
        for (MatchRecord matchRecord : matchRecords) {
            replaceListValue(matchRecord.getPlayers(), existingPlayer, updatedPlayer);
        }
    }

    private void replaceHeroReferences(Hero existingHero, Hero updatedHero) {
        for (Player player : players) {
            replaceListValue(player.getHeroes(), existingHero, updatedHero);
        }
        for (MatchRecord matchRecord : matchRecords) {
            replaceListValue(matchRecord.getHeroesUsed(), existingHero, updatedHero);
        }
    }

    private void replaceEquipmentReferences(Equipment existingEquipment, Equipment updatedEquipment) {
        for (Hero hero : heroes) {
            replaceListValue(hero.getEquipmentList(), existingEquipment, updatedEquipment);
        }
    }

    private void replaceMatchRecordReferences(MatchRecord existingMatchRecord, MatchRecord updatedMatchRecord) {
        for (Player player : players) {
            replaceListValue(player.getMatchHistory(), existingMatchRecord, updatedMatchRecord);
        }
    }

    private void replaceHero(Hero updatedHero) {
        for (int i = 0; i < heroes.size(); i++) {
            if (equalsIgnoreCase(heroes.get(i).getHeroId(), updatedHero.getHeroId())) {
                heroes.set(i, updatedHero);
                return;
            }
        }
        throw new IllegalArgumentException("Hero not found: " + updatedHero.getHeroId());
    }

    private void replaceEquipment(Equipment updatedEquipment) {
        for (int i = 0; i < equipmentItems.size(); i++) {
            if (equalsIgnoreCase(equipmentItems.get(i).getEquipmentId(), updatedEquipment.getEquipmentId())) {
                equipmentItems.set(i, updatedEquipment);
                return;
            }
        }
        throw new IllegalArgumentException("Equipment not found: " + updatedEquipment.getEquipmentId());
    }

    private void replaceTeam(Team updatedTeam) {
        for (int i = 0; i < teams.size(); i++) {
            if (equalsIgnoreCase(teams.get(i).getTeamId(), updatedTeam.getTeamId())) {
                teams.set(i, updatedTeam);
                return;
            }
        }
        throw new IllegalArgumentException("Team not found: " + updatedTeam.getTeamId());
    }

    private void replaceMatchRecord(MatchRecord updatedMatchRecord) {
        for (int i = 0; i < matchRecords.size(); i++) {
            if (equalsIgnoreCase(matchRecords.get(i).getMatchId(), updatedMatchRecord.getMatchId())) {
                matchRecords.set(i, updatedMatchRecord);
                return;
            }
        }
        throw new IllegalArgumentException("Match record not found: " + updatedMatchRecord.getMatchId());
    }

    private <T extends Person> void replaceById(List<T> targetList, T updatedValue, String id, String errorPrefix) {
        for (int i = 0; i < targetList.size(); i++) {
            T current = targetList.get(i);
            if (equalsIgnoreCase(current.getId(), id)) {
                targetList.set(i, updatedValue);
                return;
            }
        }
        throw new IllegalArgumentException(errorPrefix + id);
    }

    private void requireUniquePersonId(String id) {
        if (isBlank(id)) {
            throw new IllegalArgumentException("Person id cannot be blank.");
        }
        if (getUserById(id).isPresent()) {
            throw new IllegalArgumentException("Duplicate person id: " + id);
        }
    }

    private void requireUniqueHeroId(String id) {
        if (isBlank(id)) {
            throw new IllegalArgumentException("Hero id cannot be blank.");
        }
        if (getHeroById(id).isPresent()) {
            throw new IllegalArgumentException("Duplicate hero id: " + id);
        }
    }

    private void requireUniqueEquipmentId(String id) {
        if (isBlank(id)) {
            throw new IllegalArgumentException("Equipment id cannot be blank.");
        }
        if (getEquipmentById(id).isPresent()) {
            throw new IllegalArgumentException("Duplicate equipment id: " + id);
        }
    }

    private void requireUniqueTeamId(String id) {
        if (isBlank(id)) {
            throw new IllegalArgumentException("Team id cannot be blank.");
        }
        if (getTeamById(id).isPresent()) {
            throw new IllegalArgumentException("Duplicate team id: " + id);
        }
    }

    private void requireUniqueMatchId(String id) {
        if (isBlank(id)) {
            throw new IllegalArgumentException("Match record id cannot be blank.");
        }
        if (getMatchRecordById(id).isPresent()) {
            throw new IllegalArgumentException("Duplicate match record id: " + id);
        }
    }

    private void requireNotNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be null.");
        }
    }

    private boolean equalsIgnoreCase(String first, String second) {
        return first != null && second != null && first.equalsIgnoreCase(second);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private <T> void replaceListValue(List<T> list, T existingValue, T updatedValue) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == existingValue) {
                list.set(i, updatedValue);
            }
        }
    }
}
