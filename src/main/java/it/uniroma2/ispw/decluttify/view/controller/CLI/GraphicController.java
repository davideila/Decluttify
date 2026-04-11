package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.View;

public abstract class GraphicController<V extends View> {
    protected V view;
    protected boolean isLoginRequired;
    protected NavigatorManager navigatorManager = NavigatorManager.getInstance();
    protected boolean listening;

    public GraphicController() {}

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
                if(!SessionManager.getInstance().isLoggedIn()){
                    handleLogin();
                }
                else{
                    handleLogout();
                }
                break;
            case "p","P":
                if(SessionManager.getInstance().isLoggedIn()){
                    handleProfile();
                }
                break;
            case "r","R":
                if(SessionManager.getInstance().isLoggedIn()){
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
        //TODO
        this.view.showMessage("TO BE IMPLEMENTED", false);
    }

    void handleBarters() {
        triggerLogin();
        if(SessionManager.getInstance().isLoggedIn()) {
            navigatorManager.navigatetoMyBarters();
        }
    }

    void handleMyOffers() {
        triggerLogin();
        if(SessionManager.getInstance().isLoggedIn()) {
            navigatorManager.navigateToMyOffers();
        }
    }

    void handleMyItems() {
        triggerLogin();
        if(SessionManager.getInstance().isLoggedIn()) {
            navigatorManager.navigateToMyItems();
        }
    }

    void handleItemBrowser() {
        navigatorManager.navigateToItemBrowser();
    }

    protected void handleRegister() {
        this.view.showMessage("TO BE IMPLEMENTED", false);
        //TODO
    }

    protected void handleProfile() {
        triggerLogin();
        if(SessionManager.getInstance().isLoggedIn()) {
            navigatorManager.navigateToProfile();
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
        if(!SessionManager.getInstance().isLoggedIn()){
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
        navigatorManager.handleLogout();
    }


    public void handleHome(){
        navigatorManager.navigateToHome();
    }

    public void handleBack(){
        navigatorManager.navigateToBack();
    }

    public void handleExit(){
        this.listen(false);
        this.view.showMessage("Exiting... goodbye!", false);
        PersistenceManager.getInstance().closeConnection();
        System.exit(0);
    }

    public void handleLogin(){
        navigatorManager.navigateToLogin();
    }

    protected void handleException(Exception e) {
        if (e instanceof DAOException) {
            this.view.showMessage("System error: service not available. Please try again later.", true);
        } else if (e instanceof ModelException) {
            this.view.showMessage(e.getMessage(), true);
        } else {
            this.view.showMessage("Unexpected error: " + e.getMessage(), true);
        }
        e.printStackTrace();
    }

    //Getters & Setters

    public View getView() {
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
