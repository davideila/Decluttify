package it.uniroma2.ispw.decluttify.model;

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
    }

    public Offer(int id) {
        this.id = id;
    }


    //Business methods

    public boolean isDuplicate(Offer partnerOffer) {
        if (partnerOffer == null) {
            return false;
        }

        //check receivers
        if (this.getReceiver() == null || partnerOffer.getReceiver() == null ||
                !this.getReceiver().getUsername().equals(partnerOffer.getReceiver().getUsername())) {
            return false;
        }

        //check offerers
        if (this.getOfferer() == null || partnerOffer.getOfferer() == null ||
                !this.getOfferer().getUsername().equals(partnerOffer.getOfferer().getUsername())) {
            return false;
        }

        //check target items
        if (this.getItemRequested() == null || partnerOffer.getItemRequested() == null ||
                this.getItemRequested().getId() != partnerOffer.getItemRequested().getId()) {
            return false;
        }

        //check offered items
        if (this.itemOffered.size() != partnerOffer.getItemOffered().size()) {
            return false;
        }
        for (Item item1 : this.itemOffered) {
            boolean found = false;
            for (Item item2 : partnerOffer.getItemOffered()) {
                if (item1.getId() == item2.getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
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
    }

}
