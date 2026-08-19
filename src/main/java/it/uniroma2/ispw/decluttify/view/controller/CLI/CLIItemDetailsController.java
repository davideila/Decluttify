package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.CLIItemDetailsView;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;

public class CLIItemDetailsController extends CLIGraphicController<CLIItemDetailsView> {
    VisualizeItemController visualizeItemController;
    FullItemBean item;

    public CLIItemDetailsController(PreviewItemBean pib, SessionManager sessionManager, Navigator navigatorManager) {
        super(sessionManager, navigatorManager);
        this.visualizeItemController = new VisualizeItemController();
        try {
            this.item = visualizeItemController.loadItemDetails(pib);
        }catch(Exception e){
            this.view.showMessage("Item details not available", true);
        }
    }

    @Override
    protected CLIItemDetailsView createView() {
        return new CLIItemDetailsView(sessionManager);
    }

    @Override
    protected void handleInput(String choice) {
        super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {
        switch (index) {
            case 0:
                triggerLogin();
                if(sessionManager.getLoggedUser() != null && item.getOwner().equals(sessionManager.getLoggedUser().getUsername())){
                    this.view.showMessage("TO BE IMPLEMENTED", false);
                }
                else {
                    navigatorManager.navigateTo(CLIViewType.OFFER_FORM, item);
                }
                break;
            case 1:
                //navigatorManager.navigateToUserDetails(username);
                this.view.showMessage("TO BE IMPLEMENTED", false);
                break;
            default:
                this.view.showMessage("Invalid choice", true);
                break;
        }
    }

    @Override
    protected void setupData(){
        try{
            this.view.setItem(item);
        }catch(Exception e){
            this.handleException(e);
        }
    }
}
