package it.uniroma2.ispw.decluttify.persistence.dao;

import it.uniroma2.ispw.decluttify.model.User;

public abstract class UserDAO {
    public abstract User retrieveUserByUsername(String username);
}
