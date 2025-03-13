package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    @Override
    public void createUser(UserData user) throws DataAccessException {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                statement.setString(1, user.username());
                statement.setString(2, user.password());
                statement.setString(3, user.email());

                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String query = "";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                //body
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void removeAllUsers() throws DataAccessException {
        String query = "";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                //body
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        String query = "";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                //body
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
