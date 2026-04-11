package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.view.CLI.HomeView;

public class HomeController extends GraphicController<HomeView> {

    @Override
    protected HomeView createView() {
        return new HomeView();
    }

    @Override
    protected void handleInput(String choice) {
            super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {

    }

}
