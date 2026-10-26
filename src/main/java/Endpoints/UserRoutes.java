package Endpoints;

import Controller.UserController;
import io.javalin.Javalin;

public class UserRoutes {

    public static void register(Javalin app, UserController userController){
        app.post("/users", userController::createUser);
        app.get("/users", userController::getAllUsers);
        app.get("/users/{id}", userController::getUserById);
        app.put("/users/{id}", userController::updateUser);
        app.delete("/users/{id}", userController::deleteUser);
    }
}
