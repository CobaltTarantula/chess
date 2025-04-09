package ui;

import chess.ChessGame;
import client.ServerFacade;
import model.AuthData;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.ERASE_SCREEN;

public class ConsoleUI {
    private final ServerFacade facade;
    PrintStream out;
    Scanner scanner;

    private AuthData auth;
    private ChessGame game;
    private final Map<Integer, GameData> numberedList;

    boolean loggedIn;
    boolean inGame;
    boolean isObserver;

    public ConsoleUI(ServerFacade facade){
        this.facade = facade;
        game = new ChessGame();
        loggedIn = false;
        inGame = false;
        isObserver = true;
        numberedList = new HashMap<>();
    }

    public void start() {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String input = "";
        scanner = new Scanner(System.in);

        out.print(ERASE_SCREEN);
        out.println("♕ Welcome to 240 chess. Type 'help' to get started. ♕");

        while (!Objects.equals(input, "quit")) {
            if (!(loggedIn)) {
                out.print("[LOGGED_OUT] >>> ");
                input = scanner.nextLine();
                preLogin(input);
            } else {
                out.print("[LOGGED_IN] >>> ");
                input = scanner.nextLine();
                postLogin(input);
            }
        }
        out.println("Hope you had fun, bye!");
    }

    private boolean isInList(String input) {
        int gameNum;
        try {
            gameNum = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            out.println("Invalid input. Please enter a number.");
            return false;
        }

        if (!numberedList.containsKey(gameNum)) {
            out.println("That game doesn't exist. Please enter a valid number (use 'list' to see available games).");
            return false;
        }
        return true;
    }

    private void preLogin(String input){
        switch (input) {
            case "help":
                help();
                break;
            case "register":
                register();
                break;
            case "login":
                login();
                break;
            case "exit", "quit":
                break;
            default:
                out.println("Invalid command. Type 'help' for possible commands.");
        }
    }

    private void postLogin(String input){
        switch (input) {
            case "logout":
                logout();
                break;
            case "create":
                createGame();
                break;
            case "list":
                listGames();
                break;
            case "play":
                playGame();
                break;
            case "observe":
                observeGame();
                break;
            case "help":
                help();
                break;
            case "exit", "quit":
                break;
            default:
                out.println("Invalid command. Type 'help' for possible commands.");
        }
    }

    private void help(){
        out.println("Available Commands:");
        out.println("- register   : Create a new account");
        out.println("- login      : Log into your account");
        out.println("- quit       : Exit the program");
        out.println("- help       : Display available commands");
        if (loggedIn) {
            out.println("- logout     : Log out of your account");
            out.println("- create     : Create a new chess game");
            out.println("- list       : Show available games");
            out.println("- play       : Join a game as a player");
            out.println("- observe    : Watch a game in progress");
        }
    }

    private void login(){
        out.print("Enter username: ");
        String username = scanner.nextLine();
        out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            auth = facade.login(username, password);
            out.println("Login successful. Hello, " + username + "!");
            loggedIn = true;
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void register(){
        out.println("Enter new username: ");
        String username = scanner.nextLine();
        out.println("Enter new password: ");
        String password = scanner.nextLine();
        out.println("Enter new email: ");
        String email = scanner.nextLine();

        try {
            auth = facade.register(username, password, email);
            out.println("Registration successful.  Hello, " + username + "!");
            loggedIn = true;
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void logout(){
        if (!loggedIn) {
            out.println("You are not logged in.");
            return;
        }
        try {
            facade.logout(auth.authToken());
            out.println("Successfully logged out.");
            loggedIn = false;
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void createGame(){
        out.print("Enter a name for the new game: ");
        String gameName = scanner.nextLine();
        try {
            facade.createGame(auth.authToken(), gameName);
            out.println("Game created");
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void listGames(){
        try {
            var gamesList = facade.listGames(auth.authToken());
            int i = 1;
            numberedList.clear();
            out.println("Games:");
            for (var game : gamesList) {
                out.println(i + " -- Name: " + game.gameName() + ", White player: " + game.whiteUsername() +
                        ", Black player: " + game.blackUsername());
                numberedList.put(i, game);
                i++;
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void playGame(){
        out.print("Enter the number of the game you'd like to join: ");
        String input = scanner.nextLine();

        if (isInList(input)) {
            int gameNum = Integer.parseInt(input);
            GameData listedGame = numberedList.get(gameNum);
            int gameID = listedGame.gameID();

            out.print("Please enter your desired team (WHITE or BLACK): ");
            String givenTeam = scanner.nextLine();
            String team = givenTeam.toUpperCase();

            if (!team.equals("WHITE") && !team.equals("BLACK")) {
                out.println("Invalid team. Please enter either 'WHITE' or 'BLACK'.");
                return;
            }

            try {
                facade.joinGame(auth.authToken(), team, gameID);
                out.println("Joined ["+ listedGame.gameName() + "] as the " + team + " player");

                game = listedGame.game();
                this.inGame = true;

                ChessGame.TeamColor playerColor;
                if (team.equals("WHITE")){
                    playerColor = ChessGame.TeamColor.WHITE;
                    new BoardUI(out, game).printBoard(playerColor);
                }
                else{
                    playerColor = ChessGame.TeamColor.BLACK;
                    new BoardUI(out, game).printBoard(playerColor);
                }

            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }
    }

    private void observeGame(){
        out.print("Enter the number of the game you'd like to observe: ");
        String input = scanner.nextLine();

        if (isInList(input)) {
            int gameNum = Integer.parseInt(input);
            GameData listedGame = numberedList.get(gameNum);
            game = listedGame.game();

            new BoardUI(out, game).printBoard(WHITE);
        }
    }
}
