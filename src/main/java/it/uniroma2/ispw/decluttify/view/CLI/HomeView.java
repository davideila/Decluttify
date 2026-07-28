package it.uniroma2.ispw.decluttify.view.CLI;

import it.uniroma2.ispw.decluttify.utils.SessionManager;

public class HomeView extends View {

    public HomeView(SessionManager sessionManager) {
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
