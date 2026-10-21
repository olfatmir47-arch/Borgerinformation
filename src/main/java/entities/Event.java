package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@DiscriminatorValue("Event")
public class Event extends Occurrence {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
