package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Barter {
    private int id;
    private Offer offer;
    private String startDate;
    private String completionDate;
    private BarterStatus status;
    private boolean offererConfirmed = false;
    private boolean receiverConfirmed = false;

    public Barter(Offer offer) {
        this.offer = offer;
        this.setStatus(BarterStatus.ONGOING);
        this.startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.completionDate = null;
    }

    public Barter(int barterID, int offerID, String startDate, String completionDate, BarterStatus status, boolean offererConfirmed, boolean receiverConfirmed) {
        this.setId(barterID);
        this.offer = new Offer(offerID);
        this.startDate = startDate;
        this.completionDate = completionDate;
        this.setStatus(status);
        this.offererConfirmed = offererConfirmed;
        this.receiverConfirmed = receiverConfirmed;
    }

    // Business methods

    public void confirm(String username) {
        if(this.status == BarterStatus.ONGOING || this.status == BarterStatus.CONFIRMED) {
            if (this.offer.getOfferer().getUsername().equals(username)) {
                this.setOffererConfirmed(true);
            } else if (this.offer.getReceiver().getUsername().equals(username)) {
                this.setReceiverConfirmed(true);
            }
            this.setStatus(BarterStatus.CONFIRMED);
        }
        else{
            throw new ModelException("Couldn't confirm this barter: invalid status");
        }
        if(this.isOffererConfirmed() && this.isReceiverConfirmed()) {
            this.setStatus(BarterStatus.COMPLETED);
            this.setCompletionDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

    }

    public void cancel(String username) {
        //TODO
    }

    public void dispute(String username) {
        //TODO
    }

    // Getter and Setter

    public BarterStatus getStatus() {
        return status;
    }

    public void setStatus(BarterStatus status) { //TODO could use pattern SM
        this.status = status;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        if (completionDate != null) {
            LocalDate start = LocalDate.parse(this.startDate);
            LocalDate completion = LocalDate.parse(completionDate);
            if (completion.isBefore(start)) {
                throw new ModelException("Completion date cannot be before start date");
            }
        }
        this.completionDate = completionDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public boolean isOffererConfirmed() {
        return offererConfirmed;
    }

    public void setOffererConfirmed(boolean offererConfirmed) {
        this.offererConfirmed = offererConfirmed;
    }

    public boolean isReceiverConfirmed() {
        return receiverConfirmed;
    }

    public void setReceiverConfirmed(boolean receiverConfirmed) {
        this.receiverConfirmed = receiverConfirmed;
    }

    public boolean isCompleted() {
        return this.status == BarterStatus.COMPLETED;
    }
}