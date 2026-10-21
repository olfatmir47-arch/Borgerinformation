package entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@DiscriminatorValue("Incident")
public class Incident extends Occurrence {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
