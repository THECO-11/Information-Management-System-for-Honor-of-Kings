package model;

import enums.Role;

public class Admin extends Person {
    public Admin() {
    }

    public Admin(String id, String name, String username, String password) {
        super(id, name, username, password);
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}
