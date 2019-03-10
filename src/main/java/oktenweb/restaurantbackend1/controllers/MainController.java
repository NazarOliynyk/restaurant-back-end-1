package oktenweb.restaurantbackend1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/restaurants")
    public String restaurants(){
        return "restaurant";
    }
    @GetMapping("/clients")
    public String clients(){
        return "client";
    }
}
