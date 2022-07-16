package lk.pos.dao.custom;

import lk.pos.dao.CrudDAO;
import lk.pos.entity.OrderDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public interface OrderDetailsDAO extends CrudDAO<OrderDetails, String, Connection> {
    ArrayList<OrderDetails> searchOrderDetail(String id, Connection connection) throws SQLException, ClassNotFoundException;
}