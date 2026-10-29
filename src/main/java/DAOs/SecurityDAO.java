package DAOs;

import DTO.Security.JwtUserDTO;
import entities.Role;
import entities.User;
import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityManagerFactory;
import org.mindrot.jbcrypt.BCrypt;

public class SecurityDAO implements ISecurityDAO {

    private UserDAO userDAO;

    public SecurityDAO(EntityManagerFactory emf) {
        this.userDAO = new UserDAOImpl(emf);
    }

    @Override
    public User getVerifiedUser(String email, String password) throws ValidationException {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new IllegalArgumentException("wrong password");
        }
        return user;
    }

    public boolean verifyPassword(String passWord) {
       return true;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public Role createRole(String role) {
        return null;
    }

    @Override
    public User addUserRole(String username, String role) {
        return null;
    }
}
