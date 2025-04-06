package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> games;

    public MemGameDAO() {
        games = new HashMap<>();
    }

    public Integer createGame(String gameName, Integer gameID) {
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, newGame);
        return gameID;
    }

    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public GameData saveGame(int gameID, GameData game) {
        if (games.containsKey(gameID)) {
            games.put(gameID, game);
            return game;
        } else{ return null;}
    }

    public void removeAllGames() {
        games.clear();
    }

    public boolean isEmpty() {
        return games.isEmpty();
    }
}
