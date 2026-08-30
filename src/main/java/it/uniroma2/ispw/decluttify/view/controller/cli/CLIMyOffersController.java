package it.uniroma2.ispw.decluttify.view.controller.cli;

import it.uniroma2.ispw.decluttify.controller.logic.VisualizeOfferController;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.cli.CLIMyOffersView;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;

public class CLIMyOffersController extends CLIGraphicController<CLIMyOffersView> {
    private final VisualizeOfferController visualizeOfferController;

    public CLIMyOffersController(SessionManager sessionManager, Navigator navigatorManager) {
        super(sessionManager, navigatorManager);
        this.visualizeOfferController = new VisualizeOfferController(sessionManager);
    }

    @Override
    protected CLIMyOffersView createView() {
        return new CLIMyOffersView(sessionManager);
    }

    @Override
    protected void handleInput(String choice) {
            super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {
        if (!this.view.isOnSelection()){
            if(index > this.view.getReceivedOffers().size() || index <= 0){
                this.view.showMessage("Select a valid offer", true);
            }
            else{
                this.view.setSelectedOffer(this.view.getReceivedOffers().get(index-1));
                this.view.setOnSelection(true);
            }
        }
        else{
             switch(index){
                 case 0:
                     this.view.showComingSoon();
                     break;
                 case 1:
                     this.view.showComingSoon();
                     break;
                 default:
                     this.view.showMessage("Select a valid option", true);
                     break;
            }
        }
    }

    @Override
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
                if(this.view.isOnSelection()){
                    this.view.setOnSelection(false);
                    break;
                }
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

    @Override
    protected void setupData() {
        try {
            this.view.setReceivedOffers(visualizeOfferController.loadReceivedOffers(sessionManager.getLoggedUser()));
            this.view.setSentOffers(visualizeOfferController.loadSentOffers(sessionManager.getLoggedUser()));
        }catch(Exception e){
            this.handleException(e);
        }
    }

}
