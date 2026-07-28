package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.MyItemsView;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;

public class MyInventoryController extends GraphicController<MyItemsView> {

    public MyInventoryController(SessionManager sessionManager, Navigator navigatorManager) {
        super(sessionManager, navigatorManager);
    }

    @Override
    protected MyItemsView createView() {
        return new MyItemsView(sessionManager);
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
