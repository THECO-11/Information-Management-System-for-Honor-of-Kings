import java.nio.file.Paths;
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
        run("T12 search service not implemented", Main::testSearchServiceNotImplemented);
        run("T13 ranking service not implemented", Main::testRankingServiceNotImplemented);
        run("T14 authentication not implemented", Main::testAuthenticationNotImplemented);
        run("T15 file storage not implemented", Main::testFileStorageNotImplemented);

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

    private static void testSearchServiceNotImplemented() {
        expectUnsupported(() -> new SearchService(createData()).searchPlayer("P001"));
    }

    private static void testRankingServiceNotImplemented() {
        expectUnsupported(() -> new RankingService(createData()).rankPlayersByWinRate(5));
    }

    private static void testAuthenticationNotImplemented() {
        expectUnsupported(() -> new AuthenticationService(createData()).login("admin", "admin123"));
    }

    private static void testFileStorageNotImplemented() {
        expectUnsupported(() -> new FileStorageService(Paths.get("data")).save(createData()));
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
