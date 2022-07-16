package lk.pos.bo.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.pos.bo.custom.ItemBO;
import lk.pos.dao.DAOFactory;
import lk.pos.dao.custom.ItemDAO;
import lk.pos.dto.ItemDTO;
import lk.pos.entity.Item;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class ItemBOImpl implements ItemBO {

    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean addItem(Connection connection, ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        Item item = new Item(
                itemDTO.getItemCode(),
                itemDTO.getItemName(),
                itemDTO.getQtyOnHand(),
                itemDTO.getUnitPrice()
        );

        return itemDAO.add(item, connection);
    }

    @Override
    public ObservableList<ItemDTO> getAllItem(Connection connection) throws SQLException, ClassNotFoundException {
        ObservableList<Item> all = itemDAO.getAll(connection);

        ObservableList<ItemDTO> obList = FXCollections.observableArrayList();

        for (Item temp : all){
            ItemDTO itemDTO = new ItemDTO(
                    temp.getItemCode(),
                    temp.getName(),
                    temp.getQtyOnHand(),
                    temp.getPrice()
            );

            obList.add(itemDTO);
        }
        return obList;
    }

    @Override
    public ItemDTO searchItem(String itemCode, Connection connection) throws SQLException, ClassNotFoundException {

        Item item = itemDAO.search(itemCode, connection);

        ItemDTO itemDTO = new ItemDTO(
                item.getItemCode(), item.getName(), item.getQtyOnHand(), item.getPrice()
        );

        return itemDTO;
    }

    @Override
    public boolean deleteItem(Connection connection, String itemCode) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(itemCode, connection);
    }

    @Override
    public boolean updateItem(Connection connection, ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        Item item = new Item(
                itemDTO.getItemCode(), itemDTO.getItemName(), itemDTO.getQtyOnHand(), itemDTO.getUnitPrice()
        );

        return itemDAO.update(item, connection);
    }

    @Override
    public int countItem(Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.itemCount(connection);
    }
}
