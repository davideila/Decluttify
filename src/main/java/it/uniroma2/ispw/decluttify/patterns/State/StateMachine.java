package it.uniroma2.ispw.decluttify.patterns.State;

import it.uniroma2.ispw.decluttify.patterns.State.Offer.AbstractState;
import it.uniroma2.ispw.decluttify.patterns.State.Offer.Events;

public interface StateMachine {
    public abstract void goNext(Events e);
    public abstract void changeToState(AbstractState state);
}
