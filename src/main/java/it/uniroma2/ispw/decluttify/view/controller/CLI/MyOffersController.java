package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.controller.logic.MakeBarterController;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.MyOffersView;

public class MyOffersController extends GraphicController<MyOffersView>{

    @Override
    protected MyOffersView createView() {
        return new MyOffersView();
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
             MakeBarterController mbc = new MakeBarterController();
             switch(index){
                 case 0:
                     try{
                         mbc.acceptOffer(this.view.getSelectedOffer());
                         this.view.showMessage("Offer successfully accepted", false);
                         this.navigatorManager.navigatetoMyBarters();
                     }catch(Exception e){
                         this.handleException(e);
                     }
                     finally{
                         this.view.setOnSelection(false);
                         this.view.setSelectedOffer(null);
                     }
                     break;
                 case 1:
                     try{
                         mbc.rejectOffer(this.view.getSelectedOffer());
                         this.view.showMessage("Offer successfully rejected", false);
                     }catch(Exception e){
                         this.handleException(e);
                     }
                     finally{
                         this.view.setOnSelection(false);
                         this.view.setSelectedOffer(null);
                     }
                     break;
                 default:
                     this.view.showMessage("Select a valid option", true);
            }
        }
    }

    @Override
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
        MakeBarterController mbc = new MakeBarterController();
        try {
            this.view.setReceivedOffers(mbc.loadReceivedOffers(SessionManager.getInstance().getLoggedUser().getUsername()));
            this.view.setSentOffers(mbc.loadSentOffers(SessionManager.getInstance().getLoggedUser().getUsername()));
        }catch(Exception e){
            this.handleException(e);
        }
    }
}
