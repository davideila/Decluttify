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
    private List<NotificationBean>  notifications = new ArrayList<>();
    private boolean loginLocked;
    private int loginAttempts;
    private final int MAX_LOGIN_ATTEMPTS = 3;
    private final int LOCKED_LOGIN_DELAY = 10000;

    public SessionManager() {
        this.loggedUser = null;
        this.loggedIn = false;
        this.loginLocked = false;
        loginAttempts = 0;
    }

    public void login(UserBean userBean, boolean success) {
        if(isLoginLocked()){
        }
        else {
            if (success) {
                this.setLoggedUser(userBean);
                this.setLoggedIn(true);
                notifyObservers();
            } else {
                this.setLoggedUser(null);
                this.setLoggedIn(false);
                loginAttempts++;
                if (loginAttempts == MAX_LOGIN_ATTEMPTS) {
                    lockLogin();
                }
            }
        }
    }

    public void logout(){
        this.setLoggedIn(false);
        this.setLoggedUser(null);
        notifyObservers();
    }

    private void lockLogin(){
        this.loginLocked = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                unlockLogin();
            }
        }, LOCKED_LOGIN_DELAY);
    }
    private void unlockLogin(){
        this.loginLocked = false;
        this.loginAttempts = 0;
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
}
