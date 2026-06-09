import java.nio.file.Paths;
import java.util.List;
import enums.HeroType;
import model.Equipment;
import model.Hero;
import model.MatchRecord;
import model.Player;
import model.Team;
import service.AuthenticationService;
import service.FileStorageService;
import service.GameDataManager;
import service.RankingService;
import service.SearchService;
import util.DataInitializer;

public class Main {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        run("T01 initial dataset counts", Main::testInitialDatasetCounts);
        run("T02 team member counts", Main::testTeamMemberCounts);
        run("T03 player hero ownership", Main::testPlayerHeroOwnership);
        run("T04 hero equipment compatibility", Main::testHeroEquipmentCompatibility);
        run("T05 lookup by id", Main::testLookupById);
        run("T06 team lookup by player id", Main::testTeamByPlayerId);
        run("T07 duplicate player id rejected", Main::testDuplicatePlayerRejected);
        run("T08 add player updates users and players", Main::testAddPlayer);
        run("T09 delete player clears references", Main::testDeletePlayerReferences);
        run("T10 update hero replaces references", Main::testUpdateHeroReferences);
        run("T11 delete equipment clears hero references", Main::testDeleteEquipmentReferences);
        run("T12 search player by id or name", Main::testSearchPlayer);
        run("T13 search team by id or name", Main::testSearchTeam);
        run("T14 search hero by id or name", Main::testSearchHero);
        run("T15 player match history lookup", Main::testFindMatchesForPlayer);
        run("T16 team match history lookup", Main::testFindMatchesForTeam);
        run("T17 player ranking by win rate and level", Main::testRankPlayersByWinRateAndLevel);
        run("T18 equipment ranking by score", Main::testRankEquipmentByScore);
        run("T19 match count and composite ranking", Main::testRankPlayersByMatchCountAndComposite);
        run("T20 authentication not implemented", Main::testAuthenticationNotImplemented);
        run("T21 file storage not implemented", Main::testFileStorageNotImplemented);

