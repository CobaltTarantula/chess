package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public String loginUser(String username, String password) throws DataAccessException {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password are required");
        }
        if (userDAO.getUser(username) == null) {
            throw new DataAccessException("No user found");
        }
        return authDAO.createAuth(username);
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