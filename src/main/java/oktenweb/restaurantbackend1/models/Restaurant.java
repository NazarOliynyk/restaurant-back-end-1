package oktenweb.restaurantbackend1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Restaurants")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"meals", "menuSections", "orders", "clients"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int id;
     String username;
     String password;
     String name;
     String address;
     String email;
     String phoneNumber;
     String additionalInfo;
     int numberOfPositiveResp;
     int numberOfNegativeResp;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "restaurant"
    )
    List<Meal> meals = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "restaurant"
    )
    List<MenuSection> menuSections = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "restaurant"
    )
    List<OrderMeal> orders = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
            // mappedBy = "orders"
    )
    List<Client> clients = new ArrayList<>();
}
