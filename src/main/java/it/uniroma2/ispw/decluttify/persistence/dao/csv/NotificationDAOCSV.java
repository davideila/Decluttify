package it.uniroma2.ispw.decluttify.persistence.dao.csv;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Notification;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.NotificationDAO;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOCSV extends NotificationDAO {
    private final String notificationFilePath = PersistenceManager.getInstance().getCSVPathPrefix() + "notifications.csv";

    @Override
    public synchronized void createNotification(Notification notification) {
        if (PersistenceManager.getInstance().isDemoMode()) {
            return;
        }

        File notificationFile = new File(notificationFilePath);
        if (!notificationFile.exists()) {
            throw new DAOException("Error: Notifications file does not exist.");
        }

        if (notificationFile.length() == 0) {
            throw new DAOException("No header found in file notifications.");
        }

        try (RandomAccessFile raf = new RandomAccessFile(notificationFile, "rw")) {
            long fileLength = notificationFile.length();
            long pointer = fileLength - 1; // last char of the file

            while (pointer >= 0) {
                raf.seek(pointer);
                int b = raf.read();

                if (b == '\n' && pointer != fileLength - 1) { // '\n' = 10
                    break;
                }
                pointer--;
            }

            String line = raf.readLine();
            int id = parseLastNotificationId(line) + 1;

            if (raf.length() > 0) {
                raf.seek(raf.length() - 1);
                if (raf.read() != '\n') {
                    raf.writeBytes("\r\n"); // Add new line and return feed if missing
                }
            }

            raf.seek(raf.length());
            StringBuilder newRow = new StringBuilder();
            newRow.append(id).append(";")
                    .append(notification.getUsername()).append(";")
                    .append(notification.getMessage()).append(";")
                    .append(notification.getType()).append(";")
                    .append(notification.isRead()).append(";")
                    .append(notification.getCreatedAt()).append("\r\n");

            raf.write(newRow.toString().getBytes());

        } catch (IOException e) {
            throw new DAOException("Error: Impossible to save new barter in CSV.", e);
        }
    }

    @Override
    public void update(Notification notification) {
        if (PersistenceManager.getInstance().isDemoMode()) {
            return;
        }
        // not implemented
    }

    @Override
    public List<Notification> retrieveNotificationByUser(String username) {
        List<Notification> notifications = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(notificationFilePath))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] notificationData = line.split(";");

                if (notificationData[1].equals(username)) {
                    notifications.add(new Notification(
                            Integer.parseInt(notificationData[0]),
                            notificationData[1],
                            notificationData[2],
                            notificationData[3],
                            Boolean.parseBoolean(notificationData[4]),
                            LocalDateTime.parse(notificationData[5])
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: notification database file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("System error while reading notification CSV file.", e);
        }

        return notifications;
    }

    // ##################################################################################
    // Private helper methods as requested by sonarqube to eliminate nested try blocks

    private int parseLastNotificationId(String line) {
        if (line != null && !line.isEmpty()) {
            try {
                return Integer.parseInt(line.split(";")[0].trim());
            } catch (NumberFormatException e) {
                // No previous notifications, only header present
                return 0;
            }
        }
        return 0;
    }
}
