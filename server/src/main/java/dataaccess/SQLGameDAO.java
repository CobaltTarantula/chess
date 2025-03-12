package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;
import java.sql.*;
import java.util.UUID;

public class SQLGameDAO implements GameDAO{
    @Override
    public Integer createGame(String gameName, Integer gameID) throws DataAccessException {
        String query = "INSERT INTO GAMES (gameID, gameName, whiteUsername, blackUsername, chessGame)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                //body
                statement.setInt(1, gameID);
                statement.setString(2, gameName);
                statement.setString(3, null);
                statement.setString(4, null);
                // chessGame
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData saveGame(int gameID, GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public void removeAllGames() throws DataAccessException {

    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        return false;
    }
}
