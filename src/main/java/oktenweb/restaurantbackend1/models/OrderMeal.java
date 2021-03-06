package oktenweb.restaurantbackend1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"meals"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderMeal implements Comparable<OrderMeal>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    Date date;
    String reasonOfCancelation;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    ResponseType responseFromClient;

    @Enumerated(EnumType.STRING)
    ResponseType responseFromRestaurant;

    String descriptionFromClient;
    String descriptionFromRestaurant;

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
    Client client;

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
    Restaurant restaurant;

    @JsonIgnore
    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            fetch = FetchType.EAGER
            //mappedBy = "orders"
    )
    List<Meal> meals = new ArrayList<>();

    @Override
    public int compareTo(OrderMeal o) {
        return this.getOrderStatus().compareTo(o.getOrderStatus());
    }
}
