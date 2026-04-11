package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.model.Notification;
import it.uniroma2.ispw.decluttify.persistence.dao.NotificationDAO;
import it.uniroma2.ispw.decluttify.utils.ConfigReader;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.dao.UserDAO;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import it.uniroma2.ispw.decluttify.utils.BeanConverter;
import java.util.List;

public class LoginController {

    public boolean login(String username, String inputPassword) {
        UserDAO userDAO= DAOFactory.getDAOFactory().createUserDAO();
        User user = userDAO.retrieveUserByUsername(username);
        if(user == null){
            return false;
        }
        else {
            if (user.checkPassword(inputPassword, ConfigReader.getInstance().getPepper())) {
                SessionManager.getInstance().login(BeanConverter.toUserBean(user));
                this.checkForNotifications();
                return true;
            } else return false;
        }
    }

    public void logout(){
        if (SessionManager.getInstance().isLoggedIn()){
            SessionManager.getInstance().logout();
        }
    }

    public void signOn(){//TODO
    }

    public void signOut(){//TODO
    }

    public void checkForNotifications() {
        NotificationDAO notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
        List<Notification> notifications = notificationDAO.retrieveNotificationByUser(SessionManager.getInstance().getLoggedUser().getUsername());
        SessionManager.getInstance().setNotifications(BeanConverter.toNotificationBeanList(notifications));
    }
}
