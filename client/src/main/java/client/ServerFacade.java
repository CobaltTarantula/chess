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

        if (reqJson != null && !method.equals("GET")) {
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(new Gson().toJson(reqJson).getBytes());
            }
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream resBody = connection.getInputStream()) {
                return new Gson().fromJson(new InputStreamReader(resBody), responseType);
            }
        } else {
            throw new IOException("HTTP error code: " + connection.getResponseCode());
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
        Map<String, String> res = sendRequest("POST", url, reqJson, new TypeToken<Map<String, String>>() {}.getType());
        return new AuthData(res.get("authToken"), res.get("username"));
    }

    public AuthData login(String username, String password) throws IOException{
        URL url = validateUrl("/session");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("username", username);
        reqJson.addProperty("password", password);
        Map<String, String> res = sendRequest("POST", url, reqJson, new TypeToken<Map<String, String>>() {}.getType());
        return new AuthData(res.get("authToken"), res.get("username"));
    }

    public void logout(String authToken) throws IOException {
        this.authToken = authToken;
        URL url = validateUrl("/session");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        sendRequest("DELETE", url, reqJson, Integer.class);
    }

    public void createGame(String authToken, String gameName) throws IOException {
        this.authToken = authToken;
        URL url = validateUrl("/game");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        reqJson.addProperty("gameName", gameName);
        Map<String, String> res = sendRequest("POST", url, reqJson, new TypeToken<Map<String, String>>() {}.getType());
        Integer.valueOf(res.get("gameID"));
    }

    public void joinGame(String authToken, String playerColor, int gameId) throws IOException {
        this.authToken = authToken;
        URL url = validateUrl("/game");
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("authToken", authToken);
        reqJson.addProperty("playerColor", playerColor);
        reqJson.addProperty("gameID", gameId);
        sendRequest("PUT", url, reqJson, Integer.class);
    }

    public Collection<GameData> listGames(String authToken) throws IOException {
        this.authToken = authToken;
        URL url = validateUrl("/game");
        Map<String, String> res = sendRequest("GET", url, null, new TypeToken<Map<String, String>>() {}.getType());
        return new Gson().fromJson(new Gson().toJson(res.get("games")), new TypeToken<Collection<GameData>>() {}.getType());
    }

    public void clear() throws IOException {
        URL url = new URL(baseUrl + "/db");
        JsonObject reqJson = new JsonObject();
        doDelete(url, reqJson);
    }

    private void doDelete(URL url, JsonObject reqJson) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        // Write JSON body (if needed)
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = reqJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Check response code
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to clear: HTTP " + responseCode);
        }

        // Close the connection
        connection.disconnect();
    }

    public void makeInvalidRequest() throws IOException {
        // Perform an HTTP request to a non-existent endpoint (e.g., "/invalid")
        URL url = new URL(baseUrl + "/invalid");  // Invalid endpoint
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // This should throw an exception or return a 404 status
        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new IOException("404 Not Found: Invalid endpoint");
        }
    }
}
