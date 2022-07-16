package lk.pos.bo.custom;

import javafx.collections.ObservableList;
import lk.pos.bo.SuperBO;
import lk.pos.dto.CustomerDTO;
import lk.pos.dto.ItemDTO;
import lk.pos.dto.OrderDetailsDTO;
import lk.pos.dto.OrdersDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public interface OrderBO extends SuperBO {

    boolean saveOrder(Connection connection, OrdersDTO ordersDTO) throws SQLException, ClassNotFoundException;

    boolean saveOrderDetail(Connection connection, OrdersDTO ordersDTO) throws SQLException, ClassNotFoundException;

    boolean updateQtyOnHand(Connection connection, String id, int qty) throws SQLException, ClassNotFoundException;

    ObservableList<OrdersDTO> getAllOrders(Connection connection) throws SQLException, ClassNotFoundException;

    ObservableList<OrderDetailsDTO> getAllOrderDetails(Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<OrderDetailsDTO> searchOrderDetails(String orderId, Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewOrderId(Connection connection)throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDTO> getAllCustomers(Connection connection)throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems(Connection connection)throws SQLException, ClassNotFoundException;

    int ordersCount(Connection connection) throws SQLException, ClassNotFoundException;

    OrdersDTO searchOrder(Connection connection, String id) throws SQLException, ClassNotFoundException;
}