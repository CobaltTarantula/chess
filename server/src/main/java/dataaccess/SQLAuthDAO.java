package dataaccess;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public String createAuth(String username) throws DataAccessException {
        String query = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                String authToken = UUID.randomUUID().toString();
                statement.setString(1, authToken);
                statement.setString(2, username);

                statement.executeUpdate();
                return authToken;
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        String query = "SELECT authToken FROM auths WHERE authToken = ?";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                statement.setString(1, authToken);
                try(var findAuth = statement.executeQuery()){
                    if (findAuth.next()) {
                        return findAuth.getString("authToken");
                    }
                    else {
                        return null;
                    }
                }
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        String query = "SELECT username FROM auths WHERE authToken = ?";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                statement.setString(1, authToken);
                try(var results = statement.executeQuery()){
                    if (results.next()) {
                        return results.getString("username");
                    }
                }
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String query = "DELETE FROM auths WHERE authToken = ?";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                statement.setString(1, authToken);
                statement.executeUpdate();
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void removeAllAuthTokens() throws DataAccessException {
        String query = "DELETE FROM auths";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                statement.executeUpdate();
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        String query = "SELECT 1 FROM auths LIMIT 1";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                try(var verifyEmpty = statement.executeQuery()){
                    return !verifyEmpty.next();
                }
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean verifyAuth(String authToken) throws DataAccessException {
        String query = "SELECT 1 FROM auths WHERE authToken = ? LIMIT 1";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                statement.setString(1, authToken);
                try(var authList = statement.executeQuery()){
                    if(authList.next()){
                        return true;
                    }
                }
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return false;
    }
}
