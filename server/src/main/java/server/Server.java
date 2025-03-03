package server;

import service.GameService;
import service.UserService;
import spark.*;
import dataaccess.*;

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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
