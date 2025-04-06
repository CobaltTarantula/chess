package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    @Override
    public void createUser(UserData user) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());  // Hash the password
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                statement.setString(1, user.username());
                statement.setString(2, hashedPassword);
                statement.setString(3, user.email());

                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(query)) {

            statement.setString(1, username);

            try (var results = statement.executeQuery()) {
                if (!results.next()){
                    return null;
                }

                String storedHashedPassword = results.getString("password");
                if (!BCrypt.checkpw(password, storedHashedPassword)){
                    return null;
                }

                return new UserData(
                        results.getString("username"),
                        storedHashedPassword,
                        results.getString("email")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAllUsers() throws DataAccessException {
        String query = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        String query = "SELECT 1 FROM users LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                try (var results = statement.executeQuery()) {
                    return !results.next();
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
