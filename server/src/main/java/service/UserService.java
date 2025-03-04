package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("bad request");
        }

        if (userDAO.getUser(user.username()) != null) {
            throw new DataAccessException("already taken");
        }

        userDAO.createUser(user);
        String username = user.username();
        String token = authDAO.createAuth(username);

        return new AuthData(token, username);
    }

    public AuthData loginUser(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("bad request");
        }

        // check for bad username
        if (userDAO.getUser(user.username()) == null) {
            throw new DataAccessException("unauthorized");
        }

        if (userDAO.getUser(user.username()) != null) {
            throw new DataAccessException("already taken");
        }

        // check if given password matches the one in the database
        UserData savedUser = userDAO.getUser(user.username());
        if (!verifyPassword(user.password(), savedUser.password())) {
            throw new DataAccessException("unauthorized");
        }

        userDAO.createUser(user);
        String username = user.username();
        String token = authDAO.createAuth(username);

        return new AuthData(token, username);
    }

    private boolean verifyPassword(String givenPassword, String savedPassword) {
        return givenPassword.equals(savedPassword);
    }

    private boolean verifyAuth(String authToken) throws DataAccessException {
        if (authToken == null || authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("unauthorized");
        } else return true;
    }

    public void logoutUser(String token) throws DataAccessException {
        if (!authDAO.verifyAuth(token)) {
            throw new DataAccessException("Invalid token");
        }
        authDAO.deleteAuth(token);
    }
}