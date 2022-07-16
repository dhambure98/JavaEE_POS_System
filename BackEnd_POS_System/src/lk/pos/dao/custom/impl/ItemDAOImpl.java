package lk.pos.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.pos.dao.CrudUtil;
import lk.pos.dao.custom.ItemDAO;
import lk.pos.entity.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class ItemDAOImpl implements ItemDAO {


    @Override
    public boolean add(Item item, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection, "Insert into Item values(?,?,?,?)",item.getItemCode(),
                item.getName(),item.getQtyOnHand(),item.getPrice());
    }

    @Override
    public boolean delete(String code, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"Delete from Item where itemCode=?",code);
    }

    @Override
    public boolean update(Item item, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection, "UPDATE Item SET name=?,qtyOnHand=?,price=? WHERE itemCode=?", item.getName(),
                item.getQtyOnHand(), item.getPrice(), item.getItemCode());
    }

    @Override
    public Item search(String code, Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery(connection, "SELECT * FROM Item WHERE itemCode =?", code);
        if (resultSet.next()){
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            );
        }else {
            return null;
        }
    }

    @Override
    public ObservableList<Item> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery(connection, "SELECT * FROM Item");

        ObservableList<Item> obList = FXCollections.observableArrayList();

        while (resultSet.next()){
            Item item = new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            );

            obList.add(item);
        }
        return obList;
    }

    @Override
    public boolean updateQtyOnHand(Connection connection, String id, int qty) throws SQLException, ClassNotFoundException {
        return  CrudUtil.executeUpdate(connection, "UPDATE Item SET qtyOnHand=(qtyOnHand - "+ qty +")WHERE itemCode=?", id);
    }

    @Override
    public int itemCount(Connection connection) throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        ResultSet resultSet = CrudUtil.executeQuery(connection, "SELECT COUNT(*) FROM item");
        while (resultSet.next()){
            numberRow = resultSet.getInt(1);
        }
        return numberRow;
    }
}

