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

    private SessionManager sessionManager;
    private final UserDAO userDAO;
    private final NotificationDAO notificationDAO;

    public LoginController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.userDAO = DAOFactory.getDAOFactory().createUserDAO();
        this.notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
    }

    public boolean login(String username, String inputPassword) {
        if(username == null || inputPassword == null){
            throw new DecluttifyException("Please provide username and password.");
        }
        if (sessionManager.isLoggedIn()){
            throw new DecluttifyException("You are already logged in.");
        }
        if (sessionManager.isLoginLocked()){

        }
        User user = userDAO.retrieveUserByUsername(username);
        if(user == null){
            sessionManager.login(null, false);
            throw new DecluttifyException("User " + username + " not found.");
        }
        else {
            if (user.checkPassword(inputPassword, ConfigReader.getInstance().getPepper())) {
                sessionManager.login(BeanConverter.toUserBean(user), true);
                this.checkForNotifications();
                return true;
            } else return false;
        }
    }

    public void logout(){
        if (sessionManager.isLoggedIn()){
            sessionManager.logout();
        }
    }

    public void signOn(){//TODO
    }

    public void signOut(){//TODO
    }

    public void checkForNotifications() {
        List<Notification> notifications = notificationDAO.retrieveNotificationByUser(sessionManager.getLoggedUser().getUsername());
        sessionManager.setNotifications(BeanConverter.toNotificationBeanList(notifications));
    }
}
