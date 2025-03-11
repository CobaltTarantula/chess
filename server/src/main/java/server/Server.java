package server;

import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;
import dataaccess.*;

import com.google.gson.Gson;
import model.*;

import java.util.Collection;
import java.util.Map;

public class Server {

    private final UserDAO users = new MemUserDAO();
    private final GameDAO games = new MemGameDAO();
    private final AuthDAO auths = new MemAuthDAO();

    private final UserService userService = new UserService(auths, users);
    private final GameService gameService = new GameService(auths, games);
    private final ClearService clearService = new ClearService(users, games, auths);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // initialize database
        try {
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerHandler);
        Spark.delete("/db", this::clearHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    // handlers
    private Object registerHandler(Request req, Response res) {
        try {
            var serializer = new Gson();
            UserData request = serializer.fromJson(req.body(), UserData.class);

            AuthData result = userService.registerUser(request);

            res.status(200);
            var body = serializer.toJson(Map.of("username", result.username(), "authToken", result.authToken()));
            res.body(body);
            return body;
        } catch (DataAccessException e) {
            return errorHandler(e, res);
        }
    }

    private Object loginHandler(Request req, Response res) {
        try {
            var serializer = new Gson();
            UserData request = serializer.fromJson(req.body(), UserData.class);
            AuthData result = userService.loginUser(request);

            res.status(200);
            var body = serializer.toJson(Map.of("username", result.username(), "authToken", result.authToken()));
            res.body(body);
            return body;
        } catch (DataAccessException e) {
            return errorHandler(e, res);
        }
    }

    private Object logoutHandler(Request req, Response res) {
        try {
            String request = req.headers("Authorization");

            userService.logoutUser(request);

            res.status(200);
            var body = "{}";
            res.body(body);
            return body;

        } catch (DataAccessException e) {
            return errorHandler(e, res);
        }
    }

    private Object listGamesHandler(Request req, Response res) {
        try {
            var serializer = new Gson();
            String authToken = req.headers("Authorization");
            Collection<GameData> result = gameService.listGames(authToken);

            res.status(200);
            var body = serializer.toJson(Map.of("games", result));
            res.body(body);
            return body;

        } catch (DataAccessException e) {
            return errorHandler(e, res);
        }
    }

    private Object createGameHandler(Request req, Response res) {
        try {
            var serializer = new Gson();
            String authToken = req.headers("Authorization");
            GameData request = serializer.fromJson(req.body(), GameData.class);
            String gameName = request.gameName();

            Integer result = gameService.createGame(authToken, gameName);

            res.status(200);
            var body = serializer.toJson(Map.of("gameID", result));
            res.body(body);
            return body;

        } catch (DataAccessException e) {
            return errorHandler(e, res);
        }
    }

    private Object joinGameHandler(Request req, Response res) {
        try {
            var serializer = new Gson();
            var request = serializer.fromJson(req.body(), JoinRequest.class);
            String authToken = req.headers("Authorization");
            String playerColor = request.playerColor();
            Integer gameID = request.gameID();

            gameService.joinGame(authToken, playerColor, gameID);

            res.status(200);
            var body = "{}";
            res.body(body);
            return body;

        } catch (DataAccessException e) {
            return errorHandler(e, res);
        }
    }

    private Object clearHandler(Request req, Response res) {
        try {
            clearService.clear();

            res.status(200);
            res.body("{}");

            return "{}";
        } catch (DataAccessException e) {
            return errorHandler(e, res);
        }
    }

    public Object errorHandler(Exception e, Response res) {
        String errorMessage = e.getMessage();
        int statusCode = 0;
        statusCode = switch (errorMessage) {
            case "data not cleared.",
                 "must fill all fields",
                 "no account with that username found",
                 "game already exists" -> 500;
            case "bad request" -> 400;
            case "unauthorized" -> 401;
            case "already taken" -> 403;
            default -> statusCode;
        };

        var body = new Gson().toJson(Map.of("message", "Error: " + errorMessage));
        res.type("application/json");
        res.status(statusCode);
        res.body(body);
        return body;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
