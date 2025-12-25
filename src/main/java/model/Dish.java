package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private int id;
    private String name;
    private double price;
    private DishCategory category;
    private boolean availability;

    public boolean isAvailable() {
        return availability;
    }
}
