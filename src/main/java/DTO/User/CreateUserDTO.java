package DTO.User;

import DTO.Address.CreateAddressDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CreateUserDTO {
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    private String email;
    private String password;
    private CreateAddressDTO address;

    public void validate() {
        if (!firstName.matches("[a-zA-ZæøåÆØÅ'\\- ]+$")) {
            throw new IllegalArgumentException("Firstname must contain only letters");
        }
        if (!lastName.matches("[a-zA-ZæøåÆØÅ'\\- ]+")) {
            throw new IllegalArgumentException("Lastname must contain only letters");
        }
        /*if(!birthDate.("dd-MM-yyyy")) {
            throw new IllegalArgumentException("birthdate must be written in dd-mm-yyyy fx: 01-01-1999");
        }*/

    }

}
