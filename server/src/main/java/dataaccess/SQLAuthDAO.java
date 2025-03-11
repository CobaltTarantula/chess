package dataaccess;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public String createAuth(String username) throws DataAccessException {
        // string sql request
        return "";
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void removeAllAuthTokens() throws DataAccessException {

    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        return false;
    }

    @Override
    public boolean verifyAuth(String authToken) throws DataAccessException {
        return false;
    }
}
