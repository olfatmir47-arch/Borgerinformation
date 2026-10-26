package Endpoints;

import Controller.CategoryController;
import Controller.EventController;
import io.javalin.Javalin;

public class EventRoutes {

    public static void register(Javalin app, EventController eventController) {

        app.post("/events", eventController::createEvent);
        app.get("/events", eventController::GetAllEvents);
        app.get("/events/category/{id}", eventController::getAllEventsByCategory);
    }
}
