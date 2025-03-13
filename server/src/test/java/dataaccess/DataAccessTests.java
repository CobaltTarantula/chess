package dataaccess;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class DataAccessTests {
    SQLUserDAO userDAO;
    SQLGameDAO gameDAO;
    SQLAuthDAO authDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();

        userDAO.removeAllUsers();
        gameDAO.removeAllGames();
        authDAO.removeAllAuthTokens();
    }

    @Nested
    class TestUserMethods {

        @Nested
        class CreateUserTests {
            @Test
            public void testCreateUserSuccess() throws DataAccessException, SQLException {
                UserData newUser = new UserData("testUser", "testPassword", "testEmail");
                userDAO.createUser(newUser);

                try (Connection conn = DatabaseManager.getConnection()) {
                    try (var preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                        preparedStatement.setString(1, newUser.username());
                        try (var rs = preparedStatement.executeQuery()) {
                            assertTrue(rs.next(), "User was not created");
                            assertEquals(newUser.username(), rs.getString("username"), "Username does not match");
                            assertEquals(newUser.email(), rs.getString("email"), "Email does not match");
                        }
                    }
                }
            }

            @Test
            public void testCreateUserFailure() throws DataAccessException {
                UserData newUser = new UserData("testUser", "testPassword", "testEmail");
                userDAO.createUser(newUser);

                assertThrows(DataAccessException.class, () -> userDAO.createUser(newUser));
            }
        }

        @Nested
        class GetUserTests {
            @Test
            public void testGetUserSuccess() throws DataAccessException {
                UserData newUser = new UserData("testUser", "testPassword", "testEmail");
                userDAO.createUser(newUser);

                UserData retrievedUser = userDAO.getUser(newUser.username(), newUser.password());

                assertEquals(newUser.username(), retrievedUser.username(), "Username does not match");
                assertEquals(newUser.email(), retrievedUser.email(), "Email does not match");
            }

            @Test
            public void testGetUserFailure() throws DataAccessException {
                UserData newUser = new UserData("testUser", "testPassword", "testEmail");
                userDAO.createUser(newUser);

                assertNull(userDAO.getUser("badUser", "testPassword"));

            }
        }
    }

    @ Nested
    class TestGameMethods {
        @Nested
        class CreateGameTests {

            @Test
            public void testCreateGameSuccess() throws DataAccessException, SQLException {
                gameDAO.createGame("testGame", 1);

                try (Connection conn = DatabaseManager.getConnection()) {
                    try (var preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameName = ?")) {
                        preparedStatement.setString(1, "testGame");
                        try (var rs = preparedStatement.executeQuery()) {
                            assertTrue(rs.next(), "Game was not created");
                            assertEquals("testGame", rs.getString("gameName"), "Game name does not match");
                        }
                    }
                }
            }

            @Test
            public void testCreateGameFailure() throws DataAccessException {
                gameDAO.createGame("testGame", 1);

                assertThrows(DataAccessException.class, () -> gameDAO.createGame("testGame", 1));
            }
        }

        @Nested
        class GetGameTests {

            @Test
            public void testGetGameSuccess() throws DataAccessException {
                GameData newGame = new GameData(1, null, null, "testGame", new ChessGame());
                gameDAO.createGame(newGame.gameName(), newGame.gameID());

                GameData savedGame = gameDAO.getGame(newGame.gameID());
                assertEquals(newGame.gameName(), savedGame.gameName(), "GetGame failure");
            }

            @Test
            public void testGetGameFailure() throws DataAccessException {
                GameData badGame = gameDAO.getGame(3);
                assertNull(badGame, "returned null");
            }
        }

        @Nested
        class ListGamesTests {

            @Test
            public void testListGamesSuccess() throws DataAccessException {
                GameData game1 = new GameData(1, null, null, "testGame1", new ChessGame());
                GameData game2 = new GameData(2, null, null, "testGame2", new ChessGame());
                GameData game3 = new GameData(3, null, null, "testGame3", new ChessGame());

                gameDAO.createGame(game1.gameName(), game1.gameID());
                gameDAO.createGame(game2.gameName(), game2.gameID());
                gameDAO.createGame(game3.gameName(), game3.gameID());

                Collection<GameData> expectedGames = new ArrayList<>();
                expectedGames.add(game1);
                expectedGames.add(game2);
                expectedGames.add(game3);

                assertEquals(expectedGames.size(), gameDAO.listGames().size(), "List of games does not match expected list");
            }

            @Test
            public void testListGamesFailure() throws DataAccessException {
                gameDAO.removeAllGames();

                Collection<GameData> retrievedGames = gameDAO.listGames();

                assertTrue(retrievedGames.isEmpty(), "List of games is not empty");
            }
        }

        @Nested
        class SaveGameTests {

            @Test
            public void testSaveGameSuccess() throws DataAccessException {
                GameData game = new GameData(1, null, null, "testGame", new ChessGame());
                gameDAO.createGame(game.gameName(), game.gameID());

                GameData newGame = new GameData(1, "testWhite", "testBlack", "testGame", new ChessGame());

                gameDAO.saveGame(newGame.gameID(), newGame);

                assertEquals(newGame.gameName(), gameDAO.getGame(newGame.gameID()).gameName(), "Game name does not match");
            }

            @Test
            public void testSaveGameFailure() throws DataAccessException {
                GameData badGame = new GameData(9999, null, null, "badGame", new ChessGame());
                gameDAO.saveGame(badGame.gameID(), badGame);
                assertNull(gameDAO.getGame(badGame.gameID()), "Game not saved");
            }
        }
    }

    @Nested
    class TestAuthMethods {
        @Nested
        class CreateAuthTests {
            @Test
            public void testCreateAuthSuccess() throws DataAccessException {
                String token = authDAO.createAuth("testUser");

                assertNotNull(token, "Auth token was not created");
            }
        }

        @Nested
        class GetAuthTests {
            @Test
            public void testGetAuthSuccess() throws DataAccessException {
                String token = authDAO.createAuth("testUser");

                String retrievedAuth = authDAO.getAuth(token);

                assertEquals(token, retrievedAuth, "auths do not match");
            }

            @Test
            public void testGetAuthFailure() throws DataAccessException {
                String badAuth = authDAO.getAuth("bad token");
                assertNull(badAuth, "returned null");

            }
        }

        @Nested
        class GetUsernameTests {
            @Test
            public void testGetUsernameSuccess() throws DataAccessException {
                String token = authDAO.createAuth("testUser");

                String retrievedUsername = authDAO.getUsername(token);

                assertEquals("testUser", retrievedUsername, "Username does not match");
            }

            @Test
            public void testGetUsernameFailure() throws DataAccessException {
                String badUsername = authDAO.getUsername("nonexistentUser");
                assertNull(badUsername, "returned null");
            }
        }

        @Test
        public void testDeleteAuth() throws DataAccessException {
            String token = authDAO.createAuth("testUser");

            authDAO.deleteAuth(token);

            String badAuth = authDAO.getAuth(token);
            assertNull(badAuth, "returned null");
        }
    }

    @Test
    public void testClear() throws DataAccessException {
        userDAO.createUser(new UserData("testUser", "testPassword", "testEmail"));
        gameDAO.createGame("testGame", 1);
        authDAO.createAuth("testUser");

        userDAO.removeAllUsers();
        gameDAO.removeAllGames();
        authDAO.removeAllAuthTokens();

        assertTrue(userDAO.isEmpty(), "Users table is not empty");
        assertTrue(gameDAO.isEmpty(), "Games table is not empty");
        assertTrue(authDAO.isEmpty(), "Auths table is not empty");
    }
}
