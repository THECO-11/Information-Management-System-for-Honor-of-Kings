package service;

import enums.EquipmentType;
import enums.HeroType;
import enums.MatchResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.Admin;
import model.Equipment;
import model.Hero;
import model.MatchRecord;
import model.Person;
import model.Player;
import model.Team;

public class FileStorageService {
    private static final List<String> REQUIRED_FILES = Arrays.asList(
            "admins.csv",
            "players.csv",
            "equipment.csv",
            "heroes.csv",
            "teams.csv",
            "matches.csv"
    );

    private final Path dataDirectory;

    public FileStorageService(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public void save(GameDataManager dataManager) {
        try {
            Files.createDirectories(dataDirectory);
            writeCsvFile("admins.csv", buildAdminLines(dataManager.getAdmins()));
            writeCsvFile("players.csv", buildPlayerLines(dataManager.getPlayers()));
            writeCsvFile("equipment.csv", buildEquipmentLines(dataManager.getEquipmentItems()));
            writeCsvFile("heroes.csv", buildHeroLines(dataManager.getHeroes()));
            writeCsvFile("teams.csv", buildTeamLines(dataManager.getTeams()));
            writeCsvFile("matches.csv", buildMatchLines(dataManager.getMatchRecords()));
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Failed to save data files.", exception);
        }
    }

    public GameDataManager load() {
        if (!Files.exists(dataDirectory)) {
            throw new IllegalStateException("Data directory does not exist: " + dataDirectory);
        }

        try {
            ensureRequiredFilesExist();
            GameDataManager dataManager = new GameDataManager();

            List<Admin> admins = loadAdmins();
            List<Equipment> equipmentItems = loadEquipment();
            Map<String, Equipment> equipmentById = toEquipmentMap(equipmentItems);

            HeroLoadResult heroLoadResult = loadHeroes(equipmentById);
            List<Hero> heroes = heroLoadResult.getHeroes();
            Map<String, Hero> heroById = toHeroMap(heroes);

            PlayerLoadResult playerLoadResult = loadPlayers(heroById);
            List<Player> players = playerLoadResult.getPlayers();
            Map<String, Player> playerById = toPlayerMap(players);

            List<Team> teams = loadTeams(playerById);
            List<MatchRecord> matchRecords = loadMatchRecords(playerById, heroById);

            populatePlayerMatchHistory(players, matchRecords, playerLoadResult.getPlayerMatchIds());

            List<Person> users = new ArrayList<>();
            users.addAll(admins);
            users.addAll(players);

            dataManager.setUsers(users);
            dataManager.setAdmins(admins);
            dataManager.setPlayers(players);
            dataManager.setHeroes(heroes);
            dataManager.setEquipmentItems(equipmentItems);
            dataManager.setTeams(teams);
            dataManager.setMatchRecords(matchRecords);

            return dataManager;
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Failed to load data files.", exception);
        }
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    private List<String> buildAdminLines(List<Admin> admins) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,username,password");
        for (Admin admin : admins) {
            lines.add(toCsv(admin.getId(), admin.getName(), admin.getUsername(), admin.getPassword()));
        }
        return lines;
    }

    private List<String> buildPlayerLines(List<Player> players) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,username,password,level,winRate,heroIds,matchIds");
        for (Player player : players) {
            lines.add(toCsv(
                    player.getId(),
                    player.getName(),
                    player.getUsername(),
                    player.getPassword(),
                    String.valueOf(player.getLevel()),
                    String.valueOf(player.getWinRate()),
                    joinIds(player.getHeroes().stream().map(Hero::getHeroId).collect(Collectors.toList())),
                    joinIds(player.getMatchHistory().stream().map(MatchRecord::getMatchId).collect(Collectors.toList()))
            ));
        }
        return lines;
    }

