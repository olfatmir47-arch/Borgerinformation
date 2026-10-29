package Endpoints;

import Controller.UserController;
import entities.Role;
import io.javalin.Javalin;

public class UserRoutes {

    public static void register(Javalin app, UserController userController){

        app.post("/users", userController::createUser, Role.EVERYONE);
        app.get("/users", userController::getAllUsers, Role.ADMIN);
        app.get("/users/{id}", userController::getUserById, Role.ADMIN);
        app.put("/users/{id}", userController::updateUser, Role.USER, Role.ADMIN);
        app.delete("/users/{id}", userController::deleteUser, Role.USER, Role.ADMIN);
        app.get("/users/test", ctx -> {
            System.out.println("User test route called");
            ctx.result("User route works");
        });
    }
}
