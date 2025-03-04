package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData registerUser(UserData user) throws DataAccessException {
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
        if (user.username() == null || user.password() == null) {
            throw new DataAccessException("must fill all fields");
        }

        if (userDAO.getUser(user.username()) == null) {
            throw new DataAccessException("unauthorized");
        }

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
        } else{ return true;}
    }

    public void logoutUser(String authToken) throws DataAccessException {
        if (verifyAuth(authToken)) {
            authDAO.deleteAuth(authToken);
        }
        authDAO.getAuth(authToken);
    }
}