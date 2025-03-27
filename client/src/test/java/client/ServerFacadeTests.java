package client;

import model.AuthData;
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
    void registerSuccess() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authData.authToken(), "Auth token should not be null after successful registration");
        assertTrue(authData.authToken().length() > 10, "Auth token should be long enough");
    }

    // Negative test case: Test registration with an already existing username
    @Test
    void registerDuplicateUsername() throws Exception {
        facade.register("player1", "password", "player1@email.com");  // Register first
        System.out.println("Registered player1 with password: password");

        try {
            System.out.println("Attempting to register player1 again...");
            facade.register("player1", "password123", "newemail@email.com");  // Attempt duplicate registration
            fail("Expected IOException due to duplicate username");  // Fail if no exception is thrown
        } catch (IOException e) {
            // Log the exception message for debugging
            System.out.println("Caught IOException: " + e.getMessage());

            // Assert that the exception message contains the expected duplicate username message
            assertTrue(e.getMessage().contains("HTTP error code: 500"),
                    "Expected 'HTTP error code: 500' in exception message");
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            fail("Expected IOException but got: " + e.getClass().getName());
        }
    }

    // Positive test case: Test successful login
    @Test
    void loginSuccess() throws Exception {
        facade.register("player2", "password", "p2@email.com");  // First register the user
        var authData = facade.login("player2", "password");
        assertNotNull(authData.authToken(), "Auth token should not be null after successful login");
        assertTrue(authData.authToken().length() > 10, "Auth token should be long enough");
    }

    // Negative test case: Test login with incorrect password
    @Test
    void loginIncorrectPassword() throws Exception {
        facade.register("player3", "password", "p3@email.com");  // Register first
        System.out.println("Registered player3 with password: password");

        try {
            System.out.println("Attempting login with incorrect password...");
            facade.login("player3", "wrongPassword");  // Attempt login with wrong password
            fail("Expected IOException due to incorrect password");  // Fail if no exception is thrown
        } catch (IOException e) {
            // Log the exception message for debugging
            System.out.println("Caught IOException: " + e.getMessage());

            // Assert that the exception message contains "HTTP error code: 401"
            assertTrue(e.getMessage().contains("HTTP error code: 401"),
                    "Expected 'HTTP error code: 401' in exception message");
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            fail("Expected IOException but got: " + e.getClass().getName());
        }
    }

    @Test
    void createGameSuccess() throws Exception {
        var authData = facade.register("player6", "password", "p6@email.com");
        String authToken = authData.authToken();

        // Create a game before checking the list
        facade.createGame(authToken, "Test Game");

        // List games and verify the new game is included
        var games = facade.listGames(authToken);

        // Verify at least one game matches the expected name
        boolean gameExists = games.stream().anyMatch(game -> game.gameName().equals("Test Game"));
        assertTrue(gameExists, "Created game should be in the list");
    }

    @Test
    void createGameInvalidAuth() {
        Exception exception = assertThrows(IOException.class, () -> facade.createGame("invalidAuthToken", "Invalid Game"));
        assertTrue(exception.getMessage().contains("401"), "Should throw unauthorized error for invalid auth token");
    }


    @Test
    void listGamesSuccess() throws Exception {
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

    @Test
    void listGamesInvalidAuth() {
        Exception exception = assertThrows(IOException.class, () -> facade.listGames("invalidAuthToken"));
        assertTrue(exception.getMessage().contains("401"), "Should throw unauthorized error for invalid auth token");
    }

    @Test
    void joinGameSuccess() throws Exception {
        var authData = facade.register("player7", "password", "p7@email.com");
        String authToken = authData.authToken();

        // Create a game
        facade.createGame(authToken, "Joinable Game");

        // Retrieve the game ID from the list of games
        var games = facade.listGames(authToken);
        var game = games.stream().filter(g -> g.gameName().equals("Joinable Game")).findFirst()
                .orElseThrow(() -> new Exception("Game not found"));

        // Join the game using its ID
        facade.joinGame(authToken, "WHITE", game.gameID());

        // If no exception is thrown, assume success
        assertTrue(true, "Player should be able to join an existing game");
    }

    // Negative test case: Test action on an unavailable game (e.g., joining a non-existent game)
    @Test
    void joinGameNonExistentGame() throws Exception {
        facade.register("player5", "password", "p5@email.com");
        AuthData authData = facade.login("player5", "password");  // Capture the AuthData
        String authToken = authData.authToken();  // Access the authToken directly since it's a field in the record
        Exception exception = assertThrows(Exception.class, () -> {
            facade.joinGame(authToken, "WHITE", 999);  // Try joining a non-existent game with ID 999
        });

        // Print out the exception and its message
        System.out.println("Exception class: " + exception.getClass().getName());
        System.out.println("Exception message: " + exception.getMessage());

        assertTrue(exception.getMessage().contains("HTTP error code: 400"), "Should throw an exception for a non-existent game");
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