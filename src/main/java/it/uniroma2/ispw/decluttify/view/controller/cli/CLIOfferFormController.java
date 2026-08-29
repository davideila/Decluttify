package it.uniroma2.ispw.decluttify.view.controller.cli;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.exception.DuplicateOfferException;
import it.uniroma2.ispw.decluttify.exception.SessionExpiredException;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.cli.CLIOfferFormView;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import java.util.ArrayList;
import java.util.List;

public class CLIOfferFormController extends CLIGraphicController<CLIOfferFormView> {

    private List<PreviewItemBean> inventoryItems;
    private List<Integer> addedItems = new ArrayList<>(); //stores indexes of inventoryItems for added to offer items
    private List<Integer> offeredItems = new ArrayList<>(); //stores indexes of inventoryItems for items sent for offer
    private PreviewItemBean requestedItem;
    private final it.uniroma2.ispw.decluttify.controller.logic.MakeOfferController makeOfferController;

    public CLIOfferFormController(PreviewItemBean requestedItem, SessionManager sessionManager, Navigator navigatorManager) {
        super(sessionManager, navigatorManager);
        this.setRequestedItem(requestedItem);
        this.makeOfferController = new it.uniroma2.ispw.decluttify.controller.logic.MakeOfferController(sessionManager);
    }

    @Override
    protected CLIOfferFormView createView() {
        return new CLIOfferFormView(sessionManager);
    }

    @Override
    protected void handleInput(String choice) {
            super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {
        if(inventoryItems == null || inventoryItems.isEmpty()) {
            this.view.showMessage("You have no items to offer in your inventory", true);
        }
        else {
            if(index > inventoryItems.size() || index < 0) {
                this.view.showMessage("Please select a valid item", true);
                return;
            }
            if(index == 0 && !addedItems.isEmpty()) { // 0 is send offer when atleast 1 item is added
                if(!offeredItems.isEmpty() && offeredItems.equals(addedItems)) {
                    this.view.showMessage("Offer already sent!", true);
                }
                else {
                    ArrayList<PreviewItemBean> items= new ArrayList<>();
                    for(Integer i : addedItems) {
                        items.add(inventoryItems.get(i-1));
                    }
                    try {
                        makeOfferController.submitOffer(items, requestedItem, sessionManager.getLoggedUser());
                        this.view.showMessage("Offer sent!", false);
                        navigatorManager.navigateTo(CLIViewType.MY_OFFERS);
                    }catch(SessionExpiredException e){
                        this.view.showMessage("Session has expired. Please try again", true);
                        this.navigatorManager.reset();
                    }catch(DuplicateOfferException e){
                        this.handleException(e);
                    }catch(DecluttifyException e){
                        this.handleException(e);
                        navigatorManager.navigateTo(CLIViewType.HOME);
                    }catch(Exception e){
                        this.handleException(e);
                    }
                }
            }
            else {
                if(addedItems.contains(index)) {
                    this.view.showMessage("Item already added to offer!", true);
                }
                else {
                    if (index != 0) {
                        addedItems.add(index);
                        this.view.addIndex(index);
                    }
                }
            }
        }
    }

    @Override
    protected void setupData() {
        try{
            this.inventoryItems = makeOfferController.loadUserInventory(sessionManager.getLoggedUser());
            this.view.setItems(this.inventoryItems);
        }catch(Exception e){
            this.handleException(e);
        }
        this.view.setRequestedItem(this.requestedItem);
    }

    public void setRequestedItem(PreviewItemBean requestedItem) {
        this.requestedItem = requestedItem;
    }
}
