package service;

import db.DBConnectionProvider;
import model.Order;
import model.OrderStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public Order createOrder(int customerIDtoForCreatingOrder) {
        String sql = "INSERT INTO `order` (customer_id, total_price, status) VALUES (?, ?, ?)";
        Order order = new Order();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, customerIDtoForCreatingOrder);
            preparedStatement.setDouble(2, 0);
            preparedStatement.setString(3, OrderStatus.PENDING.name());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getInt(1));
            }
            order.setCustomerId(customerIDtoForCreatingOrder);
            order.setOrderStatus(OrderStatus.PENDING);
            System.out.println("Order created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public Order getOrderByID(int orderIDtoBeAddedTo) {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderIDtoBeAddedTo);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setCustomerId(resultSet.getInt("customer_id"));
                order.setOrderDate(resultSet.getDate("order_date").toLocalDate());
                order.setTotalPrice(resultSet.getDouble("total_price"));
                order.setOrderStatus(OrderStatus.valueOf(resultSet.getString("status")));
                return order;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> viewAllOrders() {
        String sql = "select * from `order`";
        List<Order> orders = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                orders.add(getOrdersFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private Order getOrdersFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setCustomerId(resultSet.getInt("customer_id"));
        order.setOrderDate(resultSet.getDate("order_date").toLocalDate());
        order.setTotalPrice(resultSet.getDouble("total_price"));
        order.setOrderStatus(OrderStatus.valueOf(resultSet.getString("status")));
        return order;
    }

    public void updateOrderTotal(int orderID) {
        String sql = "update `order` o set o.total_price = (" +
                "select sum(oi.price) from order_item oi where oi.order_id =?)" +
                "where o.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderID);
            preparedStatement.setInt(2, orderID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean changeOrderStatus(int orderID, OrderStatus newStatus) {
        Order order = getOrderByID(orderID);
        if (order == null) {
            System.out.println("No order found with id " + orderID);
            return false;
        }

        OrderStatus currentStatus = order.getOrderStatus();
        boolean allowed = false;
        switch (currentStatus) {
            case PENDING:
                allowed = (newStatus == OrderStatus.PREPARING);
                break;
            case PREPARING:
                allowed = (newStatus == OrderStatus.READY);
                break;
            case READY:
                allowed = (newStatus == OrderStatus.DELIVERED);
                break;
            case DELIVERED:
                allowed = false;
                break;
        }

        if (!allowed) {
            System.out.println("Cannot change status from " + currentStatus + " to " + newStatus);
            return false;
        }

        String sql = "update `order` set status = ? where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newStatus.name());
            preparedStatement.setInt(2, orderID);
            preparedStatement.executeUpdate();
            System.out.println("Order status changed successfully");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}