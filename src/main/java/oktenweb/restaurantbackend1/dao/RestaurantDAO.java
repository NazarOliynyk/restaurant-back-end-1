package oktenweb.restaurantbackend1.dao;

import oktenweb.restaurantbackend1.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantDAO extends JpaRepository<Restaurant, Integer>{
}
