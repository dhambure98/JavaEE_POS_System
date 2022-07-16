package lk.pos.dao.custom;

import lk.pos.dao.CrudDAO;
import lk.pos.entity.Item;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public interface ItemDAO extends CrudDAO<Item, String, Connection> {

    boolean updateQtyOnHand(Connection connection, String id, int qty) throws SQLException, ClassNotFoundException;

    int itemCount(Connection connection) throws SQLException, ClassNotFoundException;
}
