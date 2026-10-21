package DTO.Event;

import DTO.Address.AddressDTO;
import DTO.Category.CategoryDTO;
import entities.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventDTO {
    private int id;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AddressDTO address;
    private CategoryDTO category;

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getNameOfOccurrence();
        this.description = event.getDescription();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.address = new AddressDTO(event.getAddress());
        this.category = new CategoryDTO(event.getCategory());
    }
}
