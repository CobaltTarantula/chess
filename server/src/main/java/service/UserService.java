package service;

import dataaccess.*;
import model.*;

public class UserService {
    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    public void register(UserData user) throws DataAccessException {
        dao.createUser(user);
    }

    public UserData login(String username, String password) throws DataAccessException {
        UserData user = dao.getUser(username);
        if (user == null || !user.password().equals(password)) {
            throw new DataAccessException("Invalid credentials");
        }
        return user;
    }
}
