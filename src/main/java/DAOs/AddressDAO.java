package DAOs;

import app.config.HibernateConfig;
import entities.Address;
import entities.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.sql.PreparedStatement;
import java.util.List;


public class AddressDAO {

    private EntityManagerFactory emf;

    public AddressDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    public Address createAddress(Address address) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(address);
        em.getTransaction().commit();
        em.close();
        return address;
    }

    public Address findAddress(String streetName, String number, String floor, City city) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery(
                    "SELECT a FROM Address a " +
                            "WHERE a.streetName = :streetName " +
                            "AND a.number = :number " +
                            "AND a.floor = :floor " +
                            "AND a.city.id = :cityId",
                    Address.class
            );
            query.setParameter("streetName", streetName);
            query.setParameter("number", number);
            query.setParameter("floor", floor);
            query.setParameter("cityId", city.getId());

            List<Address> results = query.getResultList();

            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return null;
        }finally {
            em.close();
        }
    }
}
