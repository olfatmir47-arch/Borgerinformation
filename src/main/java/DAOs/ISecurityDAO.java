package DAOs;

import entities.Role;
import entities.User;
import io.javalin.validation.ValidationException;

public interface ISecurityDAO {
    User getVerifiedUser(String email, String password) throws ValidationException; // used for login
    User createUser(User user); // used for register
    Role createRole(String role);
    User addUserRole(String username, String role);
}
