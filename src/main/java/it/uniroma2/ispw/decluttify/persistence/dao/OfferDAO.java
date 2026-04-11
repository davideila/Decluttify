package it.uniroma2.ispw.decluttify.persistence.dao;

import it.uniroma2.ispw.decluttify.model.Offer;
import java.util.List;

public abstract class OfferDAO {
    public abstract void createOffer(Offer offer);
    public abstract void deleteOffer(int id);
    public abstract void rejectOffer(Offer offer);
    public abstract List<Offer> retrieveOffersByReceiver(String receiver);
    public abstract List<Offer> retrieveOffersBySender(String sender);
    public abstract Offer retrieveOfferById(int id) ;
    public abstract void acceptOffer(Offer offer, List<Offer> collidingOffers);
    public abstract List<Offer> retrieveCollidingOffers(Offer offerToExclude);
}
