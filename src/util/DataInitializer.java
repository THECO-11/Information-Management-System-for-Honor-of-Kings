package util;

import enums.EquipmentType;
import enums.HeroType;
import enums.MatchResult;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Admin;
import model.Equipment;
import model.Hero;
import model.MatchRecord;
import model.Person;
import model.Player;
import model.Team;
import service.GameDataManager;

public class DataInitializer {
    public GameDataManager createInitialData() {
        GameDataManager dataManager = new GameDataManager();

        List<Equipment> equipmentItems = createEquipmentItems();
        List<Hero> heroes = createHeroes(equipmentItems);
        List<Player> players = createPlayers(heroes);
        List<Admin> admins = createAdmins();
        List<Team> teams = createTeams(players);
        List<MatchRecord> matchRecords = createMatchRecords(players, heroes);

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
    }

    private List<Admin> createAdmins() {
        return new ArrayList<>(Arrays.asList(
                new Admin("A001", "System Admin", "admin", "admin123")
        ));
    }

    private List<Equipment> createEquipmentItems() {
        return new ArrayList<>(Arrays.asList(
                new Equipment("E001", "Shadow Blade", EquipmentType.ATTACK, 91.5),
                new Equipment("E002", "Storm Bow", EquipmentType.ATTACK, 88.0),
                new Equipment("E003", "Dragon Spear", EquipmentType.ATTACK, 86.5),
                new Equipment("E004", "Blood Dagger", EquipmentType.ATTACK, 84.0),
                new Equipment("E005", "Guardian Shield", EquipmentType.DEFENSE, 89.0),
                new Equipment("E006", "Iron Armor", EquipmentType.DEFENSE, 82.5),
                new Equipment("E007", "Phoenix Cloak", EquipmentType.DEFENSE, 87.0),
                new Equipment("E008", "Titan Helmet", EquipmentType.DEFENSE, 80.0),
                new Equipment("E009", "Arcane Staff", EquipmentType.MAGIC, 92.0),
                new Equipment("E010", "Moon Orb", EquipmentType.MAGIC, 88.5),
                new Equipment("E011", "Frost Tome", EquipmentType.MAGIC, 85.5),
                new Equipment("E012", "Flame Crown", EquipmentType.MAGIC, 83.5),
                new Equipment("E013", "Swift Boots", EquipmentType.MOVEMENT, 79.0),
                new Equipment("E014", "Wind Boots", EquipmentType.MOVEMENT, 81.0),
                new Equipment("E015", "Hunter Boots", EquipmentType.MOVEMENT, 82.0),
                new Equipment("E016", "War Boots", EquipmentType.MOVEMENT, 80.5),
                new Equipment("E017", "Sunbreaker", EquipmentType.ATTACK, 90.0),
                new Equipment("E018", "Ocean Guard", EquipmentType.DEFENSE, 86.0),
                new Equipment("E019", "Spirit Codex", EquipmentType.MAGIC, 87.5),
                new Equipment("E020", "Light Treads", EquipmentType.MOVEMENT, 78.5)
        ));
    }

    private List<Hero> createHeroes(List<Equipment> equipmentItems) {
        List<Hero> heroes = new ArrayList<>(Arrays.asList(
                new Hero("H001", "Arthur", HeroType.TANK, 72, 90),
                new Hero("H002", "Angela", HeroType.MAGE, 88, 45),
                new Hero("H003", "Li Bai", HeroType.ASSASSIN, 94, 50),
                new Hero("H004", "Hou Yi", HeroType.MARKSMAN, 91, 48),
                new Hero("H005", "Cai Wenji", HeroType.SUPPORT, 55, 75),
                new Hero("H006", "Luban No.7", HeroType.MARKSMAN, 89, 42),
                new Hero("H007", "Diao Chan", HeroType.MAGE, 90, 52),
                new Hero("H008", "Zhao Yun", HeroType.ASSASSIN, 86, 68),
                new Hero("H009", "Xiang Yu", HeroType.TANK, 70, 93),
                new Hero("H010", "Sun Shangxiang", HeroType.MARKSMAN, 92, 47),
                new Hero("H011", "Zhang Fei", HeroType.SUPPORT, 62, 88),
                new Hero("H012", "Wang Zhaojun", HeroType.MAGE, 87, 50),
                new Hero("H013", "Han Xin", HeroType.ASSASSIN, 93, 53),
                new Hero("H014", "Yao", HeroType.SUPPORT, 58, 76),
                new Hero("H015", "Lian Po", HeroType.TANK, 67, 95)
        ));

        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            hero.addEquipment(equipmentItems.get(i % equipmentItems.size()));
            hero.addEquipment(equipmentItems.get((i + 5) % equipmentItems.size()));
        }

