package entities;

import io.javalin.security.RouteRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public enum Role implements RouteRole {
    USER,
    ADMIN,
    EVERYONE

}
