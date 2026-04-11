package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.ItemDetailsView;

public class ItemDetailsController extends GraphicController<ItemDetailsView> {

    FullItemBean item;

    public ItemDetailsController(int id) {
        VisualizeItemController vic = new VisualizeItemController();
        try {
            this.item = vic.loadItemDetails(id);
        }catch(Exception e){
            this.view.showMessage("Item details not available", true);
        }
    }

    @Override
    protected ItemDetailsView createView() {
        return new ItemDetailsView();
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
                if(SessionManager.getInstance().getLoggedUser() != null && item.getOwner().equals(SessionManager.getInstance().getLoggedUser().getUsername())){
                    this.view.showMessage("TO BE IMPLEMENTED", false);
                }
                else {
                    navigatorManager.navigateToMakeOffer(item);
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
