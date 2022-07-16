package lk.pos.bo.custom;

import javafx.collections.ObservableList;
import lk.pos.bo.SuperBO;
import lk.pos.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public interface CustomerBO extends SuperBO {

    boolean addCustomer(Connection connection, CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;

    ObservableList<CustomerDTO> getAllCustomer(Connection connection) throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String id, Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(Connection connection, String id) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(Connection connection, CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;

    int countCustomer(Connection connection) throws SQLException, ClassNotFoundException;

}
