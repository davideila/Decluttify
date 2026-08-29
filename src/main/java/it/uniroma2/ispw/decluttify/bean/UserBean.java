package it.uniroma2.ispw.decluttify.bean;

public class UserBean {

    private String username;
    private double rating;

    public UserBean(String username, double rating) {
        this.setUsername(username);
        this.setRating(rating);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return this.rating;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
