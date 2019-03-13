package oktenweb.restaurantbackend1.controllers;

import oktenweb.restaurantbackend1.dao.MealDAO;
import oktenweb.restaurantbackend1.dao.MenuSectionDAO;
import oktenweb.restaurantbackend1.dao.RestaurantDAO;
import oktenweb.restaurantbackend1.models.Meal;
import oktenweb.restaurantbackend1.models.MenuSection;
import oktenweb.restaurantbackend1.models.OrderMeal;
import oktenweb.restaurantbackend1.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantDAO restaurantDAO;
    @Autowired
    private MealDAO mealDAO;
    @Autowired
    private MenuSectionDAO menuSectionDAO;

    private Restaurant restaurantChosen = new Restaurant();

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

//    @PostMapping("/loginRestaurant")
//    public String loginRestaurant(Model model,
//            @RequestParam("username") String username,
//            @RequestParam("password") String password
//    ){
//        String ret="";
//        List<Restaurant> restaurantList = new ArrayList<>();
//        restaurantList = restaurantDAO.findAll();
//        for (Restaurant restaurant : restaurantList) {
//            if(restaurant.getUsername().equals(username)&&
//            restaurant.getPassword().equals(password)){
//                model.addAttribute("restaurant", restaurant);
//                List<String> menuSections = new ArrayList<>();
//                menuSections.add("APPETIZER");
//                menuSections.add("SALAD");
//                menuSections.add("FIRST_COURSE");
//                menuSections.add("MAIN_COURSE");
//                menuSections.add("STRONG_DRINK");
//                menuSections.add("SOFT_DRINK");
//                menuSections.add("HOT_DRINK");
//                menuSections.add("DESSERT");
//                menuSections.add("OTHER");
//
//                model.addAttribute("menuSections", menuSections);
//                ret = "addMenu";
//                break;
//            }else {
//                model.addAttribute("resultOfLogination", "Wrong Input!!! Try Again!");
//                ret = "restaurant";
//            }
//        }
//        return  ret;
//    }


    @PostMapping("/loginRestaurant")
    public String loginRestaurant(Model model,
                                  @RequestParam("username") String username,
                                  @RequestParam("password") String password
    ){
        String ret="";
        List<Restaurant> restaurantList ;
        restaurantList = restaurantDAO.findAll();
        for (Restaurant restaurant : restaurantList) {
            if(restaurant.getUsername().equals(username)&&
                    restaurant.getPassword().equals(password)){
                model.addAttribute("restaurant", restaurant);
                restaurantChosen = restaurant;
                List<MenuSection> menuSectionObjects = null;
                menuSectionObjects = restaurantChosen.getMenuSections();

                List<String> menuSections = new ArrayList<>();
                if(menuSectionObjects == null){
                    menuSections.add("No Menu Sections added yet!");
                }else {
                    for (MenuSection menuSectionObject : menuSectionObjects) {
                        menuSections.add(menuSectionObject.getName());
                    }
                }
                model.addAttribute("menuSections", menuSections);
//                List<String> menuSections = new ArrayList<>();
//                menuSections.add("Section A");
//                menuSections.add("Section B");
//                menuSections.add("Section C");
//                menuSections.add("Section D");
//                menuSections.add("Section E");
//                menuSections.add("Section F");
//                menuSections.add("Section J");
//                menuSections.add("Section H");
//                menuSections.add("Section K");
//                menuSections.add("Section L");
                ret = "createMenuSections";
                break;
            }else {
                model.addAttribute("resultOfLogination", "Wrong Input!!! Try Again!");
                ret = "restaurant";
            }
        }
        return  ret;
    }

    @PostMapping("/saveMenuSections")
    public String saveMenuSections( Model model,
                                    @RequestParam("sectionName") String sectionName,
                                    @RequestParam("restaurantName") String restaurantName
                                  ){

        MenuSection menuSection = new MenuSection();
        menuSection.setName(sectionName);
        restaurantChosen = restaurantDAO.findRestaurantByName(restaurantName);
        menuSection.setRestaurant(restaurantChosen);
        menuSectionDAO.save(menuSection);

        List<MenuSection> menuSectionObjects = null;
        menuSectionObjects = restaurantChosen.getMenuSections();
        List<String> menuSections = new ArrayList<>();
        if(menuSectionObjects == null){
            menuSections.add("No Menu Sections added yet!");
        }else {
            for (MenuSection menuSectionObject : menuSectionObjects) {
                menuSections.add(menuSectionObject.getName());
            }
        }

        model.addAttribute("restaurant", restaurantChosen);
        model.addAttribute("menuSections", menuSections);

        return "createMenuSections";
    }

    @PostMapping("/saveMeal")
    public String saveMeal(Model model,
                            @RequestParam("menuSection") String menuSection,
                           @RequestParam("name") String name,
                           @RequestParam("description") String description,
                           @RequestParam("quantity") String quantity,
                           @RequestParam("price") String price
                           ){
        Meal meal = new Meal();

        List<MenuSection> menuSectionObjects = null;
        menuSectionObjects = restaurantChosen.getMenuSections();
        MenuSection menuSectionObject = new MenuSection();

        for (MenuSection sectionObject : menuSectionObjects) {
            if(sectionObject.getName().equals(menuSection)){
                menuSectionObject = sectionObject;
            }
        }

        meal.setName(name);
        meal.setDescription(description);
        meal.setQuantity(quantity);
        meal.setPrice(Double.parseDouble(price));
        meal.setRestaurant(restaurantChosen);
        meal.setMenuSection(menuSectionObject);
        mealDAO.save(meal);

        List<String> menuSections = new ArrayList<>();
        if(menuSectionObjects == null){
            menuSections.add("No Menu Sections added yet!");
        }else {
            for (MenuSection menuSectionObject1 : menuSectionObjects) {
                menuSections.add(menuSectionObject1.getName());
            }
        }

        model.addAttribute("restaurant", restaurantChosen);
        model.addAttribute("menuSections", menuSections);
        return "addMenu";
    }

    @GetMapping("/goToMenu")
    public String goToMenu(Model model,
                           @RequestParam("restaurantName") String restaurantName){
        restaurantChosen = restaurantDAO.findRestaurantByName(restaurantName);
        List<MenuSection> menuSectionObjects = null;
        menuSectionObjects = restaurantChosen.getMenuSections();
        List<String> menuSections = new ArrayList<>();
        if(menuSectionObjects == null){
            menuSections.add("No Menu Sections added yet!");
        }else {
            for (MenuSection menuSectionObject : menuSectionObjects) {
                menuSections.add(menuSectionObject.getName());
            }
        }
        model.addAttribute("restaurant", restaurantChosen);
        model.addAttribute("menuSections", menuSections);
        return "addMenu";
    }
    @PostMapping("/watchToMenu")
    public String watchToMenu(Model model,
                              @RequestParam("restaurantName") String restaurantName){
        restaurantChosen = restaurantDAO.findRestaurantByName(restaurantName);
        List<Meal> meals = restaurantChosen.getMeals();
        Collections.sort(meals);
        model.addAttribute("meals", meals);
        return "menuForRestaurant";
    }

    @PostMapping("/watchMyOrders")
    public String watchMyOrders(Model model,
                                @RequestParam("restaurantName") String restaurantName){
        Restaurant restaurant = restaurantDAO.findRestaurantByName(restaurantName);
        List<OrderMeal> orders = restaurant.getOrders();
        model.addAttribute("orders", orders);
        return "orderListForRestaurant";
    }

}
