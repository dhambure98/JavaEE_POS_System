package lk.pos.dao;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public interface CrudDAO <T, ID, C>extends SuperDAO{
    boolean add(T t, Connection connection) throws SQLException, ClassNotFoundException;

    boolean delete(ID id, Connection connection) throws SQLException, ClassNotFoundException;

    boolean update(T t, Connection connection) throws SQLException, ClassNotFoundException;

    T search(ID id, Connection connection) throws SQLException, ClassNotFoundException;

    ObservableList<T> getAll(Connection connection) throws SQLException, ClassNotFoundException;
}
