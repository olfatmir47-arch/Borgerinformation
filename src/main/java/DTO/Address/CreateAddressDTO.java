package DTO.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CreateAddressDTO {
    private String streetName;
    private String number;
    private String floor;
    private String postalCode;
    private String cityName;

    public void validate() {
        if (streetName == null || !streetName.matches("[a-zA-ZæøåÆØÅ'\\- ]+$")) {
            throw new IllegalArgumentException("Streetname can contain letters, ', - ");
        }
        if (cityName == null || !cityName.matches("[a-zA-ZæøåÆØÅ'\\- ]+$")) {
            throw new IllegalArgumentException("Cityname can contain letters, ', - ");
        }
        if (postalCode == null || !postalCode.matches("^[0-9]{4}$")) {
            throw new IllegalArgumentException("Postalcode must only contain 4 digits");
        }
    }
}
