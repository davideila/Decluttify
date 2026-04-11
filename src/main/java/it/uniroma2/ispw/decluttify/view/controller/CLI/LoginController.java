package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.LoginView;
import java.util.Scanner;

public class LoginController extends GraphicController<LoginView> {

    @Override
    protected LoginView createView() {
        return new LoginView();
    }

    @Override
    protected void handleInput(String choice) {
        if (choice.equalsIgnoreCase("C")) {
            this.handleCancel();
        }
        else {
            super.handleInput(choice);
        }
    }

    @Override
    protected void handleViewChoice(int index) {
        switch (index) {
            case 0:
                handleLoginPrompt();
                break;
            default:
                handleViewChoice(index);
        }
    }


    private void handleCancel(){
        System.err.println("Action canceled");
        this.listen(false);
    }

    private void handleLoginPrompt() {
        System.out.println("Enter username: ");
        Scanner sc = this.getView().getScanner();
        String username = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();
        it.uniroma2.ispw.decluttify.controller.logic.LoginController loginController = new it.uniroma2.ispw.decluttify.controller.logic.LoginController();
        try {
            if(!loginController.login(username, password)){
                this.view.showMessage("Incorrect username or password", true);
                this.start();
            }
        }catch(Exception e){
            this.handleException(e);
        }
        finally {
            if (SessionManager.getInstance().isLoggedIn()){
                this.listen(false);
            }
        }
    }
}

