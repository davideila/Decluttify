package it.uniroma2.ispw.decluttify.utils;

import it.uniroma2.ispw.decluttify.bean.NotificationBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.patterns.Observer.Subject;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SessionManager extends Subject {
    private UserBean loggedUser;
    private boolean loggedIn;
    private List<NotificationBean>  notifications = new ArrayList<>();
    private boolean loginLocked;
    private int loginTries;

    public SessionManager() {
        this.loggedUser = null;
        this.loggedIn = false;
        this.loginLocked = false;
        loginTries = 0;
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
                loginTries++;
                if (loginTries == 3) {
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
                unlockLogin();
            }
        }, 10000);
    }
    private void unlockLogin(){
        this.loginLocked = false;
        this.loginTries = 0;
    }
}
