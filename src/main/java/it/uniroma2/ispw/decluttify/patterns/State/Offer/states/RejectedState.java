package it.uniroma2.ispw.decluttify.patterns.State.Offer.states;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.model.OfferStatus;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.AbstractState;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.StateMachineImpl;

public class RejectedState extends AbstractState {

    public RejectedState() {
        super(OfferStatus.REJECTED);
    }

    @Override
    public void reject(StateMachineImpl context) {
        throw new ModelException("Offer already rejected!");
    }

    @Override
    public void accept(StateMachineImpl context) {
        throw new ModelException("Rejected offer can't be accepted!");
    }

    @Override
    public void cancel(StateMachineImpl context) {
        //TODO
    }

    @Override
    public void entry(StateMachineImpl contextSM) {
        contextSM.decrOffersCounters();
        contextSM.setOfferStatus();
    }
}
