package lk.pos.bo.custom;

import javafx.collections.ObservableList;
import lk.pos.bo.SuperBO;
import lk.pos.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public interface ItemBO extends SuperBO {

    boolean addItem(Connection connection, ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    ObservableList<ItemDTO> getAllItem(Connection connection) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String itemCode, Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteItem(Connection connection, String itemCode) throws SQLException, ClassNotFoundException;

    boolean updateItem(Connection connection, ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    int countItem(Connection connection) throws SQLException, ClassNotFoundException;
}

