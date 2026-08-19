package it.uniroma2.ispw.decluttify.view.CLI;

import it.uniroma2.ispw.decluttify.utils.SessionManager;

public class CLILoginView extends CLIView {

    public CLILoginView(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public void show() {
        printHeader("Login View");
        if(sessionManager.isLoggedIn()){
            super.show();
        }
        else {
            System.out.println("[f] Item Browser  | [i] My Items      | [o] My Offers     | [m] My Barters    ");
            System.out.println("[d] Donate        | [e] Exit          | [c] Cancel        ");
            this.showFunctions();
        }
    }

    @Override
    public void showFunctions() {
        System.out.println("\nDon't have an account yet? Press [r] to register and start bartering!");
        System.out.println("Press [0] To enter credentials.");
    }

}
