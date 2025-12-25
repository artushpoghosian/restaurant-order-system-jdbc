public interface Commands {

    String EXIT = "0";
    String ADD_DISH_TO_MENU = "1";
    String REMOVE_DISH_FROM_MENU = "2";
    String UPDATE_DISH_IN_MENU = "3";
    String VIEW_ALL_DISHES = "4";
    String ADD_CUSTOMER = "5";
    String VIEW_ALL_CUSTOMERS = "6";
    String CREATE_ORDER = "7";
    String ADD_DISH_TO_ORDER = "8";
    String VIEW_ALL_ORDERS = "9";
    String CHANGE_ORDER_STATUS = "10";
    String CALCULATE_ORDER_TOTAL_PRICE = "11";
    String VIEW_MENU = "M";

    static void printCommands() {
        System.out.println("Please input " + EXIT + " for EXITING THE PROGRAM");
        System.out.println("Please input " + ADD_DISH_TO_MENU + " for ADDING A DISH");
        System.out.println("Please input " + REMOVE_DISH_FROM_MENU + " for REMOVING A DISH");
        System.out.println("Please input " + UPDATE_DISH_IN_MENU + " for UPDATING A DISH");
        System.out.println("Please input " + VIEW_ALL_DISHES + " for VIEWING ALL DISHES");
        System.out.println("Please input " + ADD_CUSTOMER + " for ADDING A CUSTOMER");
        System.out.println("Please input " + VIEW_ALL_CUSTOMERS + " for VIEWING ALL CUSTOMERS");
        System.out.println("Please input " + CREATE_ORDER + " for CREATING AN ORDER");
        System.out.println("Please input " + ADD_DISH_TO_ORDER + " for ADDING DISH TO ORDER");
        System.out.println("Please input " + VIEW_ALL_ORDERS + " for VIEWING ALL ORDERS");
        System.out.println("Please input " + CHANGE_ORDER_STATUS + " for CHANGING ORDER STATUS");
        System.out.println("Please input " + CALCULATE_ORDER_TOTAL_PRICE + " for CALCULATING ORDER'S TOTAL PRICE");
        System.out.println("Please input " + VIEW_MENU + " for MENU");
    }
}