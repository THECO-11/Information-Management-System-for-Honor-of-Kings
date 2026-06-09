package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        if (isBlank(keyword)) {
            return Optional.empty();
        }

        Optional<Player> byId = dataManager.getPlayerById(keyword);
        if (byId.isPresent()) {
            return byId;
        }

        return dataManager.getPlayers().stream()
                .filter(player -> matchesText(player.getName(), keyword) || matchesText(player.getUsername(), keyword))
                .findFirst();
    }

    public Optional<Team> searchTeam(String keyword) {
        if (isBlank(keyword)) {
            return Optional.empty();
        }

        Optional<Team> byId = dataManager.getTeamById(keyword);
        if (byId.isPresent()) {
            return byId;
        }

        return dataManager.getTeams().stream()
                .filter(team -> matchesText(team.getTeamName(), keyword))
                .findFirst();
    }

    public Optional<Hero> searchHero(String keyword) {
        if (isBlank(keyword)) {
            return Optional.empty();
        }

        Optional<Hero> byId = dataManager.getHeroById(keyword);
        if (byId.isPresent()) {
            return byId;
        }

        return dataManager.getHeroes().stream()
                .filter(hero -> matchesText(hero.getHeroName(), keyword))
                .findFirst();
    }

    public List<MatchRecord> findMatchesForPlayer(String playerId, int limit) {
        if (isBlank(playerId) || limit <= 0) {
            return new ArrayList<>();
        }

        Optional<Player> player = dataManager.getPlayerById(playerId);
        if (player.isEmpty()) {
            return new ArrayList<>();
        }

        return player.get().getMatchHistory().stream()
                .sorted(Comparator.comparing(MatchRecord::getDate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<MatchRecord> findMatchesForTeam(String teamId, int limit) {
        if (isBlank(teamId) || limit <= 0) {
            return new ArrayList<>();
        }

        Optional<Team> team = dataManager.getTeamById(teamId);
        if (team.isEmpty()) {
            return new ArrayList<>();
        }

        return dataManager.getMatchRecords().stream()
                .filter(matchRecord -> containsAnyTeamMember(matchRecord, team.get()))
                .sorted(Comparator.comparing(MatchRecord::getDate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public GameDataManager getDataManager() {
        return dataManager;
    }

    private boolean containsAnyTeamMember(MatchRecord matchRecord, Team team) {
        return matchRecord.getPlayers().stream()
                .anyMatch(matchPlayer -> team.getMembers().stream()
                        .anyMatch(teamPlayer -> samePlayerId(teamPlayer, matchPlayer)));
    }

    private boolean samePlayerId(Player first, Player second) {
        return first != null
                && second != null
                && first.getId() != null
                && first.getId().equalsIgnoreCase(second.getId());
    }

    private boolean matchesText(String source, String keyword) {
        return source != null && source.toLowerCase().contains(keyword.trim().toLowerCase());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
