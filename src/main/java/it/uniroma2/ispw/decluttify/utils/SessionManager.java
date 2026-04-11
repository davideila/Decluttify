package it.uniroma2.ispw.decluttify.utils;

import it.uniroma2.ispw.decluttify.bean.NotificationBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.patterns.Observer.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SessionManager extends Subject {
    private UserBean loggedUser;
    private boolean loggedIn;
    private static SessionManager instance = null;
    private List<NotificationBean>  notifications = new ArrayList<>();
    private boolean loginLocked = false;


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

    public boolean isLoginLocked() {
        return loginLocked;
    }

    public void lockLogin(){
        this.loginLocked = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SessionManager.getInstance().unlockLogin();
            }
        }, 10000);
    }
    private void unlockLogin(){
        this.loginLocked = false;
    }
}
