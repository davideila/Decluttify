package it.uniroma2.ispw.decluttify.persistence.dao.factory;

import it.uniroma2.ispw.decluttify.persistence.dao.*;
import it.uniroma2.ispw.decluttify.utils.ConfigReader;

public abstract class DAOFactory {

    private static DAOFactory me = null;

    protected DAOFactory(){
    }

    public static synchronized DAOFactory getDAOFactory(){
        if ( me == null ){
            switch (ConfigReader.getInstance().getPersistenceType()) {
                case "mysql","MYSQL":
                    me = new JDBCDAOFactory();
                    break;
                case "csv","CSV":
                    me = new CSVDAOFactory();
                    break;
            }
        }
        return me;
    }

    public abstract BarterDAO createBarterDAO();
    public abstract ItemDAO createItemDAO();
    public abstract OfferDAO createOfferDAO();
    public abstract UserDAO createUserDAO();
    public abstract NotificationDAO createNotificationDAO();
}
