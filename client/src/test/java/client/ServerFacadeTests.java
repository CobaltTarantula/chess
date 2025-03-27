package client;

import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on port " + port);
        facade = new ServerFacade(port); // Initialize the facade with the port
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    // Run before each test to reset any state (such as clearing the database)
    @BeforeEach
    public void resetState() throws IOException {
        facade.clear(); // Reset the database before each test
    }

    // Positive test case: Test successful registration
    @Test
    void register_success() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authData.authToken(), "Auth token should not be null after successful registration");
        assertTrue(authData.authToken().length() > 10, "Auth token should be long enough");
    }

    // Negative test case: Test registration with an already existing username
    @Test
    void register_duplicateUsername() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        Exception exception = assertThrows(Exception.class, () -> {
            facade.register("player1", "newpassword", "newp1@email.com");
        });
        assertTrue(exception.getMessage().contains("Username already exists"), "Should throw an exception for duplicate username");
    }

    // Positive test case: Test successful login
    @Test
    void login_success() throws Exception {
        facade.register("player2", "password", "p2@email.com");  // First register the user
        var authData = facade.login("player2", "password");
        assertNotNull(authData.authToken(), "Auth token should not be null after successful login");
        assertTrue(authData.authToken().length() > 10, "Auth token should be long enough");
    }

    // Negative test case: Test login with incorrect password
    @Test
    void login_incorrectPassword() throws Exception {
        facade.register("player3", "password", "p3@email.com");  // Register first
        Exception exception = assertThrows(Exception.class, () -> {
            facade.login("player3", "wrongPassword");  // Attempt login with wrong password
        });
        assertTrue(exception.getMessage().contains("Invalid credentials"), "Should throw exception for invalid password");
    }

    @Test
    void listGames_success() throws Exception {
        // Register and login a player
        facade.register("player4", "password", "p4@email.com");
        var authData = facade.login("player4", "password");  // login returns authData

        // Retrieve the authentication token from the login response
        String authToken = authData.authToken();

        // Create a game before listing
        facade.createGame(authToken, "Test Game");

        // Now pass the auth token when calling listGames
        var games = facade.listGames(authToken);  // Assuming listGames requires an auth token

        // Check if the game list is not null and has at least one game
        assertNotNull(games, "Game list should not be null");
        assertFalse(games.isEmpty(), "Game list should have at least one game");
    }


    // Negative test case: Test action on an unavailable game (e.g., joining a non-existent game)
    @Test
    void joinGame_nonExistentGame() throws Exception {
        facade.register("player5", "password", "p5@email.com");
        facade.login("player5", "password");
        Exception exception = assertThrows(Exception.class, () -> {
            facade.joinGame("FIXME", "WHITE", 999);  // Try joining a non-existent game with ID 999
        });
        assertTrue(exception.getMessage().contains("Game not found"), "Should throw an exception for a non-existent game");
    }

    // Example: Test handling of invalid server requests (e.g., invalid endpoint)
    @Test
    void invalidRequest() {
        Exception exception = assertThrows(IOException.class, () -> {
            facade.makeInvalidRequest();  // Assuming there's a method that makes an invalid request
        });
        assertTrue(exception.getMessage().contains("404"), "Should throw an IOException for invalid request");
    }
}