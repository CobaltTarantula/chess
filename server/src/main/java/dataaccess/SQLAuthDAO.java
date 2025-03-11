package dataaccess;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public String createAuth(String username) throws DataAccessException {
        // string sql request
        String query = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                String authToken = UUID.randomUUID().toString();
                statement.setString(1, authToken);
                statement.setString(2, username);
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
                // body
                statement.setString(1, authToken);
                try(var find_auth = statement.executeQuery()){
                    if (find_auth.next()) {
                        return find_auth.getString("authToken");
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
        String query = "";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                // body code
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return "";
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String query = "";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                // body code
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void removeAllAuthTokens() throws DataAccessException {
        String query = "";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                // body code
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        String query = "";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                // body code
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean verifyAuth(String authToken) throws DataAccessException {
        String query = "";
        try(Connection conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement(query)){
                // body code
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return false;
    }
}
