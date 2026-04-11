package it.uniroma2.ispw.decluttify.patterns.State.Offer;

import it.uniroma2.ispw.decluttify.model.OfferStatus;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.states.AcceptedState;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.states.PendingState;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.states.RejectedState;

public abstract class AbstractState {
    protected OfferStatus offerStatus;

    protected AbstractState(OfferStatus offerStatus){
        this.offerStatus = offerStatus;
    }

    public void entry(StateMachineImpl contextSM){};
    public void exit(StateMachineImpl contextSM){};

    public abstract void reject(StateMachineImpl context);

    public abstract void accept(StateMachineImpl context);

    public abstract void cancel(StateMachineImpl context);

    public static AbstractState getInitialState(OfferStatus status) {
        switch (status) {
            case ACCEPTED:
                return new AcceptedState();
            case REJECTED:
                return new RejectedState();
            default:
                return new PendingState();
        }
    }

    public OfferStatus getOfferStatus(){
        return this.offerStatus;
    }

}