        System.out.println("SUMMARY passed=" + passed + " failed=" + failed);
    }

    private static GameDataManager createData() {
        return new DataInitializer().createInitialData();
    }

    private static void testInitialDatasetCounts() {
        GameDataManager data = createData();
        check(data.getAdmins().size() == 1, "expected 1 admin");
        check(data.getPlayers().size() == 15, "expected 15 players");
        check(data.getHeroes().size() == 15, "expected 15 heroes");
        check(data.getEquipmentItems().size() == 20, "expected 20 equipment items");
        check(data.getTeams().size() == 3, "expected 3 teams");
        check(data.getMatchRecords().size() == 10, "expected 10 match records");
    }

    private static void testTeamMemberCounts() {
        for (Team team : createData().getTeams()) {
            check(team.getMembers().size() == 5, "team should have 5 members: " + team.getTeamId());
        }
    }

    private static void testPlayerHeroOwnership() {
        for (Player player : createData().getPlayers()) {
            check(player.getHeroes().size() >= 3, "player should own at least 3 heroes: " + player.getId());
        }
    }

    private static void testHeroEquipmentCompatibility() {
        for (Hero hero : createData().getHeroes()) {
            check(hero.getEquipmentList().size() >= 2, "hero should have at least 2 equipment items: " + hero.getHeroId());
        }
    }

    private static void testLookupById() {
        GameDataManager data = createData();
        check(data.getPlayerById("P001").isPresent(), "P001 should exist");
        check(data.getHeroById("h001").isPresent(), "h001 lookup should be case-insensitive");
        check(data.getEquipmentById("E001").isPresent(), "E001 should exist");
        check(data.getTeamById("T001").isPresent(), "T001 should exist");
        check(data.getMatchRecordById("M001").isPresent(), "M001 should exist");
    }

    private static void testTeamByPlayerId() {
        Team team = createData().getTeamByPlayerId("P001")
                .orElseThrow(() -> new AssertionError("team for P001 should exist"));
        check("T001".equals(team.getTeamId()), "P001 should be in T001");
    }

    private static void testDuplicatePlayerRejected() {
        GameDataManager data = createData();
        expectIllegalArgument(() -> data.addPlayer(new Player("P001", "Duplicate", "dup", "p123", 1, 1.0)));
    }

    private static void testAddPlayer() {
        GameDataManager data = createData();
        Player player = new Player("P099", "New Player", "newplayer", "p123", 10, 50.0);
        data.addPlayer(player);
        check(data.getPlayers().size() == 16, "players should increase to 16");
        check(data.getUsers().contains(player), "users should include new player");
        check(data.getPlayerById("P099").isPresent(), "new player should be findable");
    }

    private static void testDeletePlayerReferences() {
        GameDataManager data = createData();
        data.deletePlayer("P001");
        check(data.getPlayerById("P001").isEmpty(), "P001 should be deleted");
        check(data.getUserById("P001").isEmpty(), "P001 should be removed from users");
        for (Team team : data.getTeams()) {
            check(team.getMembers().stream().noneMatch(player -> "P001".equals(player.getId())), "teams should not contain P001");
        }
        for (MatchRecord record : data.getMatchRecords()) {
            check(record.getPlayers().stream().noneMatch(player -> "P001".equals(player.getId())), "matches should not contain P001");
        }
    }

    private static void testUpdateHeroReferences() {
        GameDataManager data = createData();
        Hero oldHero = data.getHeroById("H001").orElseThrow();
        Hero updatedHero = new Hero("H001", "Arthur Prime", HeroType.TANK, 80, 96);
        data.updateHero(updatedHero);
        check(data.getHeroById("H001").orElseThrow().getHeroName().equals("Arthur Prime"), "hero name should update");
        check(data.getPlayers().stream().flatMap(player -> player.getHeroes().stream()).noneMatch(hero -> hero == oldHero), "player lists should not keep old hero reference");
        check(data.getMatchRecords().stream().flatMap(record -> record.getHeroesUsed().stream()).noneMatch(hero -> hero == oldHero), "match lists should not keep old hero reference");
    }

    private static void testDeleteEquipmentReferences() {
        GameDataManager data = createData();
        Equipment equipment = data.getEquipmentById("E001").orElseThrow();
        data.deleteEquipment("E001");
        check(data.getEquipmentById("E001").isEmpty(), "E001 should be deleted");
        for (Hero hero : data.getHeroes()) {
            check(!hero.getEquipmentList().contains(equipment), "hero should not contain deleted equipment");
        }
    }

    private static void testSearchPlayer() {
        SearchService searchService = new SearchService(createData());
        check(searchService.searchPlayer("P001").isPresent(), "P001 should be found");
        check(searchService.searchPlayer("ming").isPresent(), "ming should be found by name or username");
        check(searchService.searchPlayer("unknown").isEmpty(), "unknown player should not be found");
    }

    private static void testSearchTeam() {
        SearchService searchService = new SearchService(createData());
        check(searchService.searchTeam("T001").isPresent(), "T001 should be found");
        check(searchService.searchTeam("moonlight").isPresent(), "team name search should work");
        check(searchService.searchTeam("unknown").isEmpty(), "unknown team should not be found");
    }

    private static void testSearchHero() {
        SearchService searchService = new SearchService(createData());
        check(searchService.searchHero("H001").isPresent(), "H001 should be found");
        check(searchService.searchHero("li bai").isPresent(), "hero name search should work");
        check(searchService.searchHero("unknown").isEmpty(), "unknown hero should not be found");
    }

    private static void testFindMatchesForPlayer() {
        SearchService searchService = new SearchService(createData());
        List<MatchRecord> matches = searchService.findMatchesForPlayer("P001", 3);
        check(!searchService.findMatchesForPlayer("P001", 20).isEmpty(), "P001 should have match history");
        check(matches.size() <= 3, "player match history should respect the limit");
        check(isSortedByDateDesc(matches), "player match history should be sorted by date desc");
        check(searchService.findMatchesForPlayer("UNKNOWN", 3).isEmpty(), "unknown player should return no matches");
    }

    private static void testFindMatchesForTeam() {
        SearchService searchService = new SearchService(createData());
        List<MatchRecord> matches = searchService.findMatchesForTeam("T001", 3);
        check(matches.size() == 3, "T001 should return last 3 matches");
        check(searchService.findMatchesForTeam("T001", 20).size() > 0, "T001 should have match history");
        check(isSortedByDateDesc(matches), "team match history should be sorted by date desc");
        check(searchService.findMatchesForTeam("UNKNOWN", 3).isEmpty(), "unknown team should return no matches");
    }

    private static void testRankPlayersByWinRateAndLevel() {
        RankingService rankingService = new RankingService(createData());
        List<Player> winRateRanking = rankingService.rankPlayersByWinRate(5);
        List<Player> levelRanking = rankingService.rankPlayersByLevel(5);

        check(winRateRanking.size() == 5, "win rate ranking should respect the limit");
        check("P006".equals(winRateRanking.get(0).getId()), "P006 should rank first by win rate");
        check(isSortedByWinRate(winRateRanking), "win rate ranking should be sorted");

        check(levelRanking.size() == 5, "level ranking should respect the limit");
        check("P006".equals(levelRanking.get(0).getId()), "P006 should rank first by level");
        check(isSortedByLevel(levelRanking), "level ranking should be sorted");
    }

    private static void testRankEquipmentByScore() {
        RankingService rankingService = new RankingService(createData());
        List<Equipment> ranking = rankingService.rankEquipmentByScore(5);

        check(ranking.size() == 5, "equipment ranking should respect the limit");
        check("E009".equals(ranking.get(0).getEquipmentId()), "E009 should rank first by score");
        check("E001".equals(ranking.get(1).getEquipmentId()), "E001 should rank second by score");
        check(isSortedByEquipmentScore(ranking), "equipment ranking should be sorted");
    }

    private static void testRankPlayersByMatchCountAndComposite() {
        RankingService rankingService = new RankingService(createData());
        List<Player> matchCountRanking = rankingService.rankPlayersByMatchCount(5);
        List<Player> compositeRanking = rankingService.rankPlayersByCompositeScore(5);

        check(matchCountRanking.size() == 5, "match count ranking should respect the limit");
        check(isSortedByMatchCount(matchCountRanking), "match count ranking should be sorted");

        check(compositeRanking.size() == 5, "composite ranking should respect the limit");
        check("P006".equals(compositeRanking.get(0).getId()), "P006 should rank first by composite score");
        check(isSortedByCompositeScore(compositeRanking), "composite ranking should be sorted");
    }

    private static void testAuthenticationNotImplemented() {
        expectUnsupported(() -> new AuthenticationService(createData()).login("admin", "admin123"));
    }

    private static void testFileStorageNotImplemented() {
        expectUnsupported(() -> new FileStorageService(Paths.get("data")).save(createData()));
    }

    private static boolean isSortedByDateDesc(List<MatchRecord> matches) {
        for (int i = 1; i < matches.size(); i++) {
            if (matches.get(i - 1).getDate().isBefore(matches.get(i).getDate())) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSortedByWinRate(List<Player> players) {
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i - 1).getWinRate() < players.get(i).getWinRate()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSortedByLevel(List<Player> players) {
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i - 1).getLevel() < players.get(i).getLevel()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSortedByMatchCount(List<Player> players) {
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i - 1).getMatchHistory().size() < players.get(i).getMatchHistory().size()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSortedByEquipmentScore(List<Equipment> equipmentList) {
        for (int i = 1; i < equipmentList.size(); i++) {
            if (equipmentList.get(i - 1).getScore() < equipmentList.get(i).getScore()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSortedByCompositeScore(List<Player> players) {
        for (int i = 1; i < players.size(); i++) {
            if (calculateCompositeScore(players.get(i - 1)) < calculateCompositeScore(players.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static double calculateCompositeScore(Player player) {
        return player.getWinRate() * 0.7 + player.getLevel() * 0.3;
    }

    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS " + name);
        } catch (Throwable error) {
            failed++;
            System.out.println("FAIL " + name + " -> " + error.getMessage());
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectIllegalArgument(Runnable runnable) {
        try {
            runnable.run();
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected path.
        }
    }

    private static void expectUnsupported(Runnable runnable) {
        try {
            runnable.run();
            throw new AssertionError("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // Expected path for unfinished features.
        }
    }
}
