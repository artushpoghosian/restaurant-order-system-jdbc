package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private int customerId;
    private LocalDate orderDate;
    private double totalPrice;
    private OrderStatus orderStatus;
}
