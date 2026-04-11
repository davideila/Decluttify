package it.uniroma2.ispw.decluttify.model;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import it.uniroma2.ispw.decluttify.exception.ModelException;

public class User {
    private String username;
    private String email;
    private double rating;
    private String passwordHash;

    //Constructors
    public User(String username, String password, double rating, String email, String pepper) {
        this.setUsername(username);
        this.setEmail(email);
        this.setRating(rating);
        this.setPasswordHash(password, pepper);
    }

    // Used when retrieving user from DB
    public User(String username, String passwordHash, double rating, String email) {
        this.setUsername(username);
        this.setEmail(email);
        this.setRating(rating);
        this.passwordHash = passwordHash;
    }

    //Business methods
    public boolean checkPassword(String inputPassword, String pepper) {
        return Password.check(inputPassword, this.passwordHash)
                .addPepper(pepper)
                .with(BcryptFunction.getInstance(Bcrypt.B, 10));
    }

    public void setPasswordHash(String password, String pepper) {
        if (password == null) {
            throw new ModelException("Password cannot be null");
        }
        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 10);

        Hash hash = Password.hash(password)
                .addPepper(pepper)
                .with(bcrypt);

        this.passwordHash = hash.getResult();
    }

    public void changePassword(String password){
        //TODO
    }

    public void deleteUser(){
        this.setUsername(null);
        this.setEmail(null);
        this.setRating(0);
        //TODO
    }

    //Getters and setters

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        if(username == null){
            throw new ModelException("Username cannot be null");
        }
        else {
            this.username = username;
        }
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public double getRating() {
        return rating;
    }

}