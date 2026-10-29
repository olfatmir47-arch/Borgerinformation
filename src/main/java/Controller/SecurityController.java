package Controller;

import DAOs.ISecurityDAO;
import DAOs.SecurityDAO;
import DAOs.UserDAOImpl;
import DTO.Security.JwtUserDTO;
import DTO.Security.TokenDTO;
import DTO.User.CreateUserDTO;
import DTO.User.VerifyUserDTO;
import app.config.HibernateConfig;
import app.exceptions.ApiException;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.bugelhartmann.ITokenSecurity;
import dk.bugelhartmann.TokenSecurity;
import dk.bugelhartmann.TokenVerificationException;
import dk.bugelhartmann.UserDTO;
import entities.User;
import io.javalin.http.*;
import io.javalin.security.RouteRole;
import io.javalin.validation.ValidationException;

import jakarta.persistence.EntityNotFoundException;

import javax.swing.text.html.parser.Entity;
import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityController implements ISecurityController {
    private ISecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());
    private ObjectMapper objectMapper = new ObjectMapper();
    private ITokenSecurity tokenSecurity = new TokenSecurity();



    @Override
    public void login(Context ctx) {
        try {
            VerifyUserDTO user = ctx.bodyAsClass(VerifyUserDTO.class);
            User verifiedUser =
                    securityDAO.getVerifiedUser(
                            user.getEmail(),
                            user.getPassword());

            UserDTO tokenUser = new UserDTO(
                    verifiedUser.getEmail(),
                    verifiedUser.getRolesAsStrings());


            String token = createToken(tokenUser);

            ctx.status(200).json(new TokenDTO(token, verifiedUser.getEmail()));
        } catch (Exception e) {
            throw new ApiException(401, "Invalid email or password");
        }
    }


    @Override
    public void authenticate(Context ctx) {
        if (ctx.method().toString().equals("OPTIONS")) {
            ctx.status(200);
            return;
        }

        Set<String> allowedRoles = ctx.routeRoles()
                .stream()
                .map(routeRole -> routeRole.toString().toUpperCase())
                .collect(Collectors.toSet());

        if(isOpenEndpoint(allowedRoles)) {
            return;
        }


        UserDTO verifiedUser = validateAndGetUserFromToken(ctx);

        ctx.attribute("user", verifiedUser);
    }

    @Override
    public void authorize(Context ctx) {

        Set<String> allowedRoles = ctx.routeRoles()
                .stream()
                .map(role -> role.toString().toUpperCase())
                .collect(Collectors.toSet());

        if (isOpenEndpoint(allowedRoles)) {
            return;
        }

        UserDTO user = ctx.attribute("user");

        if (user == null) {
            throw new ForbiddenResponse("No user found");
        }


        if(!userHasAllowedRole(user, allowedRoles))
            throw new ForbiddenResponse("User was not authorized with roles: " + user.getRoles() + ". Needed roles are: " + allowedRoles);

    }

    private String createToken(UserDTO user) {
        try {
            String ISSUER;
            String TOKEN_EXPIRE_TIME;
            String SECRET_KEY;

            if (System.getenv("DEPLOYED") != null) {
                ISSUER = System.getenv("ISSUER");
                TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME");
                SECRET_KEY = System.getenv("SECRET_KEY");
            } else {
                ISSUER = Utils.getPropertyValue("ISSUER", "config.properties");
                TOKEN_EXPIRE_TIME = Utils.getPropertyValue("TOKEN_EXPIRE_TIME", "config.properties");
                SECRET_KEY = Utils.getPropertyValue("SECRET_KEY", "config.properties");
                System.out.println(ISSUER+" "+TOKEN_EXPIRE_TIME+" "+SECRET_KEY);
            }
            return tokenSecurity.createToken(user, ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY);
        } catch (Exception e) {
//            logger.error("Could not create token", e);
            e.printStackTrace();
            throw new ApiException(500, "Could not create token");
        }
    }

    private UserDTO verifyToken(String token) {
        boolean IS_DEPLOYED = (System.getenv("DEPLOYED") != null);
        String SECRET = IS_DEPLOYED ? System.getenv("SECRET_KEY") : Utils.getPropertyValue("SECRET_KEY", "config.properties");

        try {
            if (tokenSecurity.tokenIsValid(token, SECRET) && tokenSecurity.tokenNotExpired(token)) {
                return tokenSecurity.getUserWithRolesFromToken(token);
            } else {
                throw new ApiException(403, "Token is not valid");
            }
        } catch (ParseException | TokenVerificationException e) {
//            logger.error("Could not create token", e);
            throw new ApiException(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized. Could not verify token");
        }
    }

    private boolean isOpenEndpoint(Set<String> allowedRoles) {
        // If the endpoint is not protected with any roles:
        if (allowedRoles.isEmpty())
            return true;

        // 1. Get permitted roles and Check if the endpoint is open to all with the ANYONE role
        if (allowedRoles.contains("EVERYONE")) {
            return true;
        }
        return false;
    }

    private UserDTO validateAndGetUserFromToken(Context ctx) {
        String token = getToken(ctx);
        UserDTO verifiedTokenUser = verifyToken(token);
        if (verifiedTokenUser == null) {
            throw new UnauthorizedResponse("Invalid user or token"); // UnauthorizedResponse is javalin 6 specific but response is not json!
        }
        return verifiedTokenUser;
    }

    private static String getToken(Context ctx) {
        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedResponse(
                    "Authorization header is missing or malformed"
            );
        }

        String[] parts = header.split(" ");

        if (parts.length != 2 || parts[1].isBlank()) {
            throw new UnauthorizedResponse(
                    "Authorization header is malformed"
            );
        }

        return parts[1];
    }

    private static boolean userHasAllowedRole(UserDTO user, Set<String> allowedRoles) {
        return user.getRoles().stream()
                .anyMatch(role -> allowedRoles.contains(role.toUpperCase()));
    }
}
