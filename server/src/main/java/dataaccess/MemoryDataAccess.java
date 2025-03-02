package dataaccess;
import model.*;

import javax.xml.crypto.Data;
import java.util.*;

public class MemoryDataAccess implements DataAccess{
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, GameData> games = new HashMap<>();
    private final Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        games.clear();
        authTokens.clear();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("User already exists");
        }
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        // FIXME
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        // FIXME
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        // FIXME
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        // FIXME
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        // FIXME
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        // FIXME
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        // FIXME
    }
}
