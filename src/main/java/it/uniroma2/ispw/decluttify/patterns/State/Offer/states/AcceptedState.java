package it.uniroma2.ispw.decluttify.patterns.State.Offer.states;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.model.OfferStatus;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.AbstractState;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.StateMachineImpl;

public class AcceptedState extends AbstractState {

    public AcceptedState() {
        super(OfferStatus.ACCEPTED);
    }

    @Override
    public void reject(StateMachineImpl contextSM) {
        throw new ModelException("Accepted offer can't be rejected!");
    }

    @Override
    public void accept(StateMachineImpl contextSM) {
        throw new ModelException("Offer already accepted!");
    }

    @Override
    public void cancel(StateMachineImpl contextSM) {
        //TODO
    }

    @Override
    public void entry(StateMachineImpl contextSM) {
        contextSM.setOfferStatus();
        contextSM.updateItemsStatus();
    }
}
