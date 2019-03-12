package oktenweb.restaurantbackend1.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"restaurant", "menuSection"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meal implements Comparable<Meal>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int id;
     String name;
     String description;
     String quantity;
     double price;

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
     Restaurant restaurant;

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
    MenuSection menuSection;

    @Override
    public int compareTo(Meal o) {
        return this.getMenuSection().getId() - o.getMenuSection().getId();
    }

}
