package ui;

// imports here
import client.ServerFacade;
import model.AuthData;

import java.io.PrintStream;
import java.util.*;

public class ConsoleUI {
    private final ServerFacade facade;
    PrintStream out;
    Scanner scanner;
    private AuthData auth;
    boolean loggedIn;

    public ConsoleUI(ServerFacade facade, ServerFacade facade1){
        this.facade = facade1;
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
        System.out.println("register  - to create an account");
        System.out.println("login     - to play chess");
        System.out.println("quit      - playing chess");
        System.out.println("help      - with possible commands");
        if(loggedIn){
            System.out.println("logout  - when you are done");
            System.out.println("create - to make a new game to join");
        }
    }

    private void login(){
        out.print("Enter username: ");
        String username = scanner.nextLine();
        out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            auth = facade.login(username, password);
            out.println("Login successful.  Hello, " + username + "!");
            loggedIn = true;
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void register(){

    }

    private void logout(){

    }

    private void createGame(){

    }

    private void listGames(){

    }

    private void playGame(){

    }

    private void observeGame(){

    }
}
