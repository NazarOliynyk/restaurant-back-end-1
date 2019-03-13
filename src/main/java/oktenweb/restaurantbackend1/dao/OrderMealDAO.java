package oktenweb.restaurantbackend1.dao;

import oktenweb.restaurantbackend1.models.OrderMeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMealDAO extends JpaRepository<OrderMeal, Integer>{
}
