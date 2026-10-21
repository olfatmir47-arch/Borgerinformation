package DAOs;

import app.config.HibernateConfig;
import entities.City;
import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CityDAO {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public City findById(String id) {
        EntityManager em = emf.createEntityManager();
        City foundCity = em.find(City.class, id);
        return foundCity;
    }

    public City findByPostalCode(String postalCode) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<City> query = em.createQuery(
                    "SELECT c FROM City c WHERE c.postalCode = :postalCode",
                    City.class
            );

            query.setParameter("postalCode", postalCode);

            List<City> results = query.getResultList();

            return results.isEmpty() ? null : results.get(0);
        }
    }

    public City createCity(City city) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(city);
        em.getTransaction().commit();
        em.close();
        return city;
    }
}
