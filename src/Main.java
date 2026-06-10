import enums.EquipmentType;
import enums.HeroType;
import enums.MatchResult;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import model.Equipment;
import model.Hero;
import model.MatchRecord;
import model.Person;
import model.Player;
import model.Team;
import service.AuthenticationService;
import service.FileStorageService;
import service.GameDataManager;
import service.RankingService;
import service.SearchService;
import util.DataInitializer;
import util.InputHelper;

public class Main {
    private final GameDataManager dataManager;
    private final SearchService searchService;
    private final RankingService rankingService;
    private final AuthenticationService authenticationService;
    private final FileStorageService fileStorageService;
    private final InputHelper inputHelper;

    public Main() {
        this.dataManager = new DataInitializer().createInitialData();
        this.searchService = new SearchService(dataManager);
        this.rankingService = new RankingService(dataManager);
        this.authenticationService = new AuthenticationService(dataManager);
        this.fileStorageService = new FileStorageService(Paths.get("out", "data"));
        this.inputHelper = new InputHelper(new Scanner(System.in));
    }

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        System.out.println("Honor of Kings Information Management System");
        System.out.println("Default admin account: admin / admin123");
        System.out.println("Example player account: ming / p123");

        boolean running = true;
        while (running) {
            printWelcomeMenu();
            int choice = inputHelper.readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleSearchHero();
                    break;
                case 3:
                    handleSearchTeam();
                    break;
                case 4:
                    handlePlayerLeaderboard();
                    break;
                case 5:
                    handleEquipmentRanking();
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void printWelcomeMenu() {
        System.out.println();
        System.out.println("=== Main Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Search Hero");
        System.out.println("3. Search Team");
        System.out.println("4. View Player Leaderboard");
        System.out.println("5. View Equipment Ranking");
        System.out.println("0. Exit");
    }

    private void handleLogin() {
        String username = inputHelper.readText("Username: ");
        String password = inputHelper.readText("Password: ");
        Person user = authenticationService.login(username, password).orElse(null);
        if (user == null) {
            System.out.println("Login failed. Please check your username and password.");
            return;
        }

        System.out.println("Login successful. Welcome, " + user.getName() + ".");
        if (authenticationService.isCurrentUserAdmin()) {
            runAdminMenu();
        } else {
            runPlayerMenu();
        }
    }

    private void runAdminMenu() {
        boolean inMenu = true;
        while (inMenu && authenticationService.isLoggedIn()) {
            System.out.println();
            System.out.println("=== Admin Menu ===");
            System.out.println("1. Search Player");
            System.out.println("2. Search Team");
            System.out.println("3. Search Hero");
            System.out.println("4. View Player Leaderboard");
            System.out.println("5. View Equipment Ranking");
            System.out.println("6. View Player Match History");
            System.out.println("7. View Team Match History");
            System.out.println("8. Manage Players");
            System.out.println("9. Manage Heroes");
            System.out.println("10. Manage Equipment");
            System.out.println("11. Manage Teams");
            System.out.println("12. Manage Match Records");
            System.out.println("13. Save Data");
            System.out.println("14. Load Data");
            System.out.println("15. View Data Summary");
            System.out.println("0. Logout");

            int choice = inputHelper.readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    handleSearchPlayer();
                    break;
                case 2:
                    handleSearchTeam();
                    break;
                case 3:
                    handleSearchHero();
                    break;
                case 4:
                    handlePlayerLeaderboard();
                    break;
                case 5:
                    handleEquipmentRanking();
                    break;
                case 6:
                    handlePlayerMatchHistory();
                    break;
                case 7:
                    handleTeamMatchHistory();
                    break;
                case 8:
                    handleManagePlayersMenu();
                    break;
                case 9:
                    handleManageHeroesMenu();
                    break;
                case 10:
                    handleManageEquipmentMenu();
                    break;
                case 11:
                    handleManageTeamsMenu();
                    break;
                case 12:
                    handleManageMatchesMenu();
                    break;
                case 13:
                    handleSaveData();
                    break;
                case 14:
                    handleLoadData();
                    inMenu = false;
                    break;
                case 15:
                    printDataSummary();
                    break;
                case 0:
                    authenticationService.logout();
                    inMenu = false;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void runPlayerMenu() {
        boolean inMenu = true;
        while (inMenu && authenticationService.isLoggedIn()) {
            System.out.println();
            System.out.println("=== Player Menu ===");
            System.out.println("1. View My Profile");
            System.out.println("2. View My Match History");
            System.out.println("3. Search Team");
            System.out.println("4. Search Hero");
            System.out.println("5. View Player Leaderboard");
            System.out.println("6. View Equipment Ranking");
            System.out.println("0. Logout");

            int choice = inputHelper.readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    printPlayerDetails((Player) authenticationService.getCurrentUser());
                    break;
                case 2:
                    handleCurrentPlayerMatchHistory();
                    break;
                case 3:
                    handleSearchTeam();
                    break;
                case 4:
                    handleSearchHero();
                    break;
                case 5:
                    handlePlayerLeaderboard();
                    break;
                case 6:
                    handleEquipmentRanking();
                    break;
                case 0:
                    authenticationService.logout();
                    inMenu = false;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void handleManagePlayersMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            System.out.println("=== Manage Players ===");
            System.out.println("1. Add Player");
            System.out.println("2. Update Player");
            System.out.println("3. Delete Player");
            System.out.println("4. List Players");
            System.out.println("0. Back");

            int choice = inputHelper.readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    addPlayer();
                    break;
                case 2:
                    updatePlayer();
                    break;
                case 3:
                    deletePlayer();
                    break;
                case 4:
                    listPlayers();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void handleManageHeroesMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            System.out.println("=== Manage Heroes ===");
            System.out.println("1. Add Hero");
            System.out.println("2. Update Hero");
            System.out.println("3. Delete Hero");
            System.out.println("4. List Heroes");
            System.out.println("0. Back");

            int choice = inputHelper.readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    addHero();
                    break;
                case 2:
                    updateHero();
                    break;
                case 3:
                    deleteHero();
                    break;
                case 4:
                    listHeroes();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void handleManageEquipmentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            System.out.println("=== Manage Equipment ===");
            System.out.println("1. Add Equipment");
            System.out.println("2. Update Equipment");
            System.out.println("3. Delete Equipment");
            System.out.println("4. List Equipment");
            System.out.println("0. Back");

            int choice = inputHelper.readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    addEquipment();
                    break;
                case 2:
                    updateEquipment();
                    break;
                case 3:
                    deleteEquipment();
                    break;
                case 4:
                    listEquipment();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void handleManageTeamsMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            System.out.println("=== Manage Teams ===");
            System.out.println("1. Add Team");
            System.out.println("2. Update Team");
            System.out.println("3. Delete Team");
            System.out.println("4. List Teams");
            System.out.println("0. Back");

            int choice = inputHelper.readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    addTeam();
                    break;
                case 2:
                    updateTeam();
                    break;
                case 3:
                    deleteTeam();
                    break;
                case 4:
                    listTeams();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void handleManageMatchesMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println();
            System.out.println("=== Manage Match Records ===");
            System.out.println("1. Add Match Record");
            System.out.println("2. Update Match Record");
            System.out.println("3. Delete Match Record");
            System.out.println("4. List Match Records");
            System.out.println("0. Back");

            int choice = inputHelper.readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    addMatchRecord();
                    break;
                case 2:
                    updateMatchRecord();
                    break;
                case 3:
                    deleteMatchRecord();
                    break;
                case 4:
                    listMatchRecords();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void addPlayer() {
        try {
            String id = inputHelper.readText("Player ID: ");
            String name = inputHelper.readText("Player name: ");
            String username = inputHelper.readText("Username: ");
            String password = inputHelper.readText("Password: ");
            int level = inputHelper.readInt("Level: ");
            double winRate = inputHelper.readDouble("Win rate: ");
            List<Hero> heroes = resolveHeroesByPrompt("Hero IDs (comma-separated, blank for none): ");
            String teamId = inputHelper.readText("Team ID (blank for no team): ");

            Player player = new Player(id, name, username, password, level, winRate);
            player.setHeroes(heroes);

            dataManager.addPlayer(player);
            assignPlayerToTeam(player, teamId);
            System.out.println("Player added successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Add player failed: " + exception.getMessage());
        }
    }

    private void updatePlayer() {
        String id = inputHelper.readText("Player ID to update: ");
        Player existingPlayer = dataManager.getPlayerById(id).orElse(null);
        if (existingPlayer == null) {
            System.out.println("Player not found.");
            return;
        }

        try {
            Team currentTeam = dataManager.getTeamByPlayerId(existingPlayer.getId()).orElse(null);
            String name = inputHelper.readText("New player name: ");
            String username = inputHelper.readText("New username: ");
            String password = inputHelper.readText("New password: ");
            int level = inputHelper.readInt("New level: ");
            double winRate = inputHelper.readDouble("New win rate: ");
            List<Hero> heroes = resolveHeroesByPrompt("New hero IDs (comma-separated, blank for none): ");
            String teamId = inputHelper.readText("New team ID (blank for no team, current "
                    + (currentTeam == null ? "none" : currentTeam.getTeamId()) + "): ");

            Player updatedPlayer = new Player(existingPlayer.getId(), name, username, password, level, winRate);
            updatedPlayer.setHeroes(heroes);
            updatedPlayer.setMatchHistory(existingPlayer.getMatchHistory());

            dataManager.updatePlayer(updatedPlayer);
            assignPlayerToTeam(updatedPlayer, teamId);
            System.out.println("Player updated successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Update player failed: " + exception.getMessage());
        }
    }

    private void deletePlayer() {
        try {
            String id = inputHelper.readText("Player ID to delete: ");
            dataManager.deletePlayer(id);
            System.out.println("Player deleted successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Delete player failed: " + exception.getMessage());
        }
    }

    private void addHero() {
        try {
            String id = inputHelper.readText("Hero ID: ");
            String name = inputHelper.readText("Hero name: ");
            HeroType heroType = readHeroType("Hero type");
            int attack = inputHelper.readInt("Attack: ");
            int defense = inputHelper.readInt("Defense: ");
            List<Equipment> equipmentList = resolveEquipmentByPrompt("Equipment IDs (comma-separated, blank for none): ");

            Hero hero = new Hero(id, name, heroType, attack, defense);
            hero.setEquipmentList(equipmentList);

            dataManager.addHero(hero);
            System.out.println("Hero added successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Add hero failed: " + exception.getMessage());
        }
    }

    private void updateHero() {
        String id = inputHelper.readText("Hero ID to update: ");
        Hero existingHero = dataManager.getHeroById(id).orElse(null);
        if (existingHero == null) {
            System.out.println("Hero not found.");
            return;
        }

        try {
            String name = inputHelper.readText("New hero name: ");
            HeroType heroType = readHeroType("New hero type");
            int attack = inputHelper.readInt("New attack: ");
            int defense = inputHelper.readInt("New defense: ");
            List<Equipment> equipmentList = resolveEquipmentByPrompt("New equipment IDs (comma-separated, blank for none): ");

            Hero updatedHero = new Hero(existingHero.getHeroId(), name, heroType, attack, defense);
            updatedHero.setEquipmentList(equipmentList);

            dataManager.updateHero(updatedHero);
            System.out.println("Hero updated successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Update hero failed: " + exception.getMessage());
        }
    }

    private void deleteHero() {
        try {
            String id = inputHelper.readText("Hero ID to delete: ");
            dataManager.deleteHero(id);
            System.out.println("Hero deleted successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Delete hero failed: " + exception.getMessage());
        }
    }

    private void addEquipment() {
        try {
            String id = inputHelper.readText("Equipment ID: ");
            String name = inputHelper.readText("Equipment name: ");
            EquipmentType equipmentType = readEquipmentType("Equipment type");
            double score = inputHelper.readDouble("Score: ");

            Equipment equipment = new Equipment(id, name, equipmentType, score);
            dataManager.addEquipment(equipment);
            System.out.println("Equipment added successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Add equipment failed: " + exception.getMessage());
        }
    }

    private void updateEquipment() {
        String id = inputHelper.readText("Equipment ID to update: ");
        Equipment existingEquipment = dataManager.getEquipmentById(id).orElse(null);
        if (existingEquipment == null) {
            System.out.println("Equipment not found.");
            return;
        }

        try {
            String name = inputHelper.readText("New equipment name: ");
            EquipmentType equipmentType = readEquipmentType("New equipment type");
            double score = inputHelper.readDouble("New score: ");

            Equipment updatedEquipment = new Equipment(existingEquipment.getEquipmentId(), name, equipmentType, score);
            dataManager.updateEquipment(updatedEquipment);
            System.out.println("Equipment updated successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Update equipment failed: " + exception.getMessage());
        }
    }

    private void deleteEquipment() {
        try {
            String id = inputHelper.readText("Equipment ID to delete: ");
            dataManager.deleteEquipment(id);
            System.out.println("Equipment deleted successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Delete equipment failed: " + exception.getMessage());
        }
    }

    private void addTeam() {
        try {
            String id = inputHelper.readText("Team ID: ");
            String name = inputHelper.readText("Team name: ");
            List<Player> members = resolvePlayersByPrompt("Member player IDs (comma-separated, blank for none): ");

            Team team = new Team(id, name);
            team.setMembers(members);
            dataManager.addTeam(team);
            System.out.println("Team added successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Add team failed: " + exception.getMessage());
        }
    }

    private void updateTeam() {
        String id = inputHelper.readText("Team ID to update: ");
        Team existingTeam = dataManager.getTeamById(id).orElse(null);
        if (existingTeam == null) {
            System.out.println("Team not found.");
            return;
        }

        try {
            String name = inputHelper.readText("New team name: ");
            List<Player> members = resolvePlayersByPrompt("New member player IDs (comma-separated, blank for none): ");

            Team updatedTeam = new Team(existingTeam.getTeamId(), name);
            updatedTeam.setMembers(members);
            dataManager.updateTeam(updatedTeam);
            System.out.println("Team updated successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Update team failed: " + exception.getMessage());
        }
    }

    private void deleteTeam() {
        try {
            String id = inputHelper.readText("Team ID to delete: ");
            dataManager.deleteTeam(id);
            System.out.println("Team deleted successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Delete team failed: " + exception.getMessage());
        }
    }

    private void addMatchRecord() {
        try {
            String id = inputHelper.readText("Match ID: ");
            LocalDate date = readDate("Match date");
            String opponentName = inputHelper.readText("Opponent name: ");
            MatchResult result = readMatchResult("Match result");
            List<Player> players = resolvePlayersByPrompt("Player IDs (comma-separated): ");
            List<Hero> heroesUsed = resolveHeroesByPrompt("Hero IDs used (comma-separated): ");

            MatchRecord matchRecord = new MatchRecord(id, date, opponentName, result);
            matchRecord.setPlayers(players);
            matchRecord.setHeroesUsed(heroesUsed);

            dataManager.addMatchRecord(matchRecord);
            System.out.println("Match record added successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Add match record failed: " + exception.getMessage());
        }
    }

    private void updateMatchRecord() {
        String id = inputHelper.readText("Match ID to update: ");
        MatchRecord existingMatchRecord = dataManager.getMatchRecordById(id).orElse(null);
        if (existingMatchRecord == null) {
            System.out.println("Match record not found.");
            return;
        }

        try {
            LocalDate date = readDate("New match date");
            String opponentName = inputHelper.readText("New opponent name: ");
            MatchResult result = readMatchResult("New match result");
            List<Player> players = resolvePlayersByPrompt("New player IDs (comma-separated): ");
            List<Hero> heroesUsed = resolveHeroesByPrompt("New hero IDs used (comma-separated): ");

            MatchRecord updatedMatchRecord = new MatchRecord(existingMatchRecord.getMatchId(), date, opponentName, result);
            updatedMatchRecord.setPlayers(players);
            updatedMatchRecord.setHeroesUsed(heroesUsed);

            dataManager.deleteMatchRecord(existingMatchRecord.getMatchId());
            dataManager.addMatchRecord(updatedMatchRecord);
            System.out.println("Match record updated successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Update match record failed: " + exception.getMessage());
        }
    }

    private void deleteMatchRecord() {
        try {
            String id = inputHelper.readText("Match ID to delete: ");
            dataManager.deleteMatchRecord(id);
            System.out.println("Match record deleted successfully.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Delete match record failed: " + exception.getMessage());
        }
    }

    private void handleSaveData() {
        try {
            fileStorageService.save(dataManager);
            System.out.println("Data saved to " + fileStorageService.getDataDirectory().toAbsolutePath() + ".");
        } catch (IllegalStateException exception) {
            System.out.println("Save failed: " + exception.getMessage());
        }
    }

    private void handleLoadData() {
        try {
            GameDataManager loadedData = fileStorageService.load();
            applyLoadedData(loadedData);
            authenticationService.logout();
            System.out.println("Data loaded successfully. Please login again.");
        } catch (IllegalStateException exception) {
            System.out.println("Load failed: " + exception.getMessage());
        }
    }

    private void applyLoadedData(GameDataManager loadedData) {
        dataManager.setUsers(loadedData.getUsers());
        dataManager.setAdmins(loadedData.getAdmins());
        dataManager.setPlayers(loadedData.getPlayers());
        dataManager.setHeroes(loadedData.getHeroes());
        dataManager.setEquipmentItems(loadedData.getEquipmentItems());
        dataManager.setTeams(loadedData.getTeams());
        dataManager.setMatchRecords(loadedData.getMatchRecords());
    }

    private void handleSearchPlayer() {
        String keyword = inputHelper.readText("Enter player ID, name, or username: ");
        Player player = searchService.searchPlayer(keyword).orElse(null);
        if (player == null) {
            System.out.println("Player not found.");
            return;
        }
        printPlayerDetails(player);
    }

    private void handleSearchTeam() {
        String keyword = inputHelper.readText("Enter team ID or name: ");
        Team team = searchService.searchTeam(keyword).orElse(null);
        if (team == null) {
            System.out.println("Team not found.");
            return;
        }
        printTeamDetails(team);
    }

    private void handleSearchHero() {
        String keyword = inputHelper.readText("Enter hero ID or name: ");
        Hero hero = searchService.searchHero(keyword).orElse(null);
        if (hero == null) {
            System.out.println("Hero not found.");
            return;
        }
        printHeroDetails(hero);
    }

    private void handlePlayerMatchHistory() {
        String playerId = inputHelper.readText("Enter player ID: ");
        int limit = inputHelper.readInt("How many recent matches to show? ");
        List<MatchRecord> matches = searchService.findMatchesForPlayer(playerId, limit);
        if (matches.isEmpty()) {
            System.out.println("No match history found for that player.");
            return;
        }
        printMatchRecords(matches);
    }

    private void handleTeamMatchHistory() {
        String teamId = inputHelper.readText("Enter team ID: ");
        int limit = inputHelper.readInt("How many recent matches to show? ");
        List<MatchRecord> matches = searchService.findMatchesForTeam(teamId, limit);
        if (matches.isEmpty()) {
            System.out.println("No match history found for that team.");
            return;
        }
        printMatchRecords(matches);
    }

    private void handleCurrentPlayerMatchHistory() {
        Player currentPlayer = (Player) authenticationService.getCurrentUser();
        int limit = inputHelper.readInt("How many recent matches to show? ");
        List<MatchRecord> matches = searchService.findMatchesForPlayer(currentPlayer.getId(), limit);
        if (matches.isEmpty()) {
            System.out.println("No match history found.");
            return;
        }
        printMatchRecords(matches);
    }

    private void handlePlayerLeaderboard() {
        System.out.println();
        System.out.println("=== Player Leaderboard ===");
        System.out.println("1. By Win Rate");
        System.out.println("2. By Level");
        System.out.println("3. By Match Count");
        System.out.println("4. By Composite Score");

        int type = inputHelper.readInt("Choose ranking type: ");
        int limit = inputHelper.readInt("How many players to show? ");
        List<Player> players;
        switch (type) {
            case 1:
                players = rankingService.rankPlayersByWinRate(limit);
                printPlayerRanking(players, "Win Rate");
                break;
            case 2:
                players = rankingService.rankPlayersByLevel(limit);
                printPlayerRanking(players, "Level");
                break;
            case 3:
                players = rankingService.rankPlayersByMatchCount(limit);
                printPlayerRanking(players, "Match Count");
                break;
            case 4:
                players = rankingService.rankPlayersByCompositeScore(limit);
                printPlayerRanking(players, "Composite Score");
                break;
            default:
                System.out.println("Invalid ranking type.");
                break;
        }
    }

    private void handleEquipmentRanking() {
        int limit = inputHelper.readInt("How many equipment items to show? ");
        List<Equipment> ranking = rankingService.rankEquipmentByScore(limit);
        System.out.println();
        System.out.println("=== Equipment Ranking ===");
        for (int i = 0; i < ranking.size(); i++) {
            Equipment equipment = ranking.get(i);
            System.out.println((i + 1) + ". " + equipment.getEquipmentName()
                    + " (" + equipment.getEquipmentId() + ")"
                    + " | Type: " + equipment.getEquipmentType()
                    + " | Score: " + equipment.getScore());
        }
    }

    private void printPlayerDetails(Player player) {
        System.out.println();
        System.out.println("=== Player Details ===");
        System.out.println("ID: " + player.getId());
        System.out.println("Name: " + player.getName());
        System.out.println("Username: " + player.getUsername());
        System.out.println("Level: " + player.getLevel());
        System.out.println("Win Rate: " + player.getWinRate());

        Team team = dataManager.getTeamByPlayerId(player.getId()).orElse(null);
        System.out.println("Team: " + (team == null ? "N/A" : team.getTeamName()));

        System.out.println("Heroes:");
        if (player.getHeroes().isEmpty()) {
            System.out.println("- None");
            return;
        }

        for (Hero hero : player.getHeroes()) {
            System.out.println("- " + hero.getHeroName() + " (" + hero.getHeroId() + ")");
            for (Equipment equipment : hero.getEquipmentList()) {
                System.out.println("  * " + equipment.getEquipmentName() + " [" + equipment.getEquipmentType() + "]");
            }
        }
    }

    private void printTeamDetails(Team team) {
        System.out.println();
        System.out.println("=== Team Details ===");
        System.out.println("ID: " + team.getTeamId());
        System.out.println("Name: " + team.getTeamName());
        System.out.println("Members:");
        if (team.getMembers().isEmpty()) {
            System.out.println("- None");
        } else {
            for (Player member : team.getMembers()) {
                System.out.println("- " + member.getName() + " (" + member.getId() + ")");
            }
        }

        double averageLevel = team.getMembers().stream()
                .mapToInt(Player::getLevel)
                .average()
                .orElse(0.0);

        List<MatchRecord> teamMatches = searchService.findMatchesForTeam(team.getTeamId(), dataManager.getMatchRecords().size());
        long wins = teamMatches.stream().filter(match -> match.getResult() == MatchResult.WIN).count();
        double winRate = teamMatches.isEmpty() ? 0.0 : (wins * 100.0 / teamMatches.size());

        Player topPlayer = team.getMembers().stream()
                .max(Comparator.comparingDouble(Player::getWinRate))
                .orElse(null);

        System.out.println("Average Level: " + String.format("%.2f", averageLevel));
        System.out.println("Total Matches: " + teamMatches.size());
        System.out.println("Win Rate: " + String.format("%.2f", winRate));
        System.out.println("Top Player: " + (topPlayer == null ? "N/A" : topPlayer.getName()));
    }

    private void printHeroDetails(Hero hero) {
        System.out.println();
        System.out.println("=== Hero Details ===");
        System.out.println("ID: " + hero.getHeroId());
        System.out.println("Name: " + hero.getHeroName());
        System.out.println("Type: " + hero.getHeroType());
        System.out.println("Attack: " + hero.getAttack());
        System.out.println("Defense: " + hero.getDefense());

        System.out.println("Compatible Equipment:");
        if (hero.getEquipmentList().isEmpty()) {
            System.out.println("- None");
        } else {
            for (Equipment equipment : hero.getEquipmentList()) {
                System.out.println("- " + equipment.getEquipmentName() + " | Type: "
                        + equipment.getEquipmentType() + " | Score: " + equipment.getScore());
            }
        }

        System.out.println("Owned By:");
        boolean foundOwner = false;
        for (Player player : dataManager.getPlayers()) {
            boolean ownsHero = player.getHeroes().stream()
                    .anyMatch(playerHero -> playerHero.getHeroId().equalsIgnoreCase(hero.getHeroId()));
            if (ownsHero) {
                System.out.println("- " + player.getName() + " (" + player.getId() + ")");
                foundOwner = true;
            }
        }
        if (!foundOwner) {
            System.out.println("- None");
        }
    }

    private void printMatchRecords(List<MatchRecord> matches) {
        System.out.println();
        System.out.println("=== Match History ===");
        for (MatchRecord match : matches) {
            System.out.println(match.getMatchId()
                    + " | Date: " + match.getDate()
                    + " | Opponent: " + match.getOpponentName()
                    + " | Result: " + match.getResult());
        }
    }

    private void printPlayerRanking(List<Player> players, String rankingType) {
        System.out.println();
        System.out.println("=== Player Ranking: " + rankingType + " ===");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println((i + 1) + ". " + player.getName()
                    + " (" + player.getId() + ")"
                    + " | Level: " + player.getLevel()
                    + " | Win Rate: " + player.getWinRate()
                    + " | Matches: " + player.getMatchHistory().size()
                    + " | Composite: " + String.format("%.2f", calculateCompositeScore(player)));
        }
    }

    private void printDataSummary() {
        System.out.println();
        System.out.println("=== Data Summary ===");
        System.out.println("Admins: " + dataManager.getAdmins().size());
        System.out.println("Players: " + dataManager.getPlayers().size());
        System.out.println("Heroes: " + dataManager.getHeroes().size());
        System.out.println("Equipment: " + dataManager.getEquipmentItems().size());
        System.out.println("Teams: " + dataManager.getTeams().size());
        System.out.println("Match Records: " + dataManager.getMatchRecords().size());
    }

    private void listPlayers() {
        System.out.println();
        System.out.println("=== Player List ===");
        for (Player player : dataManager.getPlayers()) {
            Team team = dataManager.getTeamByPlayerId(player.getId()).orElse(null);
            System.out.println(player.getId() + " | " + player.getName()
                    + " | Level: " + player.getLevel()
                    + " | Team: " + (team == null ? "N/A" : team.getTeamName()));
        }
    }

    private void listHeroes() {
        System.out.println();
        System.out.println("=== Hero List ===");
        for (Hero hero : dataManager.getHeroes()) {
            System.out.println(hero.getHeroId() + " | " + hero.getHeroName()
                    + " | Type: " + hero.getHeroType()
                    + " | Equipment Count: " + hero.getEquipmentList().size());
        }
    }

    private void listEquipment() {
        System.out.println();
        System.out.println("=== Equipment List ===");
        for (Equipment equipment : dataManager.getEquipmentItems()) {
            System.out.println(equipment.getEquipmentId() + " | " + equipment.getEquipmentName()
                    + " | Type: " + equipment.getEquipmentType()
                    + " | Score: " + equipment.getScore());
        }
    }

    private void listTeams() {
        System.out.println();
        System.out.println("=== Team List ===");
        for (Team team : dataManager.getTeams()) {
            System.out.println(team.getTeamId() + " | " + team.getTeamName()
                    + " | Members: " + team.getMembers().size());
        }
    }

    private void listMatchRecords() {
        List<MatchRecord> orderedMatches = new ArrayList<>(dataManager.getMatchRecords());
        orderedMatches.sort(Comparator.comparing(MatchRecord::getDate));

        System.out.println();
        System.out.println("=== Match Record List ===");
        for (MatchRecord matchRecord : orderedMatches) {
            System.out.println(matchRecord.getMatchId()
                    + " | Date: " + matchRecord.getDate()
                    + " | Opponent: " + matchRecord.getOpponentName()
                    + " | Result: " + matchRecord.getResult()
                    + " | Players: " + matchRecord.getPlayers().size());
        }
    }

    private void assignPlayerToTeam(Player player, String teamId) {
        for (Team team : dataManager.getTeams()) {
            team.getMembers().removeIf(member -> samePlayerId(member, player));
        }

        if (isBlank(teamId)) {
            return;
        }

        Team targetTeam = dataManager.getTeamById(teamId).orElseThrow(
                () -> new IllegalArgumentException("Team not found: " + teamId));

        boolean alreadyExists = targetTeam.getMembers().stream()
                .anyMatch(member -> samePlayerId(member, player));
        if (!alreadyExists) {
            targetTeam.addMember(player);
        }
    }

    private List<Hero> resolveHeroesByPrompt(String prompt) {
        List<Hero> heroes = new ArrayList<>();
        for (String heroId : readIdInput(prompt)) {
            Hero hero = dataManager.getHeroById(heroId).orElse(null);
            if (hero == null) {
                throw new IllegalArgumentException("Hero not found: " + heroId);
            }
            heroes.add(hero);
        }
        return heroes;
    }

    private List<Equipment> resolveEquipmentByPrompt(String prompt) {
        List<Equipment> equipmentItems = new ArrayList<>();
        for (String equipmentId : readIdInput(prompt)) {
            Equipment equipment = dataManager.getEquipmentById(equipmentId).orElse(null);
            if (equipment == null) {
                throw new IllegalArgumentException("Equipment not found: " + equipmentId);
            }
            equipmentItems.add(equipment);
        }
        return equipmentItems;
    }

    private List<Player> resolvePlayersByPrompt(String prompt) {
        List<Player> players = new ArrayList<>();
        for (String playerId : readIdInput(prompt)) {
            Player player = dataManager.getPlayerById(playerId).orElse(null);
            if (player == null) {
                throw new IllegalArgumentException("Player not found: " + playerId);
            }
            players.add(player);
        }
        return players;
    }

    private List<String> readIdInput(String prompt) {
        String rawInput = inputHelper.readText(prompt);
        List<String> ids = new ArrayList<>();
        if (isBlank(rawInput)) {
            return ids;
        }

        String[] parts = rawInput.split(",");
        for (String part : parts) {
            String value = part.trim();
            if (!value.isEmpty()) {
                ids.add(value);
            }
        }
        return ids;
    }

    private HeroType readHeroType(String label) {
        while (true) {
            System.out.println(label + " options: TANK, MAGE, ASSASSIN, MARKSMAN, SUPPORT");
            String value = inputHelper.readText(label + ": ");
            try {
                return HeroType.valueOf(value.trim().toUpperCase());
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid hero type. Please try again.");
            }
        }
    }

    private EquipmentType readEquipmentType(String label) {
        while (true) {
            System.out.println(label + " options: ATTACK, DEFENSE, MAGIC, MOVEMENT");
            String value = inputHelper.readText(label + ": ");
            try {
                return EquipmentType.valueOf(value.trim().toUpperCase());
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid equipment type. Please try again.");
            }
        }
    }

    private MatchResult readMatchResult(String label) {
        while (true) {
            System.out.println(label + " options: WIN, LOSS, DRAW");
            String value = inputHelper.readText(label + ": ");
            try {
                return MatchResult.valueOf(value.trim().toUpperCase());
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid match result. Please try again.");
            }
        }
    }

    private LocalDate readDate(String label) {
        while (true) {
            String value = inputHelper.readText(label + " (yyyy-mm-dd): ");
            try {
                return LocalDate.parse(value.trim());
            } catch (Exception exception) {
                System.out.println("Invalid date. Please use yyyy-mm-dd.");
            }
        }
    }

    private boolean samePlayerId(Player first, Player second) {
        return first != null
                && second != null
                && first.getId() != null
                && first.getId().equalsIgnoreCase(second.getId());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private double calculateCompositeScore(Player player) {
        return player.getWinRate() * 0.7 + player.getLevel() * 0.3;
    }
}
