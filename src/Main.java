import java.util.List;
import java.util.Scanner;
import model.Equipment;
import model.Hero;
import model.MatchRecord;
import model.Person;
import model.Player;
import model.Team;
import service.AuthenticationService;
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
    private final InputHelper inputHelper;

    public Main() {
        this.dataManager = new DataInitializer().createInitialData();
        this.searchService = new SearchService(dataManager);
        this.rankingService = new RankingService(dataManager);
        this.authenticationService = new AuthenticationService(dataManager);
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
            System.out.println("8. View Data Summary");
            System.out.println("9. Logout");

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
                    printDataSummary();
                    break;
                case 9:
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
            System.out.println("7. Logout");

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
                case 7:
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
        for (Player member : team.getMembers()) {
            System.out.println("- " + member.getName() + " (" + member.getId() + ")");
        }

        double averageLevel = team.getMembers().stream()
                .mapToInt(Player::getLevel)
                .average()
                .orElse(0.0);

        List<MatchRecord> teamMatches = searchService.findMatchesForTeam(team.getTeamId(), dataManager.getMatchRecords().size());
        long wins = teamMatches.stream().filter(match -> match.getResult() == enums.MatchResult.WIN).count();
        double winRate = teamMatches.isEmpty() ? 0.0 : (wins * 100.0 / teamMatches.size());

        Player topPlayer = team.getMembers().stream()
                .max((first, second) -> Double.compare(first.getWinRate(), second.getWinRate()))
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
        for (Equipment equipment : hero.getEquipmentList()) {
            System.out.println("- " + equipment.getEquipmentName() + " | Type: " + equipment.getEquipmentType() + " | Score: " + equipment.getScore());
        }

        System.out.println("Owned By:");
        for (Player player : dataManager.getPlayers()) {
            boolean ownsHero = player.getHeroes().stream()
                    .anyMatch(playerHero -> playerHero.getHeroId().equalsIgnoreCase(hero.getHeroId()));
            if (ownsHero) {
                System.out.println("- " + player.getName() + " (" + player.getId() + ")");
            }
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

    private double calculateCompositeScore(Player player) {
        return player.getWinRate() * 0.7 + player.getLevel() * 0.3;
    }
}
