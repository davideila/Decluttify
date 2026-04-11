package it.uniroma2.ispw.decluttify.persistence.dao;

import it.uniroma2.ispw.decluttify.model.Barter;
import java.util.List;

public abstract class BarterDAO {
    public abstract List<Barter> retrieveBartersByUsername(String username);
    public abstract void createBarter(Barter barter);
    public abstract Barter retrieveBarterByID(int id);
    public abstract void updateBarter(Barter barter);
}
