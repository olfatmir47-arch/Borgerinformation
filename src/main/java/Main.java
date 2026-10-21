import Controller.CategoryController;
import Controller.EventController;
import Controller.GeocodingController;
import Controller.UserController;
import DAOs.*;
import DTO.Address.AddressDTO;
import Service.CategoryService;
import Service.EventService;
import Service.GeocodingService;
import Service.UserService;
import app.config.HibernateConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import jakarta.persistence.EntityManagerFactory;

import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws UnknownHostException {

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

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Javalin app = Javalin.create(config ->{config.jsonMapper(new JavalinJackson());}).start(7070);
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
        });

        UserController userController = new UserController(userService);
        CategoryController categoryController = new CategoryController(categoryService);
        EventController eventController = new EventController(eventService);

        app.post("/users", userController::createUser);
        app.get("/users", userController::getAllUsers);
        app.get("/users/{id}", userController::getUserById);
        app.put("/users/{id}", userController::updateUser);
        app.delete("/users/{id}", userController::deleteUser);


        app.post("/adminPage", categoryController::createCategory);

        app.post("/events", eventController::createEvent);
        app.get("/events/category/{id}", eventController::getAllEventsByCategory);


        //CoordinatesDTO coordinatesDTO = geocodingController.geocode("Stærevej 61, København");
        //System.out.println(coordinatesDTO.getLat() + ", " + coordinatesDTO.getLng());


        AddressDTO addressDTO = geocodingController.reverseGeocode(55.70175, 12.52267);
        System.out.println(addressDTO.getStreetName() +" " + addressDTO.getNumber() + ", " + addressDTO.getPostalCode() + " " + addressDTO.getCity());

    }
}
