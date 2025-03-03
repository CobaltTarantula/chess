package handlers;

import com.google.gson.*;
import dataaccess.DataAccessException;
import service.*;
import model.*;
import spark.*;

public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService service) {
        this.userService = service;
    }

    public void register(Request req, Response res) {
        try {
            UserData user = gson.fromJson(req.body(), UserData.class);
            userService.register(user);
            res.status(200);
            res.body(gson.toJson("User registered successfully"));
        } catch (DataAccessException e) {
            res.status(400);
            res.body(gson.toJson(e.getMessage()));
        }
    }
}
