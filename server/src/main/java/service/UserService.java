package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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

        if (userDAO.getUser(user.username(), user.password()) != null) {
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

        if (userDAO.getUser(user.username(), user.password()) == null) {
            throw new DataAccessException("unauthorized");
        }

        String username = user.username();

        UserData savedUser = userDAO.getUser(username, user.password());
        String givenPassword = user.password();
        String savedPassword = savedUser.password();
        if (!verifyPassword(givenPassword, savedPassword)) {
            throw new DataAccessException("unauthorized");
        }

        String token = authDAO.createAuth(username);
        return new AuthData(token, username);
    }

    private boolean verifyPassword(String givenPassword, String savedPassword) {
        return BCrypt.checkpw(givenPassword, savedPassword);
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