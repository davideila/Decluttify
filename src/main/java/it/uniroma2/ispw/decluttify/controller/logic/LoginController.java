package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.exception.LoginException;
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
        if (sessionManager.isLoginLocked()){
            throw new LoginException("Too many failed attempts. Please try again later.");
        }
        if(username == null || inputPassword == null || username.isEmpty() || inputPassword.isEmpty()){
            this.sessionManager.failLoginAttempt();
            throw new LoginException("Please provide username and password.");
        }
        if (sessionManager.isLoggedIn()){
            throw new LoginException("You are already logged in.");
        }

        User user;
        try {
            user = userDAO.retrieveUserByUsername(username);
        }catch (DAOException e){
            throw new DecluttifyException("Unable to complete the operation due to a system error.", e);
        }

        if(user == null){
            sessionManager.failLoginAttempt();
            return false;
        }
        else {
            if (user.checkPassword(inputPassword, ConfigReader.getInstance().getPepper())) {
                sessionManager.login(BeanConverter.toUserBean(user));
                this.checkForNotifications();
                return true;
            } else {
                sessionManager.failLoginAttempt();
                return false;
            }
        }
    }

    public void logout(){
        if (sessionManager.isLoggedIn()){
            sessionManager.logout();
        }
    }

    public void checkForNotifications() {
        try {
            List<Notification> notifications = notificationDAO.retrieveNotificationByUser(sessionManager.getLoggedUser().getUsername());
            sessionManager.setNotifications(BeanConverter.toNotificationBeanList(notifications));
        }
        catch (DAOException e){
            throw new DecluttifyException("Unable to complete the operation due to a system error.", e);
        }
    }
}
