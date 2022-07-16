package lk.pos.dao;

import lk.pos.dao.custom.impl.CustomerDAOImpl;
import lk.pos.dao.custom.impl.ItemDAOImpl;
import lk.pos.dao.custom.impl.OrderDAOImpl;
import lk.pos.dao.custom.impl.OrderDetailsDAOImpl;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    public SuperDAO getDAO(DAOTypes types){

        switch (types){
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case ORDERS:
                return new OrderDAOImpl();
            case ORDERDETAILS:
                return new OrderDetailsDAOImpl();
            default:
                return null;
        }
    }

    public enum DAOTypes{
        CUSTOMER, ITEM, ORDERS, ORDERDETAILS
    }
}
