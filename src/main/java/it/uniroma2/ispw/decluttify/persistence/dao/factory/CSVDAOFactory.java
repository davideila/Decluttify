package it.uniroma2.ispw.decluttify.persistence.dao.factory;

import it.uniroma2.ispw.decluttify.persistence.dao.*;
import it.uniroma2.ispw.decluttify.persistence.dao.csv.*;

public class CSVDAOFactory extends DAOFactory {

    @Override
    public BarterDAO createBarterDAO() {
        return new BarterDAOCSV();
    }

    @Override
    public ItemDAO createItemDAO() {
        return new ItemDAOCSV();
    }

    @Override
    public OfferDAO createOfferDAO() {
        return new OfferDAOCSV();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAOCSV();
    }

    @Override
    public NotificationDAO createNotificationDAO() {
        return new NotificationDAOCSV();
    }
}
