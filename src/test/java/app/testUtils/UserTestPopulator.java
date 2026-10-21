package app.testUtils;

import DTO.Address.CreateAddressDTO;
import DTO.User.CreateUserDTO;
import entities.Address;
import entities.City;
import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public final class UserTestPopulator {

    private UserTestPopulator() {}

    public static Map<String, User> populate(EntityManagerFactory emf) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

// --- City entities ---

            City city1 = new City("2000", "Frederiksberg");
            City city2 = new City("2200", "København N");
            City city3 = new City("5000", "Odense C");

// --- Address entities ---

            Address address1 = new Address();
            address1.setStreetName("Allegade");
            address1.setNumber("10");
            address1.setFloor("");
            address1.setLat(55.6797);
            address1.setLng(12.5345);
            address1.setCity(city1);

            Address address2 = new Address();
            address2.setStreetName("Nørrebrogade");
            address2.setNumber("55");
            address2.setFloor("2.th");
            address2.setLat(55.6915);
            address2.setLng(12.5532);
            address2.setCity(city2);

            Address address3 = new Address();
            address3.setStreetName("Vestergade");
            address3.setNumber("12");
            address3.setFloor("1.tv");
            address3.setLat(55.4038);
            address3.setLng(10.3876);
            address3.setCity(city3);

// --- User entities ---

            User user1 = new User();
            user1.setFirstName("Lars");
            user1.setLastName("Larsen");
            user1.setDateOfBirth(
                    LocalDate.parse("04-06-1998", formatter)
            );
            user1.setEmail("lars.larsen98@example.com");
            user1.setPassword("123456LL");
            user1.setAddress(address1);

            User user2 = new User();
            user2.setFirstName("Sara");
            user2.setLastName("Hansen");
            user2.setDateOfBirth(
                    LocalDate.parse("12-03-1992", formatter)
            );
            user2.setEmail("sara.hansen92@example.com");
            user2.setPassword("saraPass92");
            user2.setAddress(address2);

            User user3 = new User();
            user3.setFirstName("Mikkel");
            user3.setLastName("Poulsen");
            user3.setDateOfBirth(
                    LocalDate.parse("28-11-1985", formatter)
            );
            user3.setEmail("mikkel.poulsen85@example.com");
            user3.setPassword("mikkel1985");
            user3.setAddress(address3);

            try {
                em.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
                em.persist(city1);
                em.persist(city2);
                em.persist(city3);

                em.persist(address1);
                em.persist(address2);
                em.persist(address3);

                em.persist(user1);
                em.persist(user2);
                em.persist(user3);

                em.flush();
            } catch (PersistenceException e) {
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
            em.getTransaction().commit();

            Map<String, User> seeded = new LinkedHashMap<>();
            seeded.put("user1", user1);
            seeded.put("user2", user2);
            seeded.put("user3", user3);
            return seeded;
        }
    }
}
