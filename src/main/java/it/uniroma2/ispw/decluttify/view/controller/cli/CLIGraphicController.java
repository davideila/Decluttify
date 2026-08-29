package it.uniroma2.ispw.decluttify.view.controller.cli;

import it.uniroma2.ispw.decluttify.controller.logic.LoginController;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.cli.CLIView;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;

public abstract class CLIGraphicController<V extends CLIView> {
    protected V view;
    protected boolean isLoginRequired;
    protected Navigator navigatorManager;
    protected boolean listening;
    protected SessionManager sessionManager;

    public CLIGraphicController(SessionManager sessionManager, Navigator navigatorManager) {
        this.sessionManager = sessionManager;
        this.navigatorManager = navigatorManager;
    }

    protected abstract V createView();

    protected void handleInput(String choice){
        try {
            int index = Integer.parseInt(choice);
            this.handleViewChoice(index);
        } catch (NumberFormatException e) {
            this.handleMenuInput(choice);
        }
    }

    protected void handleMenuInput(String choice) {
        switch (choice){
            case "l","L":
                if(!sessionManager.isLoggedIn()){
                    handleLogin();
                }
                else{
                    handleLogout();
                }
                break;
            case "p","P":
                if(sessionManager.isLoggedIn()){
                    handleProfile();
                }
                break;
            case "r","R":
                if(sessionManager.isLoggedIn()){
                    handleRegister();
                }
                break;
            case "h","H":
                handleHome();
                break;
            case "b","B":
                handleBack();
                break;
            case "f","F":
                handleItemBrowser();
                break;
            case "i","I":
                handleMyItems();
                break;
            case "o","O":
                handleMyOffers();
                break;
            case "m","M":
                handleBarters();
                break;
            case "d","D":
                handleDonate();
                break;
            case "e","E":
                handleExit();
                break;
            default:
                this.view.showMessage("Invalid input", true);
        }
    }

    protected abstract void handleViewChoice(int index);

    void handleDonate() {
        this.view.showMessage("TO BE IMPLEMENTED", false);
    }

    void handleBarters() {
        triggerLogin();
        if(sessionManager.isLoggedIn()) {
            this.view.showMessage("TO BE IMPLEMENTED", false);
        }
    }

    void handleMyOffers() {
        triggerLogin();
        if(sessionManager.isLoggedIn()) {
            navigatorManager.navigateTo(CLIViewType.MY_OFFERS);
        }
    }

    void handleMyItems() {
        triggerLogin();
        if(sessionManager.isLoggedIn()) {
            this.view.showMessage("TO BE IMPLEMENTED", false);
        }
    }

    void handleItemBrowser() {
        navigatorManager.navigateTo(CLIViewType.ITEM_BROWSER);
    }

    protected void handleRegister() {
        this.view.showMessage("TO BE IMPLEMENTED", false);
    }

    protected void handleProfile() {
        if(sessionManager.isLoggedIn()) {
            this.view.showMessage("TO BE IMPLEMENTED", false);
        }
        else {
            triggerLogin();
        }
    }

    public void initialize() {
        this.view = this.createView();
        this.setupData();
        this.start();
    }

    protected void start(){
        this.listen(true);
        this.loadView();
    }

    protected void setupData(){}

    protected void triggerLogin() {
        if(!sessionManager.isLoggedIn()){
            this.view.showMessage("You must log in", true);
            handleLogin();
        }
    }

    public void listen(boolean b) {
        this.listening = b;
    }

    protected void loadView(){
        while (listening) {
            this.view.show();
            String choice = view.getInput("Selection: ");
            handleInput(choice);
        }
    }

    public void handleLogout(){
        LoginController loginController = new LoginController(sessionManager);
        loginController.logout();
        navigatorManager.reset();
    }

    public void handleHome(){
        navigatorManager.navigateTo(CLIViewType.HOME);
    }

    public void handleBack(){
        navigatorManager.navigateBack();
    }

    public void handleExit(){
        this.listen(false);
        this.view.showMessage("Exiting... goodbye!", false);
        System.exit(0);
    }

    public void handleLogin(){
        navigatorManager.navigateTo(CLIViewType.LOGIN);
    }

    protected void handleException(Exception e) {
        if (e instanceof DecluttifyException) {
            this.view.showMessage(e.getMessage(), true);
        } else {
            this.view.showMessage("An unexpected error occurred.", true);
            e.printStackTrace();
        }
    }

    //Getters & Setters

    public CLIView getView() {
        return view;
    }

    public void setView(V view) {
        this.view = view;
    }

    public void setIsLoginRequired(boolean isLoginRequired) {
        this.isLoginRequired = isLoginRequired;
    }

    public boolean isLoginRequired() {
        return isLoginRequired;
    }

}
