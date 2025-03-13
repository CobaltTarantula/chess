package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Objects;


public class MemUserDAO implements UserDAO {
    private final HashMap<String, UserData> users;

    public MemUserDAO() {
        users = new HashMap<>();
    }

    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    public UserData getUser(String username, String password) {
        if (users.containsKey(username) && Objects.equals(users.get(username).password(), password)) {
            return users.get(username);
        }
        return null;
    }

    public void removeAllUsers() {
        users.clear();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }
}