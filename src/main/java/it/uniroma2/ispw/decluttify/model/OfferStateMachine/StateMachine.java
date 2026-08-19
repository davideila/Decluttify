package it.uniroma2.ispw.decluttify.model.OfferStateMachine;

public interface StateMachine {
    public abstract void goNext(Events e);
    public abstract void changeToState(AbstractState state);
}
