package ui;

// imports here
import chess.ChessBoard;
import chess.ChessGame;
import client.ServerFacade;
import model.AuthData;
import model.GameData;

import java.io.PrintStream;
import java.util.*;

public class ConsoleUI {
    private final ServerFacade facade;
    PrintStream out;
    Scanner scanner;

    private AuthData auth;
    private ChessBoard board;
    private ChessGame game;
    private Map<Integer, GameData> numberedList;

    boolean loggedIn;
    boolean inGame;
    boolean isObserver;

    public ConsoleUI(ServerFacade facade){
        this.facade = facade;
        board = new ChessBoard();
        game = new ChessGame();
        loggedIn = false;
        inGame = false;
        isObserver = true;
        numberedList = new HashMap<>();
    }

    public void start() {
        // fix body
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
        out.println("register  - to create an account");
        out.println("login     - to play chess");
        out.println("quit      - playing chess");
        out.println("help      - with possible commands");
        if(loggedIn){
            out.println("logout  - when you are done");
            out.println("create - to make a new game to join");
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
            int id = facade.createGame(auth.authToken(), gameName);
            out.println("Game created");
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void listGames(){
        try {
            var gamesList = facade.listGames(auth.authToken());
            int i = 1;
            if (numberedList != null) numberedList.clear();
            out.println("Games:");
            for (var game : gamesList) {
                out.println(i + " -- Name: " + game.gameName() + ", White player: " + game.whiteUsername() + ", Black player: " + game.blackUsername());
                numberedList.put(i, game);
                i++;
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void playGame(){

    }

    private void observeGame(){

    }
}
