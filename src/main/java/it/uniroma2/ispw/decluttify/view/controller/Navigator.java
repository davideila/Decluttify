package it.uniroma2.ispw.decluttify.view.controller;

public interface Navigator {
    void navigateTo(ViewType viewType);
    void navigateTo(ViewType viewType, Object data);
    void navigateBack();
    void reset();
    void start();
}