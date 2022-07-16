package lk.pos.dao.custom;

import lk.pos.dao.CrudDAO;
import lk.pos.entity.Orders;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public interface OrderDAO extends CrudDAO<Orders, String, Connection> {

    boolean ifOrderExist(String oid, Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewOrderId(Connection connection) throws SQLException, ClassNotFoundException;

    int ordersCount(Connection connection) throws SQLException, ClassNotFoundException;
}
