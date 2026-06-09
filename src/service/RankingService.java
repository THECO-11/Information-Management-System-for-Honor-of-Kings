package service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import model.Equipment;
import model.Player;

public class RankingService {
    private final GameDataManager dataManager;

    public RankingService(GameDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Player> rankPlayersByWinRate(int limit) {
        return dataManager.getPlayers().stream()
                .sorted(playerWinRateComparator())
                .limit(normalizeLimit(limit, dataManager.getPlayers().size()))
                .collect(Collectors.toList());
    }

    public List<Player> rankPlayersByLevel(int limit) {
        return dataManager.getPlayers().stream()
                .sorted(playerLevelComparator())
                .limit(normalizeLimit(limit, dataManager.getPlayers().size()))
                .collect(Collectors.toList());
    }

    public List<Equipment> rankEquipmentByScore(int limit) {
        return dataManager.getEquipmentItems().stream()
                .sorted(equipmentScoreComparator())
                .limit(normalizeLimit(limit, dataManager.getEquipmentItems().size()))
                .collect(Collectors.toList());
    }

    public List<Player> rankPlayersByMatchCount(int limit) {
        return dataManager.getPlayers().stream()
                .sorted(playerMatchCountComparator())
                .limit(normalizeLimit(limit, dataManager.getPlayers().size()))
                .collect(Collectors.toList());
    }

    public List<Player> rankPlayersByCompositeScore(int limit) {
        return dataManager.getPlayers().stream()
                .sorted(playerCompositeComparator())
                .limit(normalizeLimit(limit, dataManager.getPlayers().size()))
                .collect(Collectors.toList());
    }

    public GameDataManager getDataManager() {
        return dataManager;
    }

    private Comparator<Player> playerWinRateComparator() {
        return Comparator.comparingDouble(Player::getWinRate).reversed()
                .thenComparing(Comparator.comparingInt(Player::getLevel).reversed())
                .thenComparing(Comparator.comparingInt((Player player) -> player.getMatchHistory().size()).reversed())
                .thenComparing(Player::getName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Player::getId, String.CASE_INSENSITIVE_ORDER);
    }

    private Comparator<Player> playerLevelComparator() {
        return Comparator.comparingInt(Player::getLevel).reversed()
                .thenComparing(Comparator.comparingDouble(Player::getWinRate).reversed())
                .thenComparing(Comparator.comparingInt((Player player) -> player.getMatchHistory().size()).reversed())
                .thenComparing(Player::getName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Player::getId, String.CASE_INSENSITIVE_ORDER);
    }

    private Comparator<Player> playerMatchCountComparator() {
        return Comparator.comparingInt((Player player) -> player.getMatchHistory().size()).reversed()
                .thenComparing(Comparator.comparingDouble(Player::getWinRate).reversed())
                .thenComparing(Comparator.comparingInt(Player::getLevel).reversed())
                .thenComparing(Player::getName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Player::getId, String.CASE_INSENSITIVE_ORDER);
    }

    private Comparator<Player> playerCompositeComparator() {
        return Comparator.comparingDouble(this::calculatePlayerCompositeScore).reversed()
                .thenComparing(Comparator.comparingDouble(Player::getWinRate).reversed())
                .thenComparing(Comparator.comparingInt(Player::getLevel).reversed())
                .thenComparing(Player::getName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Player::getId, String.CASE_INSENSITIVE_ORDER);
    }

    private Comparator<Equipment> equipmentScoreComparator() {
        return Comparator.comparingDouble(Equipment::getScore).reversed()
                .thenComparing(Equipment::getEquipmentName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Equipment::getEquipmentId, String.CASE_INSENSITIVE_ORDER);
    }

    private double calculatePlayerCompositeScore(Player player) {
        return player.getWinRate() * 0.7 + player.getLevel() * 0.3;
    }

    private int normalizeLimit(int limit, int size) {
        if (limit <= 0 || limit > size) {
            return size;
        }
        return limit;
    }
}
