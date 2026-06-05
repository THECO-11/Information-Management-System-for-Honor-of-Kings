package service;

import java.util.Optional;
import model.Person;

public class AuthenticationService {
    private final GameDataManager dataManager;
    private Person currentUser;

    public AuthenticationService(GameDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Optional<Person> login(String username, String password) {
        throw new UnsupportedOperationException("Login is not implemented yet.");
    }

    public void logout() {
        throw new UnsupportedOperationException("Logout is not implemented yet.");
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
}
