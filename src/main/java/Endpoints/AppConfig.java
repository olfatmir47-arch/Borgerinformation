package Endpoints;

import Controller.*;
import DAOs.*;
import Service.CategoryService;
import Service.EventService;
import Service.GeocodingService;
import Service.UserService;
import app.config.HibernateConfig;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class AppConfig {

    public static void configure(Javalin app) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        GeocodingController geocodingController = new GeocodingController();

        UserDAOImpl userDAO = new UserDAOImpl(emf);
        CityDAO cityDAO = new CityDAO();
        AddressDAO addressDAO = new AddressDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        EventDAOImpl eventDAO = new EventDAOImpl();

        GeocodingService geocodingService = new GeocodingService();
        EventService eventService = new EventService(eventDAO, cityDAO, addressDAO, geocodingService, categoryDAO);
        UserService userService = new UserService(userDAO, cityDAO, addressDAO, geocodingService);
        CategoryService categoryService = new CategoryService(categoryDAO);

        UserController userController = new UserController(userService);
        CategoryController categoryController = new CategoryController(categoryService);
        EventController eventController = new EventController(eventService);
        SecurityController securityController = new SecurityController();

        app.beforeMatched(securityController::authenticate);
        app.beforeMatched(securityController::authorize);

        UserRoutes.register(app, userController);
        CategoryRoutes.register(app, categoryController);
        EventRoutes.register(app, eventController);
        SecurityRoutes.register(app, securityController);
    }
}