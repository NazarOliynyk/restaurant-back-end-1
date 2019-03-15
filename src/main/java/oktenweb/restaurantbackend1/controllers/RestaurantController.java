package oktenweb.restaurantbackend1.controllers;

import oktenweb.restaurantbackend1.dao.*;
import oktenweb.restaurantbackend1.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantDAO restaurantDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private MealDAO mealDAO;
    @Autowired
    private MenuSectionDAO menuSectionDAO;
    @Autowired
    private OrderMealDAO orderMealDAO;

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
        Collections.sort(orders);
        model.addAttribute("orders", orders);
        return "orderListForRestaurant";
    }

    @GetMapping("/acceptOrderToKitchen-{xxx}")
    public String acceptOrderToKitchen(Model model,
                                       @PathVariable("xxx") int orderId){
        OrderMeal order = orderMealDAO.findOne(orderId);
        order.setOrderStatus(OrderStatus.IN_PROCESS);
        orderMealDAO.save(order);

        return "restaurant";
    }
    @GetMapping("/cancelOrderByRestaurant-{xxx}")
    public String cancelOrderByRestaurant(Model model,
                                       @PathVariable("xxx") int orderId
                                       //@PathVariable("reasonOfCancelation") String reasonOfCancelation
    ){
        OrderMeal order = orderMealDAO.findOne(orderId);
        order.setOrderStatus(OrderStatus.CANCELED_BY_RESTAURANT);
        orderMealDAO.save(order);

        return "restaurant";
    }
    @GetMapping("/negativeResponseFromRestaurant-{xxx}")
    public String negativeResponseFromRestaurant(Model model,
                                   @PathVariable("xxx") int orderId  ) {
        OrderMeal order = orderMealDAO.findOne(orderId);
        order.setResponseFromRestaurant(ResponseType.NEGATIVE);
        Client client = order.getClient();
        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: client.getOrders()) {
            if(ord.getResponseFromRestaurant().equals(ResponseType.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromRestaurant().equals(ResponseType.POSITIVE)){
                positive.add(ord);}
        }
        client.setNumberOfNegativeResp(negative.size());
        client.setNumberOfPositiveResp(positive.size());
        clientDAO.save(client);
        orderMealDAO.save(order);

        return "restaurant";
    }

    @GetMapping("/positiveResponseFromRestaurant-{xxx}")
    public String positiveResponseFromRestaurant(Model model,
                                   @PathVariable("xxx") int orderId  ) {
        OrderMeal order = orderMealDAO.findOne(orderId);
        order.setResponseFromRestaurant(ResponseType.POSITIVE);
        Client client = order.getClient();
        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: client.getOrders()) {
            if(ord.getResponseFromRestaurant().equals(ResponseType.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromRestaurant().equals(ResponseType.POSITIVE)){
                positive.add(ord);}
        }
        client.setNumberOfNegativeResp(negative.size());
        client.setNumberOfPositiveResp(positive.size());
        clientDAO.save(client);
        orderMealDAO.save(order);

        return "restaurant";
    }

}
