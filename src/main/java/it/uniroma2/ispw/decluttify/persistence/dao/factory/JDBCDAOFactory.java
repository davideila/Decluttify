package it.uniroma2.ispw.decluttify.persistence.dao.factory;

import it.uniroma2.ispw.decluttify.persistence.dao.*;
import it.uniroma2.ispw.decluttify.persistence.dao.jdbc.*;

public class JDBCDAOFactory extends DAOFactory {

    @Override
    public BarterDAO createBarterDAO() {
        return new BarterDAOJDBC();
    }

    @Override
    public ItemDAO createItemDAO() {
        return new ItemDAOJDBC();
    }

    @Override
    public OfferDAO createOfferDAO() {
        return new OfferDAOJDBC();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAOJDBC();
    }

    @Override
    public NotificationDAO createNotificationDAO() {
        return new NotificationDAOJDBC();
    }
}
