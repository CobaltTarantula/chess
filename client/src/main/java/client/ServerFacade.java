package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
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
        connection.setDoOutput(!method.equals("GET"));
        connection.addRequestProperty("Authorization", authToken);

        // Write JSON body for applicable methods
        if (reqJson != null && !method.equals("GET")) {
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(new Gson().toJson(reqJson).getBytes());
            }
        }

        // Read response
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream resBody = connection.getInputStream()) {
                return new Gson().fromJson(new InputStreamReader(resBody), responseType);
            }
        } else {
            throw new IOException("HTTP error code: " + connection.getResponseCode());
        }
    }

    public AuthData register(String username, String password, String email) throws IOException {
        URL url = new URL(baseUrl + "/user");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("username", username);
        reqJson.addProperty("password", password);
        reqJson.addProperty("email", email);
        Map<String, String> res = sendRequest("POST", url, reqJson, new TypeToken<Map<String, String>>() {}.getType());
        return new AuthData(res.get("authToken"), res.get("username"));
    }

    public AuthData login(String username, String password) throws IOException{
        URL url = new URL(baseUrl + "/session");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("username", username);
        reqJson.addProperty("password", password);
        Map<String, String> res = sendRequest("POST", url, reqJson, new TypeToken<Map<String, String>>() {}.getType());
        return new AuthData(res.get("authToken"), res.get("username"));
    }

    public int logout(String authToken) throws IOException {
        this.authToken = authToken;
        URL url = new URL(baseUrl + "/session");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        return sendRequest("DELETE", url, reqJson, Integer.class);
    }

    public Integer createGame(String authToken, String gameName) throws IOException {
        this.authToken = authToken;
        URL url = new URL(baseUrl + "/game");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        reqJson.addProperty("gameName", gameName);
        Map<String, String> res = sendRequest("POST", url, reqJson, new TypeToken<Map<String, String>>() {}.getType());
        return Integer.valueOf(res.get("gameID"));
    }

    public int joinGame(String authToken, String playerColor, int gameId) throws IOException {
        this.authToken = authToken;
        URL url = new URL(baseUrl + "/game");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        reqJson.addProperty("playerColor", playerColor);
        reqJson.addProperty("gameID", gameId);
        return sendRequest("PUT", url, reqJson, Integer.class);
    }

    public Collection<GameData> listGames(String authToken) throws IOException {
        this.authToken = authToken;
        URL url = new URL(baseUrl + "/game");
        Map<String, String> res = sendRequest("GET", url, null, new TypeToken<Map<String, String>>() {}.getType());
        return new Gson().fromJson(new Gson().toJson(res.get("games")), new TypeToken<Collection<GameData>>() {}.getType());
    }
}
