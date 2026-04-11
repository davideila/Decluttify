package it.uniroma2.ispw.decluttify.bean;

public class NotificationBean {
    private int id;
    private String message;
    private String type; // "OFFER" or "BARTER"
    private String timestamp;
    private boolean isRead;

    public NotificationBean(int id, String message, String type, String timestamp, boolean isRead) {
        this.setId(id);
        this.setMessage(message);
        this.setType(type);
        this.setTimestamp(timestamp);
        this.setRead(isRead);
    }

    // Getter and Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}