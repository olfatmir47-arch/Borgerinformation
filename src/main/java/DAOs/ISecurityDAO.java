package DAOs;

import DTO.Security.JwtUserDTO;
import entities.Role;
import entities.User;
import io.javalin.validation.ValidationException;

public interface ISecurityDAO {
    User getVerifiedUser(String email, String password) throws ValidationException; // used for login


}
