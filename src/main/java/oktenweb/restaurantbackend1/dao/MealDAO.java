package oktenweb.restaurantbackend1.dao;

import oktenweb.restaurantbackend1.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealDAO extends JpaRepository<Meal, Integer> {

}
