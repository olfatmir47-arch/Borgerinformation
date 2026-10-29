package DAOs;

import DTO.Security.JwtUserDTO;
import entities.Role;
import entities.User;
import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    private final EntityManagerFactory emf;

    public UserDAOImpl(EntityManagerFactory emf) {
        if (emf == null) {
            new Exception("UserDAOImpl oprettet med null emf").printStackTrace();
        }
        this.emf = emf;
    }

    @Override
    public User createUser(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    @Override
    public User findById(int id) {
        EntityManager em = emf.createEntityManager();
        User foundUser = em.find(User.class, id);
        em.close();
        return foundUser;

    }

    @Override
    public List<User> findAllUsers() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public User update(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User updatedUser = em.merge(user);
            em.getTransaction().commit();
            return updatedUser;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return false;
    }

    @Override
    public User findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);

    }
}
