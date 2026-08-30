package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Notification;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.NotificationDAO;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOJDBC extends NotificationDAO {

    @Override
    public void createNotification(Notification notification) {
        if (PersistenceManager.getInstance().isDemoMode()){
            return;
        }
        Connection connection = PersistenceManager.getInstance().getConnection();
        try {
            int generatedId = InsertQueries.insertNotification(
                    connection,
                    notification.getUsername(),
                    notification.getMessage(),
                    notification.getType());
            notification.setId(generatedId);
        } catch (SQLException e) {
            throw new DAOException("Database error while creating notification for user: " + notification.getUsername(), e);
        }
    }

    @Override
    public void update(Notification notification) {
        if (PersistenceManager.getInstance().isDemoMode()){
            return;
        }
        Connection connection = PersistenceManager.getInstance().getConnection();
        try {
            int rowsAffected = UpdateQueries.updateNotification(connection, notification.getId());
            if (rowsAffected != 1) {
                throw new DAOException("Update failed: notification with ID " + notification.getId() + " not found.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database error while updating notification ID: " + notification.getId(), e);
        }
    }

    @Override
    public List<Notification> retrieveNotificationByUser(String username) {
        List<Notification> notifications = new ArrayList<>();
        Connection connection = PersistenceManager.getInstance().getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(SelectQueries.SELECT_NOTIFICATIONS_BY_USER)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new Notification(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getBoolean(5),
                            rs.getObject(6, LocalDateTime.class))
                    );
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Database error while retrieving notifications for user: " + username, e);
        }
        return notifications;
    }

}
