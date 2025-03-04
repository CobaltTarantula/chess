package service;

import model.*;
//import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import dataaccess.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceTests {

    @Test
    public void testClearService() throws DataAccessException {
        UserDAO users = new MemUserDAO();
        GameDAO games = new MemGameDAO();
        AuthDAO auths = new MemAuthDAO();

        users.createUser(new UserData("testUser", "testPassword", "testEmail"));
        games.createGame("testGame", 1);
        auths.createAuth("testUsername");

        ClearService testClearService = new ClearService(users, games, auths);
        if (testClearService.clear() == null) {
            System.out.println("Data Cleared Successfully!");
        } else {
            System.out.println("Error: data not cleared.");
        }
    }

    @Nested
    class testRegister {
        @Test
        public void testIncorrectInput() {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            Exception exception = assertThrows(DataAccessException.class, () -> {
                userService.register(new UserData("no password giver", null, "IhatePasswords@live.com"));
            });
            assertEquals("bad request", exception.getMessage());
        }

        @Test
        public void testUsernameTaken() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();

            users.createUser(new UserData("copycat", "testPassword", "testEmail"));

            UserService userService = new UserService(auths, users);
            Exception exception = assertThrows(DataAccessException.class, () -> {
                userService.register(new UserData("copycat", "testPassword", "testEmail"));
            });
            assertEquals("already taken", exception.getMessage());
        }

        @Test
        public void testRegisterSuccess() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            AuthData testAuthData = userService.register(new UserData("testUser", "testPassword", "testEmail"));
            System.out.println("AuthData: " + testAuthData);
        }

    }

    @Nested
    class testLogin {
        @Test
        public void testBadPassword() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();

            users.createUser(new UserData("testUser", "testPassword", "testEmail"));

            UserService userService = new UserService(auths, users);
            Exception exception = assertThrows(DataAccessException.class, () -> {
                userService.loginUser(new UserData("testUser", "wrongPassword", "testEmail"));
            });
            assertEquals("unauthorized", exception.getMessage());
        }

        @Test
        public void testBadUsername() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();

            users.createUser(new UserData("testUser", "testPassword", "testEmail"));

            UserService userService = new UserService(auths, users);
            Exception exception = assertThrows(DataAccessException.class, () -> {
                userService.loginUser(new UserData("testUser", "wrongPassword", "testEmail"));
            });
            assertEquals("unauthorized", exception.getMessage());
        }

        @Test
        public void testEmptyFields() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();

            users.createUser(new UserData("testUser", "testPassword", "testEmail"));

            UserService userService = new UserService(auths, users);
            Exception exception = assertThrows(DataAccessException.class, () -> {
                userService.loginUser(new UserData("testUser", null, "testEmail"));
            });
            assertEquals("must fill all fields", exception.getMessage());
        }

        @Test
        public void testLoginSuccess() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();

            users.createUser(new UserData("testUser", "testPassword", "testEmail"));

            UserService userService = new UserService(auths, users);
            AuthData testAuthData = userService.loginUser(new UserData("testUser", "testPassword", "testEmail"));
            System.out.println("AuthData: " + testAuthData);
        }

    }

    @Nested
    class testLogout {
        @Test
        public void testLogoutSuccess() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            UserData testUser = new UserData("testUser", "testPassword", "testEmail");
            userService.register(testUser);

            AuthData testToken = userService.loginUser(testUser);

            if (auths.verifyAuth(testToken.authToken())) System.out.println("Logout Successful!");
        }

        @Test
        public void testBadAuth() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            UserData testUser = new UserData("testUser", "testPassword", "testEmail");
            userService.register(testUser);

