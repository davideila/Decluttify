package it.uniroma2.ispw.decluttify.patterns.State.Offer;

import it.uniroma2.ispw.decluttify.model.Offer;
import it.uniroma2.ispw.decluttify.model.OfferStatus;
import it.uniroma2.ispw.decluttify.patterns.State.StateMachine;

public class StateMachineImpl implements StateMachine {
    private AbstractState currentState;
    private Offer offer;

    public StateMachineImpl(Offer offer){
        this.offer = offer;
        AbstractState state = AbstractState.getInitialState(OfferStatus.PENDING);
        this.currentState = state;
        this.currentState.entry(this);
    }

    public StateMachineImpl(Offer offer, OfferStatus status){
        this.offer = offer;
        this.currentState = AbstractState.getInitialState(status);
    }


    @Override
    public void goNext (Events e){
        switch (e) {
            case accept:
                this.currentState.accept(this);
                break;
            case reject:
                this.currentState.reject(this);
                break;
            case cancel:
                this.currentState.cancel(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void changeToState(AbstractState state) {
        this.currentState.exit(this);
        this.currentState = state;
        this.currentState.entry(this);
    }

    public AbstractState getState(){
        return this.currentState;
    }

    public void setOfferStatus(){
        this.offer.setStatus(this.currentState.getOfferStatus());
    }

    public void incrOffersCounters(){
        this.offer.incrOfferCounters();
    }

    public void decrOffersCounters(){
        this.offer.decrOfferCounters();
    }

    public void updateItemsStatus() {
       this.offer.updateItemsStatus();
    }
}