package oktenweb.restaurantbackend1.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"restaurant"})

public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private MenuSection menuSection;
    private String name;
    private String description;
    private String quantity;
    private double price;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Restaurant restaurant;
}
