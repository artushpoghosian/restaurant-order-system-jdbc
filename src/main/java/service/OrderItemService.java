package service;

import db.DBConnectionProvider;
import model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderItemService {
    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public void addOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_item (order_id, dish_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, orderItem.getOrderId());
            preparedStatement.setInt(2, orderItem.getDishId());
            preparedStatement.setInt(3, orderItem.getQuantity());
            preparedStatement.setDouble(4, orderItem.getPrice());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderItem.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double calculateOrderTotalPrice(int orderID) {
        String sql = "select ifnull(sum(oi.price * oi.quantity), 0) as total_price from order_item oi where oi.order_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("total_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}