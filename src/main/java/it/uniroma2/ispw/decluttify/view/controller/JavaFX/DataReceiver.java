package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

public interface DataReceiver<T> {

    // this interface is needed to initialize views that need an initial input data

    void initData(T data);
}