package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.exception.LoginException;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.CLILoginView;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;

import java.util.Scanner;

public class CLILoginController extends CLIGraphicController<CLILoginView> {

    private final it.uniroma2.ispw.decluttify.controller.logic.LoginController logicLoginController;

    public CLILoginController(SessionManager sessionManager, Navigator navigatorManager) {
        super(sessionManager, navigatorManager);
        logicLoginController = new it.uniroma2.ispw.decluttify.controller.logic.LoginController(sessionManager);
    }

    @Override
    protected CLILoginView createView() {
        return new CLILoginView(sessionManager);
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

        try {
            if (!logicLoginController.login(username, password)) {
                this.view.showMessage("Incorrect username or password", true);
                this.start();
            }
        }catch (LoginException e){
            this.view.showMessage(e.getMessage(), true);
        }catch(Exception e){
            this.handleException(e);
        }
        finally {
            if (sessionManager.isLoggedIn()){
                this.listen(false);
                this.navigatorManager.navigateBack();
            }
        }
    }
}

