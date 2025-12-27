import model.Customer;
import model.Dish;
import model.DishCategory;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import service.CustomerService;
import service.DishService;
import service.OrderItemService;
import service.OrderService;

import java.util.List;
import java.util.Scanner;

public class RestaurantPortal implements Commands {

    private static Scanner scanner = new Scanner(System.in);
    private static DishService dishService = new DishService();
    private static CustomerService customerService = new CustomerService();
    private static OrderService orderService = new OrderService();
    private static OrderItemService orderItemService = new OrderItemService();

    public static void main(String[] args) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("Welcome to Restaurant Portal");
            Commands.printCommands();
            String command = scanner.nextLine().toUpperCase();
            switch (command) {
                case EXIT:
                    isRunning = false;
                    break;
                case ADD_DISH_TO_MENU:
                    addDishToMenu();
                    break;
                case REMOVE_DISH_FROM_MENU:
                    viewAllDishes();
                    System.out.println("Choose Dish ID to remove the Dish: ");
                    int dishIDtoBeRemoved = scanner.nextInt();
                    scanner.nextLine();
                    dishService.removeDish(dishIDtoBeRemoved);
                    break;
                case UPDATE_DISH_IN_MENU:
                    updateDish();
                    break;
                case VIEW_ALL_DISHES:
                    viewAllDishes();
                    break;
                case ADD_CUSTOMER:
                    addCustomer();
                    break;
                case VIEW_ALL_CUSTOMERS:
                    viewAllCustomers();
                    break;
                case CREATE_ORDER:
                    viewAllCustomers();
                    System.out.println("Choose Customer ID to create an Order: ");
                    int customerIDtoForCreatingOrder = scanner.nextInt();
                    scanner.nextLine();
                    orderService.createOrder(customerIDtoForCreatingOrder);
                    break;
                case ADD_DISH_TO_ORDER:
                    addDishToOrder();
                    break;
                case VIEW_ALL_ORDERS:
                    viewAllOrders();
                    break;
                case CHANGE_ORDER_STATUS:
                    changeOrderStatus();
                    break;
                case CALCULATE_ORDER_TOTAL_PRICE:
                    calculateOrderTotalPrice();
                    break;
                case VIEW_MENU:
                    viewMenu();
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }

