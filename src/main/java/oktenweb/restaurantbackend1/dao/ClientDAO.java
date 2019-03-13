package oktenweb.restaurantbackend1.dao;

import oktenweb.restaurantbackend1.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDAO extends JpaRepository<Client, Integer>{
    Client findClientByUsername(String username);
}
