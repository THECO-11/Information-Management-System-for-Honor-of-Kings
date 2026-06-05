package service;

import java.util.List;
import model.Equipment;
import model.Player;

public class RankingService {
    private final GameDataManager dataManager;

    public RankingService(GameDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Player> rankPlayersByWinRate(int limit) {
        throw new UnsupportedOperationException("Player ranking is not implemented yet.");
    }

    public List<Player> rankPlayersByLevel(int limit) {
        throw new UnsupportedOperationException("Level ranking is not implemented yet.");
    }

    public List<Equipment> rankEquipmentByScore(int limit) {
        throw new UnsupportedOperationException("Equipment ranking is not implemented yet.");
    }

    public GameDataManager getDataManager() {
        return dataManager;
    }
}
