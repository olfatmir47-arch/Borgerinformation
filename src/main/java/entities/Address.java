package entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String streetName;
    private String number;
    private String floor;

    private double lat;
    private double lng;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public Address(String streetName, String number, String floor, double lat, double lng, City city) {
        this.streetName = streetName;
        this.number = number;
        this.floor = floor;
        this.lat = lat;
        this.lng = lng;
        this.city = city;
    }


}
