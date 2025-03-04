package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemUserDAO implements UserDAO {
    private final HashMap<String, UserData> users;

    public MemUserDAO() {
        users = new HashMap<>();
    }

    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void removeAllUsers() {
        users.clear();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }
}