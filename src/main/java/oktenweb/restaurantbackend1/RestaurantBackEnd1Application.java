package oktenweb.restaurantbackend1;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;




@SpringBootApplication
public class RestaurantBackEnd1Application {

    public static void main(String[] args) {
//stackoverflow.com/questions/36160353/why-does-swing-think-its-headless-under-spring-boot-but-not-under-spring-or-pl

        SpringApplicationBuilder builder =
                new SpringApplicationBuilder(RestaurantBackEnd1Application.class);
        builder.headless(false).run(args);

    }

}
