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
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void resetState() throws IOException {
        facade.clear();
    }

    @Nested
    class RegisterTests {
        @Test
        void registerSuccess() throws Exception {
            AuthData auth =  facade.register("testUser", "testPassword", "testEmail");
            Assertions.assertNotNull(auth, "Registration failed");
            Assertions.assertTrue(auth.authToken().length() > 10);
        }

        @Test
        void registerDuplicateUsername() throws Exception {
            facade.register("testUser", "testPassword", "testEmail@example.com");

            Assertions.assertThrows(IOException.class, () ->
                    facade.register("testUser", "newPassword", "newEmail@example.com"));
        }
    }

    @Nested
    class LoginTests {
        @Test
        void loginSuccess() throws Exception {
            facade.register("player2", "password", "p2@email.com");
            var authData = facade.login("player2", "password");
            assertNotNull(authData.authToken(), "Auth token should not be null after successful login");
            assertTrue(authData.authToken().length() > 10, "Auth token should be long enough");
        }

        @Test
        void loginFailure() throws Exception {
            facade.register("testUser", "testPassword", "testEmail");

            Assertions.assertThrows(IOException.class, () -> facade.login("testUser", "badPassword"));
        }
    }

    @Nested
    class CreateGameTests {
        @Test
        void createGameSuccess() throws Exception {
            AuthData auth = facade.register("testUser", "testPassword", "testEmail");
            Assertions.assertNotNull(auth, "Registration failed");

            int id;
            id = facade.createGame(auth.authToken(), "testGame");
            assertTrue(id > 0);
        }

        @Test
        void createGameInvalidAuth() throws Exception {
            AuthData auth = facade.register("testUser", "testPassword", "testEmail");
            Assertions.assertNotNull(auth, "Registration failed");
            facade.createGame(auth.authToken(), "testGame");
            Assertions.assertThrows(IOException.class,
                    () -> facade.createGame(auth.authToken(), "testGame"));
        }
    }

    @Nested
    class ListGamesTests {
        @Test
        void listGamesSuccess() throws Exception {
            facade.register("player4", "password", "p4@email.com");
            var authData = facade.login("player4", "password");

            String authToken = authData.authToken();
            facade.createGame(authToken, "Test Game");
            var games = facade.listGames(authToken);

            assertNotNull(games, "Game list should not be null");
            assertFalse(games.isEmpty(), "Game list should have at least one game");
        }

        @Test
        void listGamesFailure()  {
            Assertions.assertThrows(IOException.class, () -> facade.listGames("badAuthToken"));
        }
    }

    @Nested
    class JoinGameTests {
        @Test
        void joinGameSuccess() throws Exception {
            var authData = facade.register("player7", "password", "p7@email.com");
            String authToken = authData.authToken();

            facade.createGame(authToken, "Joinable Game");

            var games = facade.listGames(authToken);
            var game = games.stream().filter(g -> g.gameName().equals("Joinable Game")).findFirst()
                    .orElseThrow(() -> new Exception("Game not found"));

            facade.joinGame(authToken, "WHITE", game.gameID());

            assertTrue(true, "Player should be able to join an existing game");
        }

        @Test
        void joinGameNonExistentGame() throws Exception {
            facade.register("player5", "password", "p5@email.com");
            AuthData authData = facade.login("player5", "password");
            String authToken = authData.authToken();
            Exception exception = assertThrows(Exception.class, () -> facade.joinGame(authToken, "WHITE", 999));

            System.out.println("Exception class: " + exception.getClass().getName());
            System.out.println("Exception message: " + exception.getMessage());

            assertTrue(exception.getMessage().contains("HTTP error code: 400"),
                    "Should throw an exception for a non-existent game");
        }
    }

    @Nested
    class LogoutTests{
        @Test
        void logoutSuccess() throws Exception {
            AuthData auth = facade.register("testUser", "testPassword", "testEmail");
            Assertions.assertNotNull(auth, "Registration failed");
            facade.login("testUser", "testPassword");
            int responseCode = facade.logout(auth.authToken());
            assertEquals(200, responseCode);
        }
        @Test
        void logoutFailure() {
            Assertions.assertThrows(Exception.class, () -> facade.logout("invalidAuthToken"));
        }
    }
}