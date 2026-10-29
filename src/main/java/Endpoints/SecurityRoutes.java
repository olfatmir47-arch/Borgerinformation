package Endpoints;

import Controller.SecurityController;
import entities.Role;
import io.javalin.Javalin;

public class SecurityRoutes {
    public static void register(Javalin app, SecurityController securityController){
        app.post("/auth/login", securityController::login, Role.EVERYONE);
    }
}
