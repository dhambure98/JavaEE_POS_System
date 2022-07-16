package lk.pos.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.pos.dao.CrudUtil;
import lk.pos.dao.custom.OrderDAO;
import lk.pos.entity.Orders;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class OrderDAOImpl implements OrderDAO {
    @Override
    public boolean add(Orders orders, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"INSERT INTO Orders VALUES (?,?,?,?,?,?)",orders.getOrderId(),
                orders.getcId(), orders.getOrderDate(), orders.getTotal(), orders.getDiscount(),
                orders.getSubTotal());
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Orders orders, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Orders search(String id, Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery(connection,"SELECT * FROM orders WHERE orderId=?", id);
        Orders order = null;
        while (resultSet.next()) {
            order = new Orders(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDate(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6)
            );
        }
        return order;
    }

    @Override
    public ObservableList<Orders> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery(connection, "SELECT * FROM Orders");

        ObservableList<Orders> obList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Orders orders = new Orders(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDate(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6)
            );

            obList.add(orders);
        }
        return obList;
    }

    @Override
    public boolean ifOrderExist(String oid, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewOrderId(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery(connection, "SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1");

        if (resultSet.next()){
            return resultSet.getString(1);
        }else {
            //return null;
            return "O-0001";
        }
    }

    @Override
    public int ordersCount(Connection connection) throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        ResultSet resultSet = CrudUtil.executeQuery(connection, "SELECT COUNT(*) FROM orders");
        while (resultSet.next()){
            numberRow = resultSet.getInt(1);
        }
        return numberRow;
    }
}
