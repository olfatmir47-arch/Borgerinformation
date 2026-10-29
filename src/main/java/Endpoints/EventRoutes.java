package Endpoints;

import Controller.CategoryController;
import Controller.EventController;
import entities.Role;
import io.javalin.Javalin;

public class EventRoutes {

    public static void register(Javalin app, EventController eventController) {

        app.post("/events", eventController::createEvent, Role.USER, Role.ADMIN);
        app.get("/events", eventController::GetAllEvents, Role.USER, Role.ADMIN);
        app.get("/events/category/{id}", eventController::getAllEventsByCategory, Role.USER, Role.ADMIN);
        app.get("events/getByPostalCode/{postalCode}", eventController::getEventsByPostalCode, Role.USER, Role.ADMIN);
    }
}
