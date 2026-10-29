package Controller;


import DTO.User.CreateUserDTO;
import DTO.User.UserDTO;
import Service.UserService;
import entities.User;
import io.javalin.http.Context;

import java.util.List;

public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    public void getUserById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        User user = userService.findUserById(id);
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        ctx.json(userDTO);
    }

    public void createUser(Context ctx){
        CreateUserDTO dto = ctx.bodyAsClass(CreateUserDTO.class);
        User user = userService.createUser(dto);
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        ctx.json(userDTO);
    }

    public void getAllUsers(Context ctx){
        List<UserDTO> users = userService.getAllUsers();
        ctx.json(users);
    }

    public void updateUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        CreateUserDTO dto = ctx.bodyAsClass(CreateUserDTO.class);
        User updated = userService.updateUser(id, dto);
        ctx.json(updated);
    }

    public void deleteUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        userService.delete(id);
        ctx.status(204);
    }
}

