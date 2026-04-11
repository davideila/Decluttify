package it.uniroma2.ispw.decluttify.utils;

import it.uniroma2.ispw.decluttify.bean.NotificationBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.patterns.Observer.Subject;
import java.util.ArrayList;
import java.util.List;

public class SessionManager extends Subject {
    private UserBean loggedUser;
    private boolean loggedIn;
    private static SessionManager instance = null;
    private List<NotificationBean>  notifications = new ArrayList<>();


    protected SessionManager(){
    }

    public static synchronized SessionManager getInstance() {
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    public UserBean getLoggedUser(){
        return loggedUser;
    }

    public void setLoggedUser(UserBean loggedUser){
        this.loggedUser = loggedUser;
    }

    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn){
        this.loggedIn = isLoggedIn;
    }

    public void login(UserBean userBean){
        this.setLoggedUser(userBean);
        this.setLoggedIn(true);
        notifyObservers();
    }

    public void logout(){
        this.setLoggedIn(false);
        this.setLoggedUser(null);
        notifyObservers();
    }

    public void setNotifications(List<NotificationBean> notificationBeans) {
        this.notifications = notificationBeans;
        this.notifyObservers();
    }

    public List<NotificationBean> getNotifications() {
        return notifications;
    }
}
