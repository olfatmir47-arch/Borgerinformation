package DAOs;

import DTO.Security.JwtUserDTO;
import entities.User;

import java.util.List;

public interface UserDAO {
    User createUser(User user);

    User update(User user);

    User findById(int id);

    List<User> findAllUsers();

    boolean delete(int id);

    User findByEmail(String email);
}
