package oktenweb.restaurantbackend1.controllers;

import oktenweb.restaurantbackend1.dao.RestaurantDAO;
import oktenweb.restaurantbackend1.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantDAO restaurantDAO;

    @PostMapping("/loginRestaurant")
    public String loginRestaurant(Model model,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ){
        String ret="";
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList = restaurantDAO.findAll();
        for (Restaurant restaurant : restaurantList) {
            if(restaurant.getUsername().equals(username)&&
            restaurant.getPassword().equals(password)){
                model.addAttribute("restaurant", restaurant);
                List<String> menuSections = new ArrayList<>();
                menuSections.add("APPETIZER");
                menuSections.add("SALAD");
                menuSections.add("FIRST_COURSE");
                menuSections.add("MAIN_COURSE");
                menuSections.add("STRONG_DRINK");
                menuSections.add("SOFT_DRINK");
                menuSections.add("HOT_DRINK");
                menuSections.add("DESSERT");
                menuSections.add("OTHER");

                model.addAttribute("menuSections", menuSections);
                ret = "addMenu";
                break;
            }else {
                model.addAttribute("resultOfLogination", "Wrong Input!!! Try Again!");
                ret = "restaurant";
            }
        }
        return  ret;
    }
    @PostMapping("/saveRestaurant")
    public String saveRestaurant(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("additionalInfo") String additionalInfo

    ){
        Restaurant restaurant = new Restaurant();
        restaurant.setUsername(username);
        restaurant.setPassword(password);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setEmail(email);
        restaurant.setPhoneNumber(phoneNumber);
        restaurant.setAdditionalInfo(additionalInfo);
        restaurantDAO.save(restaurant);

        return "restaurant";
    }
}