//            AuthData testToken = userService.loginUser(testUser);

            Exception exception = assertThrows(DataAccessException.class, () -> {
                userService.logoutUser("badToken");
            });
            assertEquals("unauthorized", exception.getMessage());
        }
    }

    @Nested
    class testCreateGame {
        @Test
        public void testCreateGameSuccess() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            GameDAO games = new MemGameDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            UserData testUser = new UserData("testUser", "testPassword", "testEmail");
            userService.register(testUser);

            AuthData testToken = userService.loginUser(testUser);
            String token = testToken.authToken();

            GameService gameService = new GameService(auths, games);
            Integer gameID = -1;
            gameID = gameService.createGame(token, "testGame");

            if (gameID == -1) System.out.println("ERROR: Game not created");
            else System.out.println("SUCCESS! :: GameID: " + gameID);
        }

        @Test
        public void testDuplicateGame() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            GameDAO games = new MemGameDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            UserData testUser = new UserData("testUser", "testPassword", "testEmail");
            userService.register(testUser);

            AuthData testToken = userService.loginUser(testUser);
            String token = testToken.authToken();


            GameService gameService = new GameService(auths, games);

            gameService.createGame(token, "testGame");

            Exception exception = assertThrows(DataAccessException.class, () -> {
                gameService.createGame(token, "testGame");
            });
            assertEquals("game already exists", exception.getMessage());
        }
    }

    @Nested
    class testListGame {
        @Test
        public void testListGameSuccess() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            GameDAO games = new MemGameDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            UserData testUser = new UserData("testUser", "testPassword", "testEmail");
            userService.register(testUser);

            AuthData testToken = userService.loginUser(testUser);
            String token = testToken.authToken();


            GameService gameService = new GameService(auths, games);

            gameService.createGame(token, "testGame1");
            gameService.createGame(token, "testGame2");
            gameService.createGame(token, "testGame3");

            System.out.println("Games: " + gameService.listGames(token));
        }

        @Test
        public void testListGameUnauthorized() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            GameDAO games = new MemGameDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            UserData testUser = new UserData("testUser", "testPassword", "testEmail");
            userService.register(testUser);

            AuthData testToken = userService.loginUser(testUser);
            String token = testToken.authToken();


            GameService gameService = new GameService(auths, games);

            gameService.createGame(token, "testGame1");
            gameService.createGame(token, "testGame2");
            gameService.createGame(token, "testGame3");

            Exception exception = assertThrows(DataAccessException.class, () -> {
                gameService.listGames("badToken");
            });
            assertEquals("unauthorized", exception.getMessage());
        }
    }

    @Nested
    class testJoinGame {
        @Test
        public void testJoinGameSuccess() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            GameDAO games = new MemGameDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            UserData testUser = new UserData("testUser", "testPassword", "testEmail");
            userService.register(testUser);

            AuthData testToken = userService.loginUser(testUser);
            String token = testToken.authToken();


            GameService gameService = new GameService(auths, games);

            Integer gameID = gameService.createGame(token, "testGame");

            GameData testGameData = gameService.joinGame(token,"WHITE",gameID);
            System.out.println("GameData: " + testGameData);
        }

        @Test
        public void testColorAlreadyTaken() throws DataAccessException {
            UserDAO users = new MemUserDAO();
            GameDAO games = new MemGameDAO();
            AuthDAO auths = new MemAuthDAO();
            UserService userService = new UserService(auths, users);

            UserData testUser = new UserData("testUser", "testPassword", "testEmail");
            UserData testUser2 = new UserData("testUser2", "testPassword2", "testEmail2");
            userService.register(testUser);
            userService.register(testUser2);

            AuthData testToken = userService.loginUser(testUser);
            String token = testToken.authToken();

//            AuthData TestToken2 = userService.loginUser(testUser2);
            String token2 = testToken.authToken();


            GameService gameService = new GameService(auths, games);

            Integer gameID = gameService.createGame(token, "testGame");
            gameService.joinGame(token, "WHITE", gameID);

            Exception exception = assertThrows(DataAccessException.class, () -> {
                gameService.joinGame(token2, "WHITE", gameID);
            });
            assertEquals("already taken", exception.getMessage());
        }
    }
}