package lk.pos.bo.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.pos.bo.custom.CustomerBO;
import lk.pos.dao.DAOFactory;
import lk.pos.dao.custom.CustomerDAO;
import lk.pos.dto.CustomerDTO;
import lk.pos.entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public boolean addCustomer(Connection connection, CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(
                customerDTO.getCusId(),customerDTO.getCusName(),customerDTO.getCusAddress(),customerDTO.getCusContact()

        );
        return customerDAO.add(customer,connection);
    }

    @Override
    public ObservableList<CustomerDTO> getAllCustomer(Connection connection) throws SQLException, ClassNotFoundException {
        ObservableList<Customer> customers = customerDAO.getAll(connection);

        ObservableList<CustomerDTO> obList = FXCollections.observableArrayList();

        for (Customer temp : customers) {
            CustomerDTO customerDTO = new CustomerDTO(
                    temp.getId(),temp.getName(),temp.getAddress(),temp.getContact()
            );

            obList.add(customerDTO);
        }
        return obList;

    }

    @Override
    public CustomerDTO searchCustomer(String id, Connection connection) throws SQLException, ClassNotFoundException {

        Customer customer = customerDAO.search(id, connection);

        return new CustomerDTO(
                customer.getId(),customer.getName(),customer.getAddress(),customer.getContact()
        );
    }

    @Override
    public boolean deleteCustomer(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id, connection);
    }

    @Override
    public boolean updateCustomer(Connection connection, CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(
                customerDTO.getCusId(),customerDTO.getCusName(),customerDTO.getCusAddress(),customerDTO.getCusContact()

        );
        return customerDAO.update(customer,connection);
    }

    @Override
    public int countCustomer(Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.customerCount(connection);
    }

}

