package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
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
                String gameJson = new Gson().toJson(new ChessGame());
                statement.setString(5, gameJson);
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameID;
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        String query = "SELECT * FROM games WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                statement.setInt(1, gameID);
                try (var results = statement.executeQuery()){
                    if (results.next()){
                        int found_id = results.getInt("gameID");
                        String gameName = results.getString("gameName");
                        String whiteUsername = results.getString("whiteUsername");
                        String blackUsername = results.getString("blackUsername");
                        String gameJson = results.getString("chessGame");
                        ChessGame chessGame = new Gson().fromJson(gameJson, ChessGame.class);
                        return new GameData(found_id, whiteUsername, blackUsername, gameName, chessGame);
                    }
                    else {
                        return null;
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        String query = "";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(query)) {
                //body
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    @Override
    public GameData saveGame(int gameID, GameData game) throws DataAccessException {
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
    public void removeAllGames() throws DataAccessException {
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
