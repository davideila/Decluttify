package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.view.CLI.MyItemsView;

public class MyInventoryController extends GraphicController<MyItemsView> {

    @Override
    protected MyItemsView createView() {
        return new MyItemsView();
    }

    @Override
    protected void handleInput(String choice) {
            super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {

    }

    @Override
    protected void setupData() {

    }
}
