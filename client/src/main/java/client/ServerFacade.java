package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServerFacade {
    private final String baseUrl;
    private String authToken;

    public ServerFacade(int givenPort) {
        baseUrl = "http://localhost:" + givenPort;
        authToken = null;
    }

    private <T> T sendRequest(String method, URL url, JsonObject reqJson, Type responseType) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);

        boolean hasBody = method.equals("POST") || method.equals("PUT") || method.equals("DELETE");
        connection.setDoOutput(hasBody);

        if (authToken != null) {
            connection.addRequestProperty("Authorization", authToken);
        }

        connection.setRequestProperty("Content-Type", "application/json");

        if (hasBody && reqJson != null) {
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(new Gson().toJson(reqJson).getBytes());
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            if (responseType == Void.class) return null;

            try (InputStream resBody = connection.getInputStream()) {
                return new Gson().fromJson(new InputStreamReader(resBody), responseType);
            }
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }

    public URL validateUrl(String suffix) throws IOException{
        URL url;
        try {
            url = new URI(baseUrl + suffix).toURL();
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URL syntax: " + e.getMessage(), e);
        }
        return url;
    }

    public AuthData register(String username, String password, String email) throws IOException {
        URL url = validateUrl("/user");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("username", username);
        reqJson.addProperty("password", password);
        reqJson.addProperty("email", email);
        Map<String, String> res = sendRequest("POST", url, reqJson,
                new TypeToken<Map<String, String>>() {}.getType());
        assert res != null;
        return new AuthData(res.get("authToken"), res.get("username"));
    }

    public AuthData login(String username, String password) throws IOException{
        URL url = validateUrl("/session");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("username", username);
        reqJson.addProperty("password", password);
        Map<String, String> res = sendRequest("POST", url, reqJson, new TypeToken<Map<String, String>>() {}.getType());
        assert res != null;
        return new AuthData(res.get("authToken"), res.get("username"));
    }

    public int logout(String authToken) throws IOException {
        this.authToken = authToken;
        URL url = validateUrl("/session");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        sendRequest("DELETE", url, reqJson, Void.class);
        return HttpURLConnection.HTTP_OK;
    }

    public Integer createGame(String authToken, String gameName) throws IOException {
        this.authToken = authToken;
        URL url = validateUrl("/game");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        reqJson.addProperty("gameName", gameName);
        Map<String, String> res = sendRequest("POST", url, reqJson, new TypeToken<Map<String, String>>() {}.getType());
        assert res != null;
        return Integer.valueOf(res.get("gameID"));
    }

    public void joinGame(String authToken, String playerColor, int gameId) throws IOException {
        this.authToken = authToken;
        URL url = validateUrl("/game");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        reqJson.addProperty("playerColor", playerColor);
        reqJson.addProperty("gameID", gameId);

        sendRequest("PUT", url, reqJson, JsonObject.class);
    }

    public Collection<GameData> listGames(String authToken) throws IOException {
        this.authToken = authToken;
        URL url = validateUrl("/game");

        Map<String, List<GameData>> res = sendRequest("GET", url, null, new TypeToken<Map<String, List<GameData>>>() {}.getType());

        return res != null ? res.getOrDefault("games", Collections.emptyList()) : Collections.emptyList();
    }

    public void clear() throws IOException {
        URL url = validateUrl("/db");
        JsonObject reqJson = new JsonObject();
        sendRequest("DELETE", url, reqJson, Void.class);
    }
}
