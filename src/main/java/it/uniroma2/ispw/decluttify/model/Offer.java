package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.patterns.State.Offer.Events;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.StateMachineImpl;
import it.uniroma2.ispw.decluttify.patterns.State.StateMachine;
import java.util.List;

public class Offer {
    private int id;
    private User offerer;
    private User receiver;
    private List<Item> itemOffered;
    private Item itemRequested;
    private boolean isShippingOn;
    private boolean isEscrowOn;
    private OfferStatus status;
    private StateMachine SM;

    //Constructors

    public Offer(int id, User offerer, User receiver, List<Item> itemsOffered, Item itemRequested, boolean isShippingOn, boolean isEscrowOn, OfferStatus status) {
        this.setId(id);
        this.setOfferer(offerer);
        this.setReceiver(receiver);
        this.setItemOffered(itemsOffered);
        this.setItemRequested(itemRequested);
        this.setIsShippingOn(isShippingOn);
        this.setIsEscrowOn(isEscrowOn);
        this.setStatus(status);
        this.restoreStateMachine(status);
    }

    public Offer(User offerer, User receiver, List<Item> itemsOffered, Item itemRequested) {
        this.setId(-1);
        this.setOfferer(offerer);
        this.setReceiver(receiver);
        this.setItemOffered(itemsOffered);
        this.setItemRequested(itemRequested);
        this.setIsShippingOn(false);
        this.setIsEscrowOn(false);
        this.setStatus(OfferStatus.PENDING);
        this.setState(new StateMachineImpl(this));
    }

    public Offer(int id) {
        this.id = id;
    }

    void restoreStateMachine(OfferStatus status) {
        this.SM = new StateMachineImpl(this, status);
    }

    //Business methods

    public Barter accept(){
        this.SM.goNext(Events.accept);
        return this.startBarter();
    }

    public void reject(){
        this.SM.goNext(Events.reject);
    }

    private Barter startBarter(){
        Barter barter = new Barter(this);
        return barter;
    }

    public void decrOfferCounters(){
        this.itemRequested.decrOffersCounter();
        for(Item item: itemOffered){
            item.decrOffersCounter();
        }
    }

    public void incrOfferCounters(){
        this.itemRequested.incrOffersCounter();
        for(Item item: itemOffered){
            item.incrOffersCounter();
        }
    }

    public void updateItemsStatus(){
        switch (this.status) {
            case ACCEPTED:
                this.itemRequested.updateStatus(ItemStatus.TRADED);
                for(Item item: itemOffered){
                    item.updateStatus(ItemStatus.TRADED);
                }
                break;
            default:
                break;
        }
    }

    //Getters and setters

    public void setId(int id) {
        this.id = id;
    }

    public void setOfferer(User offerer) {
        this.offerer = offerer;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setItemOffered(List<Item> itemOffered) {
        this.itemOffered = itemOffered;
    }

    public void setItemRequested(Item itemRequested) {
        this.itemRequested = itemRequested;
    }


    public void setIsShippingOn(boolean isShippingOn) {
        this.isShippingOn = isShippingOn;
    }

    public void setIsEscrowOn(boolean isEscrowOn) {
        this.isEscrowOn = isEscrowOn;
    }

    public int getId() {
        return id;
    }

    public User getOfferer() {
        return offerer;
    }

    public User getReceiver() {
        return receiver;
    }

    public List<Item> getItemOffered() {
        return itemOffered;
    }

    public Item getItemRequested() {
        return itemRequested;
    }


    public boolean isShippingOn() {
        return isShippingOn;
    }

    public boolean isEscrowOn() {
        return isEscrowOn;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
        this.restoreStateMachine(status);
    }

    public void setState(StateMachine SM) {
        this.SM = SM;
    }
}
