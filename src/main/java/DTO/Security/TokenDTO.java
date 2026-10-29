package DTO.Security;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class TokenDTO {

    String token;
    String email;
}
