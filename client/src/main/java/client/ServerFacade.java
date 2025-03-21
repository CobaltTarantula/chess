package client;

import chess.ChessMove;
import chess.ChessPosition;
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
    private int port;
    private String baseUrl;
    private String authToken;

    public ServerFacade(int givenPort) {
        port = givenPort;
        baseUrl = "http://localhost:" + port;
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

    public AuthData register(){
        return null;
    }

    public AuthData login(){
        return null;
    }

    public int logout(){
        return 0;
    }

    public Integer createGame(){
        return null;
    }

    public int joinGame(){
        return 0;
    }

    public Collection<GameData> listGames(){
        return null;
    }
}
