package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.controller.logic.MakeBarterController;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.MyBartersView;

public class MyBartersController extends GraphicController<MyBartersView>{

    @Override
    protected MyBartersView createView() {
        return new MyBartersView();
    }

    @Override
    protected void handleInput(String choice) {
            super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {
        if (!this.view.isOnSelection()){
            if(index > this.view.getOngoingBarters().size() || index <= 0){
                this.view.showMessage("Select a valid barter", true);
            }
            else{
                this.view.setSelectedBarter(this.view.getOngoingBarters().get(index-1));
                this.view.setOnSelection(true);
            }
        }
        else{
            MakeBarterController mbc = new MakeBarterController();
            switch(index){
                case 0:
                    try{
                        if(this.view.getSelectedBarter().getStatus().equalsIgnoreCase("COMPLETED") || this.view.getSelectedBarter().isYouConfirmed()){
                            //mbc.writeReview(this.view.getSelectedBarter());
                            this.view.showMessage("TO BE IMPLEMENTED", false);
                        }
                        else {
                            mbc.confirmBarter(this.view.getSelectedBarter());
                            this.view.showMessage("Barter successfully confirmed", false);
                            this.navigatorManager.navigatetoMyBarters();
                        }
                    }catch(DAOException e) {
                        this.view.showMessage("System error: service not available. Please try again later.", true);
                        e.printStackTrace();
                    }catch(ModelException e) {
                        this.view.showMessage(e.getMessage(), true);
                        e.printStackTrace();
                    }catch(Exception e) {
                        this.view.showMessage("Unexpected error", true);
                        e.printStackTrace();
                    }finally{
                        this.view.setOnSelection(false);
                        this.view.setSelectedBarter(null);
                    }
                    break;
                case 1:
                    try{
                        if(this.view.getSelectedBarter().getStatus().equalsIgnoreCase("COMPLETED") || this.view.getSelectedBarter().isYouConfirmed()){
                            mbc.viewBarterDetails(this.view.getSelectedBarter());
                            this.view.showMessage("TO BE IMPLEMENTED", false);
                        }
                        else {
                            mbc.disputeBarter(this.view.getSelectedBarter());
                            this.view.showMessage("TO BE IMPLEMENTED", false);
                        }
                    }catch(Exception e){
                        this.handleException(e);
                    }
                    finally{
                        this.view.setOnSelection(false);
                        this.view.setSelectedBarter(null);
                    }
                    break;
                case 2:
                    try{
                        if(this.view.getSelectedBarter().getStatus().equalsIgnoreCase("COMPLETED") || this.view.getSelectedBarter().isYouConfirmed()){
                            this.view.showMessage("Select a valid option.", true);
                        }
                        else {
                            mbc.viewBarterDetails(this.view.getSelectedBarter());
                            this.view.showMessage("TO BE IMPLEMENTED", false);
                        }
                    }catch(Exception e){
                        this.handleException(e);
                    }
                    finally{
                        this.view.setOnSelection(false);
                        this.view.setSelectedBarter(null);
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
            this.view.setOngoingBarters(mbc.loadUserBarters());
        }catch(Exception e){
            this.handleException(e);
        }
    }
}
