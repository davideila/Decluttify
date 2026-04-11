package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import java.time.LocalDateTime;

public class Notification {
    private int id;
    private String username;
    private final String message;
    private String type; // "OFFER" or "BARTER"
    private boolean read;
    private final LocalDateTime createdAt;

    // Constructors

    public Notification(String username, String message, String type) {
        this.username = username;
        this.message = message;
        this.setType(type);
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    public Notification(int id, String username, String message, String type, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.setType(type);
        this.read = read;
        this.createdAt = createdAt;
    }

    // Getter and Setter

    public int getId() { return id; }
    public String getMessage() { return message; }
    public String getType() { return type; }
    public void setType(String type){
        if (type == null){
            throw new ModelException("Notification type cannot be null.");
        }
        String formattedType = type.toUpperCase().trim();
        switch (formattedType){
            case "OFFER":
            case "BARTER":
                this.type = formattedType;
                break;
            default:
                throw new ModelException("Invalid notification type: " + formattedType);
        }
    }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getUsername() {return username; }
    public void setUsername(String username) { this.username = username; }
    public void setId(int id) { this.id = id; }
}