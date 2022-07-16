package lk.pos.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.pos.dao.CrudUtil;
import lk.pos.dao.custom.CustomerDAO;
import lk.pos.entity.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class CustomerDAOImpl implements CustomerDAO {


    @Override
    public boolean add(Customer customer, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"INSERT INTO Customer VALUES(?,?,?,?)",customer.getId(),
                customer.getName(),customer.getAddress(),customer.getContact());
    }

    @Override
    public boolean delete(String id, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"DELETE FROM Customer WHERE id=?", id);
    }

    @Override
    public boolean update(Customer customer, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection, "UPDATE Customer SET name=?,address=?,contact=? WHERE id=?",customer.getName(),
                customer.getAddress(),customer.getContact(),customer.getId());
    }

    @Override
    public Customer search(String id, Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection,"SELECT * FROM Customer WHERE id =?",id);
        if (rst.next()){
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );
        }else {
            return null;
        }
    }


    @Override
    public ObservableList<Customer> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery(connection, "SELECT * FROM Customer");

        ObservableList<Customer> obList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Customer customer = new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );

            obList.add(customer);
        }
        return obList;
    }

    @Override
    public int customerCount(Connection connection) throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        ResultSet resultSet = CrudUtil.executeQuery(connection, "SELECT COUNT(*) FROM customer");
        while (resultSet.next()){
            numberRow = resultSet.getInt(1);
        }
        return numberRow;
    }
}
