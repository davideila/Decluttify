package it.uniroma2.ispw.decluttify.patterns.State.Offer.states;

import it.uniroma2.ispw.decluttify.model.OfferStatus;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.AbstractState;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.StateMachineImpl;

public class PendingState extends AbstractState {

    public PendingState() {
        super(OfferStatus.PENDING);
    }

    @Override
    public void reject(StateMachineImpl contextSM) {
        this.offerStatus = OfferStatus.REJECTED;
        AbstractState newState = new RejectedState();
        contextSM.changeToState(newState);
    }

    @Override
    public void accept(StateMachineImpl contextSM) {
        this.offerStatus = OfferStatus.ACCEPTED;
        AbstractState newState = new AcceptedState();
        contextSM.changeToState(newState);
    }

    @Override
    public void cancel(StateMachineImpl contextSM) {
        //TODO
    }

    @Override
    public void entry(StateMachineImpl contextSM) {
        contextSM.incrOffersCounters();
        contextSM.setOfferStatus();
    }
}
