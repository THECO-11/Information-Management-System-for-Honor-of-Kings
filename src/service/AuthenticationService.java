package service;

import enums.Role;
import java.util.Optional;
import model.Person;

public class AuthenticationService {
    private final GameDataManager dataManager;
    private Person currentUser;

    public AuthenticationService(GameDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Optional<Person> login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return Optional.empty();
        }

        Optional<Person> matchedUser = dataManager.getUsers().stream()
                .filter(user -> matchesCredentials(user, username, password))
                .findFirst();

        currentUser = matchedUser.orElse(null);
        return matchedUser;
    }

    public void logout() {
        currentUser = null;
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Person currentUser) {
        this.currentUser = currentUser;
    }

    public GameDataManager getDataManager() {
        return dataManager;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.getRole() == Role.ADMIN;
    }

    public boolean isCurrentUserPlayer() {
        return currentUser != null && currentUser.getRole() == Role.PLAYER;
    }

    public Optional<Role> getCurrentRole() {
        if (currentUser == null) {
            return Optional.empty();
        }
        return Optional.of(currentUser.getRole());
    }

    private boolean matchesCredentials(Person user, String username, String password) {
        return user != null
                && user.getUsername() != null
                && user.getPassword() != null
                && user.getUsername().equalsIgnoreCase(username.trim())
                && user.getPassword().equals(password);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
