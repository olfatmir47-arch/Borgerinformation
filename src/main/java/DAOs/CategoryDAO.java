package DAOs;

import app.config.HibernateConfig;
import entities.Category;
import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class CategoryDAO {
private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


public Category create(Category category) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    em.persist(category);
    em.getTransaction().commit();
    em.close();
    return category;
}
    public Category findById(int id) {
        EntityManager em = emf.createEntityManager();
        Category foundCategory = em.find(Category.class, id);
        em.close();
        return foundCategory;

    }


    public Category findByName(String name) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT c FROM Category c WHERE c.name = :name", Category.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);

    }

    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Category category = findById(id);
        if (category != null)
        {
            em.remove(category);
        }
        em.getTransaction().commit();
        em.close();
    }

}
