package dataaccess;

import java.util.HashMap;
import java.util.UUID;

public class MemAuthDAO implements AuthDAO{
    private final HashMap<String, String> auths;

    public MemAuthDAO() {
        auths = new HashMap<>();
    }

    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        auths.put(authToken, username);
        return authToken;
    }

    public String getAuth(String authToken) {
        return auths.get(authToken);
    }
    public String getUsername(String authToken) {
        return auths.get(authToken);
    }
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    public void removeAllAuthTokens() {
        auths.clear();
    }

    public boolean isEmpty() {
        return auths.isEmpty();
    }

    @Override
    public Boolean verifyAuth(String authToken) throws DataAccessException {
        return auths.containsKey(authToken);
    }
}
