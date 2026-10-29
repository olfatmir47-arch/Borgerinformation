package Endpoints;

import Controller.CategoryController;
import Controller.UserController;
import entities.Role;
import io.javalin.Javalin;

public class CategoryRoutes {

    public static void register(Javalin app, CategoryController categoryController) {

        app.post("/adminPage", categoryController::createCategory, Role.ADMIN);
        app.delete("/categories/{id}", categoryController::deleteCategory, Role.ADMIN);
    }
}
