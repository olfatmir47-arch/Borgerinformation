package DTO.Address;

import entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AddressDTO {
    private String streetName;
    private String number;
    private String postalCode;
    private String city;

    public AddressDTO(Address address) {
        this.streetName = address.getStreetName();
        this.number = address.getNumber();
        this.postalCode = address.getCity().getPostalCode();
        this.city = address.getCity().getCityName();
    }
}


