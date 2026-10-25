package DAOs;

import app.config.HibernateConfig;
import app.exceptions.ApiException;
import entities.Category;
import entities.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class EventDAOImpl implements OccurrenceDAO<Event> {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Event create(Event event) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    em.persist(event);
    em.getTransaction().commit();
    em.close();
    return event;
    }

    @Override
    public Event findById(int id) {
        EntityManager em = emf.createEntityManager();
        Event foundEvent = em.find(Event.class, id);
        em.close();
        return foundEvent;
    }

    @Override
    public List<Event> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e", Event.class);
                return query.getResultList();
            } catch (PersistenceException e) {
                throw new ApiException(500, "Get events failed: " + e.getMessage());
            }
        }
    }

    @Override
    public Event update(Event event) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Event updatedEvent = em.merge(event);
        em.getTransaction().commit();
        em.close();
        return updatedEvent;
    }


    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Event event = findById(id);
        if (event != null)
        {
            em.remove(event);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Event> findByPostalCode(String postalCode) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Event> query = em.createQuery(
                "SELECT e from Event e WHERE e.address.city.postalCode = :postalCode", Event.class);
        query.setParameter("postalCode", postalCode);
        List<Event> events = query.getResultList();
        em.close();

        return events;
    }

    @Override
    public Set<Event> findByCity(String cityName) {
        return Set.of();
    }

    @Override
    public List<Event> findByCategory(int categoryId) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT e FROM Event e WHERE e.category.id = :categoryId", Event.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
    }

    @Override
    public Set<Event> findByDate(LocalDate startDate, LocalDate endDate) {
        return Set.of();
    }

    @Override
    public Set<Event> findByType(Event object) {
        return Set.of();
    }
}
