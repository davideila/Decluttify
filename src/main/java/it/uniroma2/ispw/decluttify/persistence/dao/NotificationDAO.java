package it.uniroma2.ispw.decluttify.persistence.dao;

import it.uniroma2.ispw.decluttify.model.Notification;
import java.util.List;

public abstract class NotificationDAO {
    public abstract void createNotification(Notification notification);
    public abstract void update(Notification notification);
    public abstract List<Notification> retrieveNotificationByUser(String username);
}
