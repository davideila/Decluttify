package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.MakeOfferView;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;

import java.util.ArrayList;
import java.util.List;

public class OfferFormController extends GraphicController<MakeOfferView>{

    private List<PreviewItemBean> inventoryItems;
    private List<Integer> addedItems = new ArrayList<>(); //stores indexes of inventoryItems for added to offer items
    private List<Integer> offeredItems = new ArrayList<>(); //stores indexes of inventoryItems for items sent for offer
    private PreviewItemBean requestedItem;
    private final it.uniroma2.ispw.decluttify.controller.logic.MakeOfferController makeOfferController;

    public OfferFormController(PreviewItemBean requestedItem, SessionManager sessionManager, Navigator navigatorManager) {
        super(sessionManager, navigatorManager);
        this.setRequestedItem(requestedItem);
        this.makeOfferController = new it.uniroma2.ispw.decluttify.controller.logic.MakeOfferController(sessionManager);
    }

    @Override
    protected MakeOfferView createView() {
        return new MakeOfferView(sessionManager);
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
            if(index == 0 && !addedItems.isEmpty()) {
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
                        navigatorManager.navigateTo(ViewType.MY_OFFERS);
                    }catch(DecluttifyException e){
                        this.handleException(e);
                        navigatorManager.navigateTo(ViewType.HOME);
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
            System.out.println(inventoryItems.toString());
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
