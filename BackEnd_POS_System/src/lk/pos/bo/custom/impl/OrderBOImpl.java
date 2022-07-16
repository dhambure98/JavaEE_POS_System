package lk.pos.bo.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.pos.bo.custom.OrderBO;
import lk.pos.dao.DAOFactory;
import lk.pos.dao.custom.CustomerDAO;
import lk.pos.dao.custom.ItemDAO;
import lk.pos.dao.custom.OrderDAO;
import lk.pos.dao.custom.OrderDetailsDAO;
import lk.pos.dto.CustomerDTO;
import lk.pos.dto.ItemDTO;
import lk.pos.dto.OrderDetailsDTO;
import lk.pos.dto.OrdersDTO;
import lk.pos.entity.OrderDetails;
import lk.pos.entity.Orders;

import javax.activation.DataSource;
import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class OrderBOImpl implements OrderBO {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERS);
    private final OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    @Override
    public boolean saveOrder(Connection connection, OrdersDTO ordersDTO) {

        Connection con = null;

        try {

            con = connection;

            connection.setAutoCommit(false);

            boolean orderAvailable = false;
            orderAvailable = orderDAO.ifOrderExist(ordersDTO.getOrderId(), connection);

            if (orderAvailable) {
                return false;
            }

            Orders orders = new Orders(ordersDTO.getOrderId(), ordersDTO.getcId(), ordersDTO.getOrderDate(),
                    ordersDTO.getTotal(), ordersDTO.getDiscount(), ordersDTO.getSubTotal());

            boolean orderAdded = orderDAO.add(orders, connection);

            if (orderAdded) {

                for (OrderDetailsDTO item : ordersDTO.getOrderDetail()) {
                    OrderDetails orderDetails = new OrderDetails(item.getoId(), item.getiCode(), item.getoQty(), item.getPrice(), item.getTotal());
                    boolean ifOrderDetailSaved = orderDetailsDAO.add(orderDetails, connection);

                    if (!ifOrderDetailSaved) {
                        connection.rollback();
                        return false;
                    }

                }

                for (OrderDetailsDTO item : ordersDTO.getOrderDetail()) {
                    boolean ifUpdateQty = itemDAO.updateQtyOnHand(connection, item.getiCode(), item.getoQty());

                    if (!ifUpdateQty) {
                        connection.rollback();
                        return false;
                    }

                }

            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return  true;
    }

    @Override
    public boolean saveOrderDetail(Connection connection, OrdersDTO ordersDTO) throws SQLException, ClassNotFoundException {

        for (OrderDetailsDTO item : ordersDTO.getOrderDetail()) {
            System.out.println(2);
            OrderDetails orderDetails = new OrderDetails(
                    item.getoId(), item.getiCode(), item.getoQty(), item.getPrice(), item.getTotal());

            boolean ifOrderDetailSaved = orderDetailsDAO.add(orderDetails,
                    connection
            );
            if (ifOrderDetailSaved){
                return updateQtyOnHand(connection, item.getiCode(), item.getoQty());
            }else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean updateQtyOnHand(Connection connection, String id, int qty) throws SQLException, ClassNotFoundException {
        return itemDAO.updateQtyOnHand(connection, id, qty);
    }


    @Override
    public ObservableList<OrdersDTO> getAllOrders(Connection connection) throws SQLException, ClassNotFoundException {
        ObservableList<Orders> orders = orderDAO.getAll(connection);

        ObservableList<OrdersDTO> obList = FXCollections.observableArrayList();

        for (Orders temp : orders) {
            OrdersDTO ordersDTO = new OrdersDTO(
                    temp.getOrderId(),temp.getcId(),temp.getOrderDate(),temp.getTotal(), temp.getDiscount(), temp.getSubTotal()
            );

            obList.add(ordersDTO);
        }
        return obList;
    }

    @Override
    public ObservableList<OrderDetailsDTO> getAllOrderDetails(Connection connection) throws SQLException, ClassNotFoundException {
        ObservableList<OrderDetails> orderDetails = orderDetailsDAO.getAll(connection);

        ObservableList<OrderDetailsDTO> obList = FXCollections.observableArrayList();

        for (OrderDetails temp : orderDetails) {
            OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(
                    temp.getoId(), temp.getiCode(), temp.getQty(), temp.getPrice(), temp.getTotal()
            );

            obList.add(orderDetailsDTO);
        }
        return obList;
    }

    @Override
    public ArrayList<OrderDetailsDTO> searchOrderDetails(String orderId, Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetails> orderDetails = orderDetailsDAO.searchOrderDetail(orderId, connection);
        ArrayList<OrderDetailsDTO> orderDetailDTOS = new ArrayList<>();
        for (OrderDetails orderDetail : orderDetails) {
            orderDetailDTOS.add(new OrderDetailsDTO(
                    orderDetail.getoId(),
                    orderDetail.getiCode(),
                    orderDetail.getQty(),
                    orderDetail.getPrice(),
                    orderDetail.getTotal()
            ));
        }
        return orderDetailDTOS;
    }


    @Override
    public String generateNewOrderId(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewOrderId(connection);
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers(Connection connection) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<ItemDTO> getAllItems(Connection connection) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public int ordersCount(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.ordersCount(connection);
    }

    @Override
    public OrdersDTO searchOrder(Connection connection, String id) throws SQLException, ClassNotFoundException {
        Orders order = orderDAO.search(id, connection);
        System.out.println(order);
        if (order != null) {
            return new OrdersDTO(order.getOrderId(), order.getcId(), order.getOrderDate(), order.getTotal(), order.getDiscount(),order.getSubTotal());
        }
        return null;
    }

}
