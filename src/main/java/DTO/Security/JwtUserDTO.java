package DTO.Security;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class JwtUserDTO {

    private String email;
    private String role;
}