    private static void viewMenu() {
        System.out.println("Choose Dish category to View Menu. Choose from the following: [APPETIZER, MAIN, DESSERT, DRINK]");
        DishCategory dishCategory = null;
        while (dishCategory == null) {
            String dishCategoryInput = scanner.nextLine().toUpperCase();
            try {
                dishCategory = DishCategory.valueOf(dishCategoryInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Dish Category. Choose from the following: [APPETIZER, MAIN, DESSERT, DRINK]");
            }
        }

        List<Dish> dishes = dishService.getDishesByCategory(dishCategory);
        for (Dish dish : dishes) {
            System.out.println(dish);
        }
    }

    private static void calculateOrderTotalPrice() {
        viewAllOrders();
        System.out.println("Choose Order ID to calculate total price: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Order Total Price: " + orderItemService.calculateOrderTotalPrice(orderId));
    }

    private static void changeOrderStatus() {
        viewAllOrders();
        System.out.println("Enter Order ID to channge its Status: ");
        int orderIdForStatusChange = scanner.nextInt();
        scanner.nextLine();

        OrderStatus newStatus = null;
        while (newStatus == null) {
            System.out.println("Choose New Status for the Order [PENDING, PREPARING, READY, DELIVERED]");
            String statusInput = scanner.nextLine().toUpperCase();
            try {
                newStatus = OrderStatus.valueOf(statusInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Order Status. Choose from [PENDING, PREPARING, READY, DELIVERED]");
            }
        }
        orderService.changeOrderStatus(orderIdForStatusChange, newStatus);
    }


    private static void addDishToOrder() {
        Order order = null;
        while (order == null) {
            viewAllOrders();
            System.out.println("Enter Order ID to add Dishes to the Order: ");
            int orderIDtoBeAddedTo = scanner.nextInt();
            scanner.nextLine();
            order = orderService.getOrderByID(orderIDtoBeAddedTo);
            if (order == null) {
                System.out.println("Order not found");
            }
        }
        Dish dish = null;
        viewAllDishes();
        while (dish == null) {
            System.out.println("Enter Dish ID to add to the Order: ");
            int dishIDtoBeAdded = scanner.nextInt();
            scanner.nextLine();
            dish = dishService.getDishByID(dishIDtoBeAdded);
            if (dish == null) {
                System.out.println("Invalid Dish ID");
            }
        }

        int quantityToBeAdded = 0;
        while (quantityToBeAdded <= 0) {
            System.out.println("Enter the quantity of the dish '" + dish.getName() + "' to be added to the Order: ");
            quantityToBeAdded = scanner.nextInt();
            scanner.nextLine();
            if (quantityToBeAdded <= 0) {
                System.out.println("Quantity must be greater than zero.");
            }
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setDishId(dish.getId());
        orderItem.setQuantity(quantityToBeAdded);
        orderItem.setPrice(dish.getPrice());
        orderItemService.addOrderItem(orderItem);
        orderService.updateOrderTotal(order.getId());
        System.out.println("Dish added successfully");
    }


    private static void viewAllOrders() {
        List<Order> orders = orderService.viewAllOrders();
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    private static void viewAllCustomers() {
        List<Customer> customers = customerService.viewAllCustomers();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private static void addCustomer() {
        Customer customer = new Customer();
        System.out.println("Enter Customer Name");
        String name = scanner.nextLine();
        customer.setName(name);
        System.out.println("Enter Customer Phone Number");
        String phoneNumber = scanner.nextLine();
        if (!phoneNumber.matches("\\d+")) {
            System.out.println("Invalid phone number. Digits only.");
            return;
        }
        customer.setPhoneNumber(phoneNumber);
        System.out.println("Enter Customer Email Address");
        String emailAddress = scanner.nextLine();
        customer.setEmail(emailAddress);
        customerService.addCustomer(customer);
        System.out.println("Customer added successfully");
    }

    private static void updateDish() {
        viewAllDishes();
        System.out.println("Enter Dish ID to update the Dish: ");
        int dishIDtoBeUpdated = scanner.nextInt();
        scanner.nextLine();
        Dish dish = dishService.getDishByID(dishIDtoBeUpdated);
        if (dish == null) {
            System.out.println("Dish ID with " + dishIDtoBeUpdated + " does not exist.");
            return;
        }
        System.out.println("Enter Dish Name to update the Dish Name: ");
        String dishNameToUpdate = scanner.nextLine();
        dish.setName(dishNameToUpdate);

        DishCategory category = null;
        while (category == null) {
            System.out.println("Choose Dish Category to update the Dish Category. Choose from the following: [APPETIZER, MAIN, DESSERT, DRINK]");
            String dishCategoryToUpdate = scanner.nextLine().toUpperCase();
            try {
                category = DishCategory.valueOf(dishCategoryToUpdate);
                dish.setCategory(category);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category! Please use one of: APPETIZER, MAIN, DESSERT, DRINK");
            }
        }

        System.out.println("Enter Dish Price to update the Dish Price: ");
        double priceToUpdate = scanner.nextDouble();
        scanner.nextLine();
        dish.setPrice(priceToUpdate);

        Boolean availability = null;
        while (availability == null) {
            System.out.println("Enter Dish Availability to update the availability of the Dish (TRUE if available, FALSE if unavailable)");
            String availabilityInput = scanner.nextLine().toUpperCase();
            if (availabilityInput.equals("TRUE")) {
                availability = true;
            } else if (availabilityInput.equals("FALSE")) {
                availability = false;
            } else System.out.println("Invalid Availability! Please use one of: TRUE, FALSE");
        }

        dishService.updateDish(dish);
    }

    private static void viewAllDishes() {
        List<Dish> dishes = dishService.viewAllDishes();
        for (Dish dish : dishes) {
            System.out.println(dish);
        }
    }


    private static void addDishToMenu() {
        Dish dish = new Dish();
        System.out.println("Enter Dish Name");
        String name = scanner.nextLine();
        dish.setName(name);
        DishCategory category = null;
        while (category == null) {
            System.out.println("Choose Dish Category from the following: [APPETIZER, MAIN, DESSERT, DRINK]");
            String categoryInput = scanner.nextLine().toUpperCase();
            try {
                category = DishCategory.valueOf(categoryInput);
                dish.setCategory(category);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category! Please use one of: APPETIZER, MAIN, DESSERT, DRINK");
            }
        }
        dish.setCategory(category);
        System.out.println("Enter Dish Price");
        double price = scanner.nextDouble();
        dish.setPrice(price);
        scanner.nextLine();
        Boolean availability = null;
        while (availability == null) {
            System.out.println("Enter Dish Availability (TRUE if available, FALSE if unavailable)");
            String availabilityInput = scanner.nextLine().toUpperCase();
            if (availabilityInput.equals("TRUE")) {
                availability = true;
            } else if (availabilityInput.equals("FALSE")) {
                availability = false;
            } else System.out.println("Invalid Availability! Please use one of: TRUE, FALSE");
        }
        dishService.addDish(dish);
        System.out.println("Dish added successfully");
    }

}
