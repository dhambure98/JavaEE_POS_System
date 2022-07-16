package lk.pos.dao.custom;

import lk.pos.dao.CrudDAO;
import lk.pos.entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public interface CustomerDAO extends CrudDAO<Customer, String, Connection> {

    int customerCount(Connection connection) throws SQLException, ClassNotFoundException;
}
