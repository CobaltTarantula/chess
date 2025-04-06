package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;

import model.GameData;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Integer createGame(String authToken, String gameName) throws DataAccessException {
        if (isValid(authToken)){ throw new DataAccessException("unauthorized");}
        if (gameName == null){ throw new DataAccessException("bad request");}

        Integer gameID = generateID(gameName);

        if (gameDAO.getGame(gameID) != null) {
            throw new DataAccessException("game already exists");
        }

        return gameDAO.createGame(gameName, gameID);
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (isValid(authToken)){ throw new DataAccessException("unauthorized");}
        return gameDAO.listGames();
    }

    public GameData joinGame(String authToken, String playerColor, Integer gameID) throws DataAccessException {
        if (isValid(authToken)){ throw new DataAccessException("unauthorized");}

        String userName = authDAO.getUsername(authToken);

        if (gameID == null){ throw new DataAccessException("bad request");}
        GameData game = gameDAO.getGame(gameID);

        GameData updatedGame = updateGame(game, userName, playerColor);

        return gameDAO.saveGame(gameID, updatedGame);
    }

    private GameData updateGame(GameData game, String userName, String playerColor) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("bad request");
        }
        if (playerColor == null || (!playerColor.equals("WHITE") && !playerColor.equals("BLACK"))) {
            throw new DataAccessException("bad request");
        }

        if (Objects.equals(playerColor, "WHITE") && game.whiteUsername() == null) {
            return new GameData(game.gameID(), userName, game.blackUsername(), game.gameName(), game.game());
        }

        else if (Objects.equals(playerColor, "BLACK") && game.blackUsername() == null) {
            return new GameData(game.gameID(), game.whiteUsername(), userName, game.gameName(), game.game());
        }

        else{ throw new DataAccessException("already taken");}
    }

    private Integer generateID(String gameName) {
        Random generator = new Random(gameName.hashCode());
        return 1000 + generator.nextInt(9000);
    }

    private boolean isValid(String authToken) throws DataAccessException {
        return authToken == null || authDAO.getAuth(authToken) == null;
    }
}