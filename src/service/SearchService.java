package service;

import java.util.List;
import java.util.Optional;
import model.Hero;
import model.MatchRecord;
import model.Player;
import model.Team;

public class SearchService {
    private final GameDataManager dataManager;

    public SearchService(GameDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Optional<Player> searchPlayer(String keyword) {
        throw new UnsupportedOperationException("Player search is not implemented yet.");
    }

    public Optional<Team> searchTeam(String keyword) {
        throw new UnsupportedOperationException("Team search is not implemented yet.");
    }

    public Optional<Hero> searchHero(String keyword) {
        throw new UnsupportedOperationException("Hero search is not implemented yet.");
    }

    public List<MatchRecord> findMatchesForPlayer(String playerId, int limit) {
        throw new UnsupportedOperationException("Player match history search is not implemented yet.");
    }

    public List<MatchRecord> findMatchesForTeam(String teamId, int limit) {
        throw new UnsupportedOperationException("Team match history search is not implemented yet.");
    }

    public GameDataManager getDataManager() {
        return dataManager;
    }
}
