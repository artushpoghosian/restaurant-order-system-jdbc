package service;

import db.DBConnectionProvider;
import model.Dish;
import model.DishCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DishService {
    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public void addDish(Dish dish) {
        String sql = "INSERT INTO dish (name, category, price, availability) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, dish.getName());
            preparedStatement.setString(2, dish.getCategory().name());
            preparedStatement.setDouble(3, dish.getPrice());
            preparedStatement.setBoolean(4, dish.isAvailability());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                dish.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Dish> viewAllDishes() {
        String sql = "select * from dish";
        List<Dish> dishes = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                dishes.add(getDishesFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    private Dish getDishesFromResultSet(ResultSet resultSet) throws SQLException {
        Dish dish = new Dish();
        dish.setId(resultSet.getInt("id"));
        dish.setName(resultSet.getString("name"));
        String categoryStr = resultSet.getString("category");
        DishCategory category = DishCategory.valueOf(categoryStr);
        dish.setCategory(category);
        dish.setPrice(resultSet.getDouble("price"));
        dish.setAvailability(resultSet.getBoolean("availability"));
        return dish;
    }

    public void removeDish(int dishID) {
        String sql = "delete from dish where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, dishID);
            int i = preparedStatement.executeUpdate();
            if (i == 0) {
                System.out.println("Dish with ID " + dishID + " does not exist");
            } else {
                System.out.println("Dish with ID " + dishID + " has been removed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDish(Dish dish) {
        String sql = "UPDATE dish SET name = ?, category = ?, price = ?, availability = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, dish.getName());
            preparedStatement.setString(2, dish.getCategory().name());
            preparedStatement.setDouble(3, dish.getPrice());
            preparedStatement.setBoolean(4, dish.isAvailable());
            preparedStatement.setInt(5, dish.getId());

            int i = preparedStatement.executeUpdate();
            if (i == 0) {
                System.out.println("Dish with ID " + dish.getId() + " does not exist");
            } else {
                System.out.println("Dish with ID " + dish.getId() + " has been updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dish getDishByID(int dishID) {
        String sql = "SELECT * FROM dish WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, dishID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("id"));
                dish.setName(resultSet.getString("name"));
                dish.setCategory(DishCategory.valueOf(resultSet.getString("category")));
                dish.setPrice(resultSet.getDouble("price"));
                dish.setAvailability(resultSet.getBoolean("availability"));
                return dish;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Dish> getDishesByCategory(DishCategory dishCategory) {
        String sql = "select * from dish where category = ?";
        List<Dish> dishes = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, dishCategory.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                dishes.add(getDishesFromResultSet(resultSet));
            }
            if (dishes.isEmpty()) {
                System.out.println("No dishes in the category '" + dishCategory.name() + "' found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }
}
