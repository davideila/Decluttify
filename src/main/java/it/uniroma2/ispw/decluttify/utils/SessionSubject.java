package it.uniroma2.ispw.decluttify.utils;

import it.uniroma2.ispw.decluttify.view.controller.jfx.SessionObserver;

import java.util.List;
import java.util.Vector;

public abstract class SessionSubject {

    private List<SessionObserver> observers;
    private final Object MUTEX = new Object();

    protected SessionSubject() {
        this((SessionObserver) null);
    }

    protected SessionSubject(SessionObserver obs) {
        this(new Vector<>());
        if (obs != null)
            this.observers.add(obs);
    }

    protected SessionSubject(List<SessionObserver> list) {
        this.observers = list;
    }

    public void attach(SessionObserver obs) {
        synchronized (MUTEX) {
            this.observers.add(obs);
        }
    }

    public void detach(SessionObserver obs) {
        synchronized (MUTEX) {
            this.observers.remove(obs);
        }
    }

    public void notifyObservers() {
        synchronized (MUTEX) {
            for (SessionObserver obs : this.observers) {
                obs.update();
            }
        }
    }

}
