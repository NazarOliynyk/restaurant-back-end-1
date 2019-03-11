package oktenweb.restaurantbackend1.models;

public enum MenuSection {


    A_MEAL(""),
    B_MEAL(""),
    C_MEAL(""),
    D_MEAL(""),
    E_MEAL(""),
    F_MEAL(""),
    J_MEAL(""),
    H_MEAL(""),
    K_MEAL(""),
    L_MEAL("");

    public String name;
     MenuSection(String name) {
        this.name= name;
    }
//    APPETIZER,
//    SALAD,
//    FIRST_COURSE,
//    MAIN_COURSE,
//    STRONG_DRINK,
//    SOFT_DRINK,
//    HOT_DRINK,
//    DESSERT,
//    OTHER
}
