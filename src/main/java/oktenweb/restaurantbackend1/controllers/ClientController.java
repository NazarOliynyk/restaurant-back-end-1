package oktenweb.restaurantbackend1.controllers;

import oktenweb.restaurantbackend1.dao.ClientDAO;
import oktenweb.restaurantbackend1.dao.MealDAO;
import oktenweb.restaurantbackend1.dao.OrderMealDAO;
import oktenweb.restaurantbackend1.dao.RestaurantDAO;
import oktenweb.restaurantbackend1.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class ClientController {
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private OrderMealDAO orderMealDAO;
    @Autowired
    private RestaurantDAO restaurantDAO;
    @Autowired
    private MealDAO mealDAO;

    private Client clientChosen = new Client();
    private Restaurant restaurantChosen = new Restaurant();
    private List<Meal> mealsCreated = new ArrayList<>();

    //@PostMapping("/loginClient")
    public String loginClient(){
//        Client client = new Client();
//        client.setUsername("ccc");
//        client.setPassword("ccc");
//        client.setEmail("naz@ukr.net");
//        clientDAO.save(client);
//        Restaurant restaurant = new Restaurant();
//        restaurant = restaurantDAO.findRestaurantByName("Tower");
//        Client clientChosen = new Client();
//        clientChosen = clientDAO.findClientByUsername("ccc");
//        List<Client> clients = restaurant.getClients();
//        clients.add(clientChosen);
//        restaurant.setClients(clients);
//        restaurantDAO.save(restaurant);

        OrderMeal order = new OrderMeal();
        order.setOrderStatus(OrderStatus.IN_PROCESS);
        order.setDate(new Date());
        Restaurant restaurant = new Restaurant();
        restaurant = restaurantDAO.findRestaurantByName("Tower");
        Client clientChosen = new Client();
        clientChosen = clientDAO.findClientByUsername("ccc");
        order.setRestaurant(restaurant);
        order.setClient(clientChosen);
//        Meal meal1 = new Meal();
//        meal1 = mealDAO.findOne(2);
//        List<OrderMeal> orders1 = new ArrayList<>();
//        orders1 =meal1.getOrders();

        Meal meal2 = new Meal();
        meal2 = mealDAO.findOne(5);
        List<OrderMeal> orders2 = new ArrayList<>();
        orders2 =meal2.getOrders();

        //orders1.add(order);
        orders2.add(order);
        //meal1.setOrders(orders1);
       meal2.setOrders(orders2);
        //mealDAO.save(meal1);
        mealDAO.save(meal2);
//        meals.add(meal1);
//        meals.add(meal2);
//        order.setMeals(meals);
  //      orderMealDAO.save(order);

        return "client";
    }

    @PostMapping("/saveClient")
    public String saveClient(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("email") String email){
        Client client = new Client();
        client.setUsername(username);
        client.setPassword(password);
        client.setEmail(email);
        clientDAO.save(client);
        return "client";
    }

    @PostMapping("/loginClient")
    public String loginClient(Model model,
                   @RequestParam("username") String username,
                   @RequestParam("password") String password){
        String ret="";
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants = restaurantDAO.findAll();
        List<Client> clients = new ArrayList<>();
        clients= clientDAO.findAll();
        for (Client client : clients) {
            if(client.getUsername().equals(username)&&
                    client.getPassword().equals(password)){
                model.addAttribute("client", client);
                model.addAttribute("restaurants", restaurants);
                clientChosen = client;
                List<OrderMeal> orders = null;
                orders = clientChosen.getOrders();
                if(orders == null){
                   model.addAttribute("orders", "No orders yet!");
                }else {
                    model.addAttribute("orders", orders);
                }
                ret = "createOrder";
                break;

            }else {model.addAttribute("resultOfLogination", "Wrong Input!!! Try Again!");
                ret = "client";
            }
        }

        return ret;
    }

        @GetMapping("/goToRestaurant-{xxx}")
        public String goToRestaurant(@PathVariable("xxx") int restaurantId,
                                           Model model)
        {
            System.out.println("restaurant.getName(): "+restaurantId);
            //Restaurant restaurant = new Restaurant();
            restaurantChosen = restaurantDAO.findOne(restaurantId);
            model.addAttribute("restaurant", restaurantChosen);
            List<Meal> meals = new ArrayList<>();
            meals = restaurantChosen.getMeals();
            Collections.sort(meals);
            model.addAttribute("meals", meals);

        return "menuForClient";
        }

        @GetMapping("/goToMeal-{xxx}")
        public String goToMeal(@PathVariable("xxx") int mealId,
                                     Model model)
        {
            System.out.println("restaurant.getName(): "+mealId);
            Meal meal = new Meal();
            meal = mealDAO.findOne(mealId);
            mealsCreated.add(meal);
            model.addAttribute("restaurant", restaurantChosen);
            model.addAttribute("mealsCreated", mealsCreated);

            return "formOrder";
        }
        @GetMapping("/goToMenuForClient")
        public String goToMenuForClient(Model model){

            model.addAttribute("restaurant", restaurantChosen);
            List<Meal> meals = new ArrayList<>();
            meals = restaurantChosen.getMeals();
            Collections.sort(meals);
            model.addAttribute("meals", meals);
            return "menuForClient";
        }

        @GetMapping("/saveOrder")
        public String saveOrder(Model model){
            OrderMeal order = new OrderMeal();
            order.setOrderStatus(OrderStatus.JUST_ORDERED);
            order.setDate(new Date());
            order.setRestaurant(restaurantChosen);
            order.setClient(clientChosen);
            order.setMeals(mealsCreated);

            for (Meal meal : mealsCreated) {
                List<OrderMeal> orderList = new ArrayList<>();
                orderList = meal.getOrders();
                orderList.add(order);
                meal.setOrders(orderList);
                mealDAO.save(meal);
            }
            model.addAttribute("restaurant", restaurantChosen);
            model.addAttribute("mealsCreated", mealsCreated);
            model.addAttribute("client", clientChosen);
            Client client = clientDAO.findOne(clientChosen.getId());
            List<OrderMeal> orderMeals = client.getOrders();
            model.addAttribute("orders", orderMeals);
            return "createOrder";
        }


}
