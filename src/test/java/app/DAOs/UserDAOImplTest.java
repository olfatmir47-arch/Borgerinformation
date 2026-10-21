package app.DAOs;

import DAOs.AddressDAO;
import DAOs.CityDAO;
import DAOs.UserDAOImpl;
import Service.GeocodingService;
import Service.UserService;
import app.config.HibernateTestConfig;
import app.testUtils.UserTestPopulator;
import entities.Address;
import entities.City;
import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDAOImplTest {
    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();

    private UserService userService;
    private UserDAOImpl userDAO;
    private CityDAO cityDAO;
    private AddressDAO addressDAO;
    private GeocodingService geocodingService;
    private Map<String, User> seeded;


    @BeforeEach
    void beforeEach() {
        seeded = UserTestPopulator.populate(emf);
        userDAO = new UserDAOImpl(emf);
    }

    @AfterAll
    void shutdown() {
        emf.close();
    }

    @Test
    void createUser() {
        City city = new City("2000", "Frederiksberg");

        Address address = new Address();
        address.setStreetName("Allegade");
        address.setNumber("10");
        address.setFloor("");
        address.setLat(55.6797);
        address.setLng(12.5345);
        address.setCity(city);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate4 = LocalDate.parse("30-10-1989", formatter);

        User user = new User();
        user.setFirstName("Lars");
        user.setLastName("Larsen");
        user.setDateOfBirth(birthDate4);
        user.setEmail("lars.test@example.com");
        user.setPassword("testPassword123");
        user.setAddress(address);

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        em.persist(city);
        em.persist(address);

        em.getTransaction().commit();
        em.close();

        User created = userDAO.createUser(user);


        assertNotNull(created.getId());
        User fetched = userDAO.findById(created.getId());
        assertEquals("Lars", created.getFirstName());
        assertEquals("Larsen", created.getLastName());
        assertEquals("2000", created.getAddress().getCity().getPostalCode());
    }

    @Test
    void findById() {
        User seed = seeded.get("user1");
        User fetched = userDAO.findById(seed.getId());
        assertEquals(seed.getId(), fetched.getId());
        assertEquals(seed.getFirstName(), fetched.getFirstName());

    }

    @Test
    void findAllUsers() {

        List<User> all = userDAO.findAllUsers();

        assertThat(all, hasSize(3));
        assertThat(all.stream().map(User::getId).toList(), containsInAnyOrder(seeded.get("user1").getId(), seeded.get("user2").getId(), seeded.get("user3").getId()));

    }

    @Test
    void update() {
        User seed = seeded.get("user1");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate4 = LocalDate.parse("10-10-2010", formatter);
        User updated = User.builder()
                .id(seed.getId())
                .firstName("Lars Læsø")
                .lastName(seed.getLastName())
                .email(seed.getEmail())
                .password(seed.getPassword())
                .dateOfBirth(birthDate4)
                .address(seed.getAddress())
                .build();

        User result = userDAO.update(updated);

        assertThat(result.getId(), is(seed.getId()));
        assertThat(result.getFirstName(), is("Lars Læsø"));
        assertThat(result.getLastName(), is(seed.getLastName()));
        assertThat(result.getDateOfBirth(), is(birthDate4));
    }

    @Test
    void delete() {
        User seed = seeded.get("user3");

        boolean deleted = userDAO.delete(seed.getId());
    }

    @Test
    void findByEmail() {
    }

    @Test
    void getVerifiedUser() {
    }
}