        return heroes;
    }

    private List<Player> createPlayers(List<Hero> heroes) {
        List<Player> players = new ArrayList<>(Arrays.asList(
                new Player("P001", "Ming", "ming", "p123", 48, 68.5),
                new Player("P002", "Lan", "lan", "p123", 51, 71.2),
                new Player("P003", "Chen", "chen", "p123", 44, 63.4),
                new Player("P004", "Yun", "yun", "p123", 56, 74.0),
                new Player("P005", "Kai", "kai", "p123", 39, 59.8),
                new Player("P006", "Rui", "rui", "p123", 62, 77.5),
                new Player("P007", "Qing", "qing", "p123", 46, 66.7),
                new Player("P008", "Tao", "tao", "p123", 53, 72.1),
                new Player("P009", "Nan", "nan", "p123", 41, 61.9),
                new Player("P010", "Wei", "wei", "p123", 58, 75.3),
                new Player("P011", "Jin", "jin", "p123", 45, 64.6),
                new Player("P012", "Bo", "bo", "p123", 49, 69.4),
                new Player("P013", "Shuo", "shuo", "p123", 52, 70.8),
                new Player("P014", "Ling", "ling", "p123", 43, 62.7),
                new Player("P015", "Hao", "hao", "p123", 60, 76.2)
        ));

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.addHero(heroes.get(i % heroes.size()));
            player.addHero(heroes.get((i + 3) % heroes.size()));
            player.addHero(heroes.get((i + 7) % heroes.size()));
        }

        return players;
    }

    private List<Team> createTeams(List<Player> players) {
        Team dragon = new Team("T001", "Dragon Vanguard");
        Team moon = new Team("T002", "Moonlight Guard");
        Team phoenix = new Team("T003", "Phoenix Storm");

        for (int i = 0; i < 5; i++) {
            dragon.addMember(players.get(i));
            moon.addMember(players.get(i + 5));
            phoenix.addMember(players.get(i + 10));
        }

        return new ArrayList<>(Arrays.asList(dragon, moon, phoenix));
    }

    private List<MatchRecord> createMatchRecords(List<Player> players, List<Hero> heroes) {
        List<MatchRecord> records = new ArrayList<>();
        String[] opponents = {
                "Crimson Wolves", "Azure Kings", "Silver Stars", "Night Falcons", "Thunder Lions",
                "Golden Wings", "Royal Blades", "Emerald Tide", "Iron Tigers", "Cloud Hunters"
        };
        MatchResult[] results = {
                MatchResult.WIN, MatchResult.LOSS, MatchResult.WIN, MatchResult.DRAW, MatchResult.WIN,
                MatchResult.LOSS, MatchResult.WIN, MatchResult.WIN, MatchResult.DRAW, MatchResult.LOSS
        };

        for (int i = 0; i < 10; i++) {
            MatchRecord record = new MatchRecord(
                    String.format("M%03d", i + 1),
                    LocalDate.of(2026, 5, 1).plusDays(i),
                    opponents[i],
                    results[i]
            );

            for (int j = 0; j < 5; j++) {
                Player player = players.get((i + j) % players.size());
                Hero hero = heroes.get((i + j * 2) % heroes.size());
                record.addPlayer(player);
                record.addHeroUsed(hero);
                player.addMatchRecord(record);
            }

            records.add(record);
        }

        return records;
    }
}
