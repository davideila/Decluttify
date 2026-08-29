package it.uniroma2.ispw.decluttify.view.controller.cli;

import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.cli.CLIHomeView;

public class CLIHomeController extends CLIGraphicController<CLIHomeView> {

    public CLIHomeController(SessionManager sessionManager, CLINavigatorManager navigatorManager) {
        super(sessionManager, navigatorManager);
    }

    @Override
    protected CLIHomeView createView() {
        return new CLIHomeView(sessionManager);
    }

    @Override
    protected void handleInput(String choice) {
            super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {

    }

}
