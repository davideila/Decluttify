package it.uniroma2.ispw.decluttify.view.cli;

import it.uniroma2.ispw.decluttify.utils.SessionManager;

public class CLIHomeView extends CLIView {

    public CLIHomeView(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void showMenu() {
        printHeader("Home View");
        super.showMenu();
    }

    @Override
    public void showFunctions() {

    }
}
