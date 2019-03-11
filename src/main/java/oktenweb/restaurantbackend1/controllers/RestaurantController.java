package oktenweb.restaurantbackend1.controllers;

import oktenweb.restaurantbackend1.dao.MealDAO;
import oktenweb.restaurantbackend1.dao.RestaurantDAO;
import oktenweb.restaurantbackend1.models.Meal;
import oktenweb.restaurantbackend1.models.MenuSection;
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
    @Autowired
    private MealDAO mealDAO;

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

    private Restaurant restaurantChosen = new Restaurant();
    private List<MenuSection> menuSectionsEnum = new ArrayList<>();

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
                restaurantChosen = restaurant;
                List<String> menuSections = new ArrayList<>();
                menuSections.add("Section A");
                menuSections.add("Section B");
                menuSections.add("Section C");
                menuSections.add("Section D");
                menuSections.add("Section E");
                menuSections.add("Section F");
                menuSections.add("Section J");
                menuSections.add("Section H");
                menuSections.add("Section K");
                menuSections.add("Section L");

                model.addAttribute("menuSections", menuSections);

                // just to try how it saves enum - looks like as numbers
//                Meal meal1 = new Meal();
//                meal1.setMenuSection(MenuSection.A_MEAL);
//                meal1.setName("GGGGG");
//                mealDAO.save(meal1);

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
                                    @RequestParam("Section A") String a,
                                   @RequestParam("Section B") String b,
                                   @RequestParam("Section C") String c,
                                   @RequestParam("Section D") String d,
                                   @RequestParam("Section E") String e,
                                   @RequestParam("Section F") String f,
                                   @RequestParam("Section J") String j,
                                   @RequestParam("Section H") String h,
                                   @RequestParam("Section K") String k,
                                   @RequestParam("Section L") String l){
        List<String> menuSections = new ArrayList<>();
        menuSections.add(a);
        menuSections.add(b);
        menuSections.add(c);
        menuSections.add(d);
        menuSections.add(e);
        menuSections.add(f);
        menuSections.add(j);
        menuSections.add(h);
        menuSections.add(k);
        menuSections.add(l);

        menuSectionsEnum.add(MenuSection.A_MEAL);
        menuSectionsEnum.add(MenuSection.B_MEAL);
        menuSectionsEnum.add(MenuSection.C_MEAL);
        menuSectionsEnum.add(MenuSection.D_MEAL);
        menuSectionsEnum.add(MenuSection.E_MEAL);
        menuSectionsEnum.add(MenuSection.F_MEAL);
        menuSectionsEnum.add(MenuSection.J_MEAL);
        menuSectionsEnum.add(MenuSection.H_MEAL);
        menuSectionsEnum.add(MenuSection.K_MEAL);
        menuSectionsEnum.add(MenuSection.L_MEAL);

        for (int i = 0; i < menuSections.size(); i++) {
             if(!menuSections.get(i).equals("")){
                 menuSectionsEnum.get(i).name = menuSections.get(i);
             }
        }
        for (MenuSection menuSection : menuSectionsEnum) {
            System.out.println(menuSection.name);
        }

        model.addAttribute(model.addAttribute("restaurant", restaurantChosen));
        model.addAttribute("menuSections", menuSections);
        return "addMenu";
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
        model.addAttribute(model.addAttribute("restaurant", restaurantChosen));
        for (int i = 0; i< menuSectionsEnum.size(); i++) {
            if(menuSectionsEnum.get(i).name.equals(menuSection)){
                meal.setMenuSection(menuSectionsEnum.get(i));
            }
        }
        System.out.println(meal.getMenuSection().toString());
        meal.setName(name);
        meal.setDescription(description);
        meal.setQuantity(quantity);
        meal.setPrice(Double.parseDouble(price));
        Meal meal1 = new Meal();
        meal1.setMenuSection(MenuSection.A_MEAL);
        meal1.setName("GGGGG");
        mealDAO.save(meal1);
       // mealDAO.save(meal);

        //List<Meal> meals = restaurantChosen.getMenu();
        //meals.add(meal);
        //restaurantChosen.setMenu(meals);
        //restaurantDAO.save(restaurantChosen);
        return "addMenu";
    }

}
