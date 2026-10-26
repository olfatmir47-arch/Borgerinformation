package Endpoints;

import Controller.CategoryController;
import Controller.UserController;
import io.javalin.Javalin;

public class CategoryRoutes {

    public static void register(Javalin app, CategoryController categoryController) {

        app.post("/adminPage", categoryController::createCategory);
    }
}
