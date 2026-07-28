package it.uniroma2.ispw.decluttify.view.CLI;

import it.uniroma2.ispw.decluttify.utils.SessionManager;

public class MyItemsView extends View{

    public MyItemsView(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void show() {
        printHeader("My Items View");
        super.show();
    }

    @Override
    public void showFunctions() {

    }

}
