package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username, String password) throws DataAccessException;

    void removeAllUsers() throws DataAccessException;

    boolean isEmpty() throws DataAccessException;
}