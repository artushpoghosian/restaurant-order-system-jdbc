package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItem {
    private int id;
    private int orderId;
    private int dishId;
    private int quantity;
    private double price;
}
