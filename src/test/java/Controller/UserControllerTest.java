package Controller;

import app.config.AppConfig;
import app.config.HibernateTestConfig;
import app.testUtils.UserTestPopulator;
import entities.User;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.json.JavalinJackson;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;


import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserControllerTest {
    private static final String ENDPOINT_URL = "/users";
    private static EntityManagerFactory emf;
    private static Javalin app;
    private Map<String, User> seeded;
    private String adminToken;

    @BeforeAll
    static void startServer() {
        emf = HibernateTestConfig.getEntityManagerFactory();
        app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
        }).start(7070);
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
        });

        AppConfig.configure(app, emf);
        app.get("/test", ctx -> ctx.result("Server works"));
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7070;
        RestAssured.basePath = "";
    }

    @BeforeEach
    void beforeEach() {
        seeded = UserTestPopulator.populate(emf);

        adminToken = given()
                .contentType(ContentType.JSON)
                .body("""
                        {"email":"admin@example.com","password":"adminPass123"}
                        """)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @AfterAll
    static void shutdown() {
        emf.close();
    }

    @Test
    void getUserById() {

        User expected = seeded.get("user1");
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/users/" + expected.getId())
                .then()
                .statusCode(200)
                .body("firstName", equalTo(expected.getFirstName()));
    }

    @Test
    void createUser() {
        String body = """
                {
                  "firstName": "Anna",
                  "lastName": "Jensen",
                  "birthDate": "15-05-1995",
                  "email": "anna.jensen@example.com",
                  "password": "annaPass95",
                  "address": {
                    "streetName": "Julius Valentiners Vej",
                    "number": "24",
                    "floor": "",
                    "postalCode": "2000",
                    "cityName": "Frederiksberg" }
                  }
                }
                """;
                given()
                        .contentType(ContentType.JSON)
                        .body(body)
                        .when()
                        .post("/users/")
                        .then()
                        .statusCode(200)
                        .body("firstName", equalTo("Anna"))
                        .body("lastName", equalTo("Jensen"))
                        .body("email", equalTo("anna.jensen@example.com"))
                        .extract()
                        .path("id");

    }


    @Test
    void getAllUsers() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(4))
                .body("firstName", hasItems("Lars", "Admin"));
    }

    @Test
    void updateUser() {
        User expected = seeded.get("user1");

        String body = """
        {
          "firstName": "Anna",
          "lastName": "Jensen",
          "birthDate": "15-05-1995",
          "email": "anna.updated@example.com",
          "password": "annaPass95",
          "address": {
            "streetName": "Julius Valentiners Vej",
            "number": "24",
            "floor": "",
            "postalCode": "2000",
            "cityName": "Frederiksberg"
          }
        }
        """;

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/users/" + expected.getId())
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Anna"))
                .body("lastName", equalTo("Jensen"))
                .body("email", equalTo("anna.updated@example.com"));
    }

    @Test
    void deleteUser() {
        User user = seeded.get("user1");

        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .delete("/users/" + user.getId())
                .then()
                .statusCode(204);
    }
}