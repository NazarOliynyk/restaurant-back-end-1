package oktenweb.restaurantbackend1.controllers;

import oktenweb.restaurantbackend1.dao.ClientDAO;
import oktenweb.restaurantbackend1.dao.MealDAO;
import oktenweb.restaurantbackend1.dao.OrderMealDAO;
import oktenweb.restaurantbackend1.dao.RestaurantDAO;
import oktenweb.restaurantbackend1.models.*;
import oktenweb.restaurantbackend1.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
    @Autowired
    private EmailService emailService;

    private Client clientChosen = new Client();
    private Restaurant restaurantChosen = new Restaurant();
    private List<Meal> mealsCreated = new ArrayList<>();
    private OrderMeal orderChosen = new OrderMeal();

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
                Collections.sort(orders);
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

            restaurantChosen = restaurantDAO.findOne(restaurantId);
            model.addAttribute("restaurant", restaurantChosen);
            List<Meal> meals = new ArrayList<>();
            meals = restaurantChosen.getMeals();
            Collections.sort(meals);
            model.addAttribute("meals", meals);
            orderChosen.setOrderStatus(OrderStatus.JUST_ORDERED);
            orderChosen.setDate(new Date());
            orderChosen.setRestaurant(restaurantChosen);
            orderChosen.setClient(clientChosen);

            orderChosen.setResponseFromClient(ResponseType.NEUTRAL);
            orderChosen.setResponseFromRestaurant(ResponseType.NEUTRAL);
            orderMealDAO.save(orderChosen);

        return "menuForClient";
        }

        @GetMapping("/goToMeal-{xxx}")
        public String goToMeal(@PathVariable("xxx") int mealId,
                                     Model model)
        {
            System.out.println("restaurant.getName(): "+mealId);
            Meal meal = new Meal();
            meal = mealDAO.findOne(mealId);
            OrderMeal order = orderMealDAO.findOne(orderChosen.getId());
            List<Meal> meals = order.getMeals();
            meals.add(meal);
            order.setMeals(meals);
            orderMealDAO.save(order);
            List<OrderMeal> orderList = new ArrayList<>();
            orderList = meal.getOrders();
            orderList.add(order);
            meal.setOrders(orderList);
            mealDAO.save(meal);
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
        public String saveOrder(Model model) throws MessagingException {

            model.addAttribute("restaurant", restaurantChosen);
            model.addAttribute("mealsCreated", mealsCreated);
            model.addAttribute("client", clientChosen);
            Client client = clientDAO.findOne(clientChosen.getId());
            List<OrderMeal> orderMeals = client.getOrders();
            model.addAttribute("orders", orderMeals);
            emailService.sendEmail(restaurantChosen.getEmail());
            return "createOrder";
        }


    @GetMapping("/confirmServed-{xxx}")
    public String cancelOrderByRestaurant(Model model,
                                          @PathVariable("xxx") int orderId  ) {
        OrderMeal order = orderMealDAO.findOne(orderId);
        order.setOrderStatus(OrderStatus.SERVED);
        orderMealDAO.save(order);

        return "client";
    }
    //@CrossOrigin(origins = "*")
    @GetMapping("/deleteOrderByClient-{xxx}")
    public String deleteOrderByClient(Model model,
                                          @PathVariable("xxx") int orderId  ) {
       OrderMeal order = orderMealDAO.findOne(orderId);
        System.out.println(order.toString());
        List<Meal> meals = order.getMeals();
        for (Meal meal : meals) {
            List<OrderMeal> orders = meal.getOrders();
            orders.remove(order);
            meal.setOrders(orders);
            mealDAO.save(meal);
        }
        orderMealDAO.delete(orderId);

        return "client";
    }

    @GetMapping("/negativeResponseFromClient-{xxx}")
    public String negativeResponseFromClient(Model model,
                                          @PathVariable("xxx") int orderId  ) {
        OrderMeal order = orderMealDAO.findOne(orderId);
        order.setResponseFromClient(ResponseType.NEGATIVE);
        Restaurant restaurant = order.getRestaurant();
        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: restaurant.getOrders()) {
            if(ord.getResponseFromClient().equals(ResponseType.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromClient().equals(ResponseType.POSITIVE)) {
                positive.add(ord);}
        }
        restaurant.setNumberOfNegativeResp(negative.size());
        restaurant.setNumberOfPositiveResp(positive.size());
        restaurantDAO.save(restaurant);
        orderMealDAO.save(order);

        return "client";
    }
    @GetMapping("/positiveResponsefromClient-{xxx}")
    public String positiveResponsefromClient(Model model,
                                   @PathVariable("xxx") int orderId  ) {
        OrderMeal order = orderMealDAO.findOne(orderId);
        order.setResponseFromClient(ResponseType.POSITIVE);
        Restaurant restaurant = order.getRestaurant();
        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: restaurant.getOrders()) {
            if(ord.getResponseFromClient().equals(ResponseType.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromClient().equals(ResponseType.POSITIVE)) {
                positive.add(ord);}
        }
        restaurant.setNumberOfNegativeResp(negative.size());
        restaurant.setNumberOfPositiveResp(positive.size());
        restaurantDAO.save(restaurant);
        orderMealDAO.save(order);

        return "client";
    }

}
