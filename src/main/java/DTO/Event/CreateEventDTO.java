package DTO.Event;

import DTO.Address.CreateAddressDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CreateEventDTO {
    private String name;
    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endTime;
    private CreateAddressDTO address;
    private int categoryId;


    public void validate() {

        if (name == null || !name.matches("[a-zA-ZæøåÆØÅ ]+")) {
            throw new IllegalArgumentException("Event name must contain only letters");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("Start time is required");
        }

        if (endTime == null) {
            throw new IllegalArgumentException("End time is required");
        }

        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        if (address == null) {
            throw new IllegalArgumentException("Location cannot be empty");
        }

        if (address.getStreetName() == null || address.getStreetName().isBlank()) {
            throw new IllegalArgumentException("Street name cannot be empty");
        }

        if (categoryId <= 0) {
            throw new IllegalArgumentException("Category is required");
        }
    }
}
