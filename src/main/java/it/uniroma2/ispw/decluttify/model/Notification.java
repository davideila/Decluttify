package it.uniroma2.ispw.decluttify.model;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private String username;
    private String message;
    private String type;
    private boolean read;
    private final LocalDateTime createdAt;

    // Constructors

    public Notification(String username, String message, String type) {
        this.setUsername(username);
        this.setMessage(message);
        this.setType(type);
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    // Used when getting data from persistence
    public Notification(int id, String username, String message, String type, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.setUsername(username);
        this.setMessage(message);
        this.setType(type);
        this.setRead(read);
        this.createdAt = createdAt;
    }

    public void read(){
        this.setRead(true);
    }

    // Getter and Setter

    public int getId() { return id; }
    public String getMessage() { return message; }
    public void setMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification message cannot be null or empty.");
        }
        this.message = message;
    }

    public String getType() { return type; }
    public void setType(String type){
        if (type == null){
            throw new IllegalArgumentException("Notification type cannot be null.");
        }
        String formattedType = type.toUpperCase().trim();
        switch (formattedType){
            case "OFFER":
            case "BARTER":
                this.type = formattedType;
                break;
            default:
                throw new IllegalArgumentException("Invalid notification type: " + formattedType);
        }
    }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getUsername() {return username; }
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification recipient username cannot be null or empty.");
        }
        this.username = username;
    }
    public void setId(int id) { this.id = id; }
}