package DAOs;

import app.config.HibernateConfig;
import app.exceptions.ApiException;
import entities.Category;
import entities.Incident;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class IncidentDAOImpl implements OccurrenceDAO<Incident> {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Incident create(Incident incident) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(incident);
        em.getTransaction().commit();
        em.close();
        return incident;
    }

    @Override
    public Incident findById(int id) {
        EntityManager em = emf.createEntityManager();
        Incident foundIncident = em.find(Incident.class, id);
        em.close();
        return foundIncident;
    }

    @Override
    public List findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                TypedQuery<Incident> query = em.createQuery("SELECT i FROM Incident i", Incident.class);
                return query.getResultList();
            } catch (PersistenceException e) {
                throw new ApiException(500, "Get studies failed: " + e.getMessage());
            }
        }
    }


    @Override
    public Incident update(Incident incident) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Incident updatedIncident = em.merge(incident);
        em.getTransaction().commit();
        em.close();
        return updatedIncident;
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Incident incident = findById(id);
        if (incident != null)
        {
            em.remove(incident);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Incident> findByPostalCode(String postalCode) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Incident> query = em.createQuery(
                "SELECT i from Incident i WHERE i.address.city.postalCode = :postalCode", Incident.class);
        query.setParameter("postalCode", postalCode);
        List<Incident> incidents = query.getResultList();
        em.close();

        return incidents;
    }

    @Override
    public Set<Incident> findByCity(String cityName) {
        return Set.of();
    }

    @Override
    public List<Incident> findByCategory(int categoryId) {
        return List.of();
    }

    @Override
    public Set<Incident> findByDate(LocalDate startDate, LocalDate endDate) {
        return Set.of();
    }

    @Override
    public Set<Incident> findByType(Incident object) {
        return Set.of();
    }
}