    private List<String> buildEquipmentLines(List<Equipment> equipmentItems) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,type,score");
        for (Equipment equipment : equipmentItems) {
            lines.add(toCsv(
                    equipment.getEquipmentId(),
                    equipment.getEquipmentName(),
                    equipment.getEquipmentType().name(),
                    String.valueOf(equipment.getScore())
            ));
        }
        return lines;
    }

    private List<String> buildHeroLines(List<Hero> heroes) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,type,attack,defense,equipmentIds");
        for (Hero hero : heroes) {
            lines.add(toCsv(
                    hero.getHeroId(),
                    hero.getHeroName(),
                    hero.getHeroType().name(),
                    String.valueOf(hero.getAttack()),
                    String.valueOf(hero.getDefense()),
                    joinIds(hero.getEquipmentList().stream().map(Equipment::getEquipmentId).collect(Collectors.toList()))
            ));
        }
        return lines;
    }

    private List<String> buildTeamLines(List<Team> teams) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,memberIds");
        for (Team team : teams) {
            lines.add(toCsv(
                    team.getTeamId(),
                    team.getTeamName(),
                    joinIds(team.getMembers().stream().map(Player::getId).collect(Collectors.toList()))
            ));
        }
        return lines;
    }

    private List<String> buildMatchLines(List<MatchRecord> matchRecords) {
        List<String> lines = new ArrayList<>();
        lines.add("id,date,opponentName,result,playerIds,heroIds");
        for (MatchRecord matchRecord : matchRecords) {
            lines.add(toCsv(
                    matchRecord.getMatchId(),
                    matchRecord.getDate() == null ? "" : matchRecord.getDate().toString(),
                    matchRecord.getOpponentName(),
                    matchRecord.getResult() == null ? "" : matchRecord.getResult().name(),
                    joinIds(matchRecord.getPlayers().stream().map(Player::getId).collect(Collectors.toList())),
                    joinIds(matchRecord.getHeroesUsed().stream().map(Hero::getHeroId).collect(Collectors.toList()))
            ));
        }
        return lines;
    }

    private List<Admin> loadAdmins() throws IOException {
        List<Admin> admins = new ArrayList<>();
        for (String line : readDataLines("admins.csv")) {
            List<String> columns = parseCsvLine(line);
            admins.add(new Admin(
                    getColumn(columns, 0),
                    getColumn(columns, 1),
                    getColumn(columns, 2),
                    getColumn(columns, 3)
            ));
        }
        return admins;
    }

    private List<Equipment> loadEquipment() throws IOException {
        List<Equipment> equipmentItems = new ArrayList<>();
        for (String line : readDataLines("equipment.csv")) {
            List<String> columns = parseCsvLine(line);
            Equipment equipment = new Equipment(
                    getColumn(columns, 0),
                    getColumn(columns, 1),
                    EquipmentType.valueOf(getColumn(columns, 2)),
                    parseDouble(getColumn(columns, 3))
            );
            equipmentItems.add(equipment);
        }
        return equipmentItems;
    }

    private HeroLoadResult loadHeroes(Map<String, Equipment> equipmentById) throws IOException {
        List<Hero> heroes = new ArrayList<>();
        for (String line : readDataLines("heroes.csv")) {
            List<String> columns = parseCsvLine(line);
            Hero hero = new Hero(
                    getColumn(columns, 0),
                    getColumn(columns, 1),
                    HeroType.valueOf(getColumn(columns, 2)),
                    parseInt(getColumn(columns, 3)),
                    parseInt(getColumn(columns, 4))
            );
            hero.setEquipmentList(resolveEquipmentList(parseIdList(getColumn(columns, 5)), equipmentById));
            heroes.add(hero);
        }
        return new HeroLoadResult(heroes);
    }

    private PlayerLoadResult loadPlayers(Map<String, Hero> heroById) throws IOException {
        List<Player> players = new ArrayList<>();
        Map<String, List<String>> playerMatchIds = new LinkedHashMap<>();

        for (String line : readDataLines("players.csv")) {
            List<String> columns = parseCsvLine(line);
            Player player = new Player(
                    getColumn(columns, 0),
                    getColumn(columns, 1),
                    getColumn(columns, 2),
                    getColumn(columns, 3),
                    parseInt(getColumn(columns, 4)),
                    parseDouble(getColumn(columns, 5))
            );
            player.setHeroes(resolveHeroList(parseIdList(getColumn(columns, 6)), heroById));
            player.setMatchHistory(new ArrayList<>());
            players.add(player);
            playerMatchIds.put(player.getId(), parseIdList(getColumn(columns, 7)));
        }

        return new PlayerLoadResult(players, playerMatchIds);
    }

    private List<Team> loadTeams(Map<String, Player> playerById) throws IOException {
        List<Team> teams = new ArrayList<>();
        for (String line : readDataLines("teams.csv")) {
            List<String> columns = parseCsvLine(line);
            Team team = new Team(getColumn(columns, 0), getColumn(columns, 1));
            team.setMembers(resolvePlayerList(parseIdList(getColumn(columns, 2)), playerById));
            teams.add(team);
        }
        return teams;
    }

    private List<MatchRecord> loadMatchRecords(Map<String, Player> playerById, Map<String, Hero> heroById) throws IOException {
        List<MatchRecord> matchRecords = new ArrayList<>();
        for (String line : readDataLines("matches.csv")) {
            List<String> columns = parseCsvLine(line);
            MatchRecord matchRecord = new MatchRecord(
                    getColumn(columns, 0),
                    parseDate(getColumn(columns, 1)),
                    getColumn(columns, 2),
                    MatchResult.valueOf(getColumn(columns, 3))
            );
            matchRecord.setPlayers(resolvePlayerList(parseIdList(getColumn(columns, 4)), playerById));
            matchRecord.setHeroesUsed(resolveHeroList(parseIdList(getColumn(columns, 5)), heroById));
            matchRecords.add(matchRecord);
        }
        return matchRecords;
    }

    private void populatePlayerMatchHistory(List<Player> players, List<MatchRecord> matchRecords, Map<String, List<String>> playerMatchIds) {
        Map<String, MatchRecord> matchById = matchRecords.stream()
                .collect(Collectors.toMap(MatchRecord::getMatchId, matchRecord -> matchRecord, (first, second) -> first, LinkedHashMap::new));

        for (Player player : players) {
            List<String> matchIds = playerMatchIds.get(player.getId());
            List<MatchRecord> history = new ArrayList<>();
            if (matchIds != null && !matchIds.isEmpty()) {
                for (String matchId : matchIds) {
                    MatchRecord matchRecord = matchById.get(matchId);
                    if (matchRecord != null) {
                        history.add(matchRecord);
                    }
                }
            } else {
                for (MatchRecord matchRecord : matchRecords) {
                    boolean involved = matchRecord.getPlayers().stream()
                            .anyMatch(matchPlayer -> equalsIgnoreCase(matchPlayer.getId(), player.getId()));
                    if (involved) {
                        history.add(matchRecord);
                    }
                }
            }
            player.setMatchHistory(history);
        }
    }

    private Map<String, Equipment> toEquipmentMap(List<Equipment> equipmentItems) {
        return equipmentItems.stream()
                .collect(Collectors.toMap(Equipment::getEquipmentId, equipment -> equipment, (first, second) -> first, LinkedHashMap::new));
    }

    private Map<String, Hero> toHeroMap(List<Hero> heroes) {
        return heroes.stream()
                .collect(Collectors.toMap(Hero::getHeroId, hero -> hero, (first, second) -> first, LinkedHashMap::new));
    }

    private Map<String, Player> toPlayerMap(List<Player> players) {
        return players.stream()
                .collect(Collectors.toMap(Player::getId, player -> player, (first, second) -> first, LinkedHashMap::new));
    }

    private List<Equipment> resolveEquipmentList(List<String> equipmentIds, Map<String, Equipment> equipmentById) {
        List<Equipment> equipmentList = new ArrayList<>();
        for (String equipmentId : equipmentIds) {
            Equipment equipment = equipmentById.get(equipmentId);
            if (equipment != null) {
                equipmentList.add(equipment);
            }
        }
        return equipmentList;
    }

    private List<Hero> resolveHeroList(List<String> heroIds, Map<String, Hero> heroById) {
        List<Hero> heroes = new ArrayList<>();
        for (String heroId : heroIds) {
            Hero hero = heroById.get(heroId);
            if (hero != null) {
                heroes.add(hero);
            }
        }
        return heroes;
    }

    private List<Player> resolvePlayerList(List<String> playerIds, Map<String, Player> playerById) {
        List<Player> players = new ArrayList<>();
        for (String playerId : playerIds) {
            Player player = playerById.get(playerId);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    private void writeCsvFile(String fileName, List<String> lines) throws IOException {
        Path targetPath = dataDirectory.resolve(fileName);
        Path temporaryPath = dataDirectory.resolve(fileName + ".tmp");
        Files.write(temporaryPath, lines);
        Files.move(temporaryPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private List<String> readDataLines(String fileName) throws IOException {
        Path filePath = dataDirectory.resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("Missing required data file: " + fileName);
        }

        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(lines.subList(1, lines.size()));
    }

    private String toCsv(String... values) {
        return Arrays.stream(values)
                .map(this::escapeCsv)
                .collect(Collectors.joining(","));
    }

    private String escapeCsv(String value) {
        String safeValue = value == null ? "" : value;
        String escapedValue = safeValue.replace("\"", "\"\"");
        if (escapedValue.contains(",") || escapedValue.contains("\"") || escapedValue.contains("\n")) {
            return "\"" + escapedValue + "\"";
        }
        return escapedValue;
    }

    private List<String> parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char currentChar = line.charAt(index);
            if (currentChar == '"') {
                if (inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    current.append('"');
                    index++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (currentChar == ',' && !inQuotes) {
                columns.add(current.toString());
                current.setLength(0);
            } else {
                current.append(currentChar);
            }
        }

        columns.add(current.toString());
        return columns;
    }

    private String getColumn(List<String> columns, int index) {
        return index < columns.size() ? columns.get(index) : "";
    }

    private List<String> parseIdList(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(value.split(";"))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .collect(Collectors.toList());
    }

    private String joinIds(List<String> ids) {
        return ids.stream()
                .filter(id -> id != null && !id.trim().isEmpty())
                .collect(Collectors.joining(";"));
    }

    private int parseInt(String value) {
        return Integer.parseInt(value.trim());
    }

    private double parseDouble(String value) {
        return Double.parseDouble(value.trim());
    }

    private LocalDate parseDate(String value) {
        return value == null || value.trim().isEmpty() ? null : LocalDate.parse(value.trim());
    }

    private boolean equalsIgnoreCase(String first, String second) {
        return first != null && second != null && first.equalsIgnoreCase(second);
    }

    private void ensureRequiredFilesExist() {
        List<String> missingFiles = new ArrayList<>();
        for (String fileName : REQUIRED_FILES) {
            if (!Files.exists(dataDirectory.resolve(fileName))) {
                missingFiles.add(fileName);
            }
        }

        if (!missingFiles.isEmpty()) {
            throw new IllegalStateException("Missing required data files: " + String.join(", ", missingFiles));
        }
    }

    private static class HeroLoadResult {
        private final List<Hero> heroes;

        HeroLoadResult(List<Hero> heroes) {
            this.heroes = heroes;
        }

        public List<Hero> getHeroes() {
            return heroes;
        }
    }

    private static class PlayerLoadResult {
        private final List<Player> players;
        private final Map<String, List<String>> playerMatchIds;

        PlayerLoadResult(List<Player> players, Map<String, List<String>> playerMatchIds) {
            this.players = players;
            this.playerMatchIds = playerMatchIds;
        }

        public List<Player> getPlayers() {
            return players;
        }

        public Map<String, List<String>> getPlayerMatchIds() {
            return playerMatchIds;
        }
    }
}
