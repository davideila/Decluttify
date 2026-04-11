package it.uniroma2.ispw.decluttify.view.CLI;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.utils.SessionManager;

public class ItemDetailsView extends View {

    FullItemBean item;

    public void show() {
        printHeader("Item Details View");
        super.show();
    }

    @Override
    public void showFunctions() {
        System.out.println("\nITEM NAME: " + item.getName());
        System.out.println("DESCRIPTION: " + item.getDescription());
        System.out.println("CATEGORY: " + item.getCategory());
        System.out.println("CONDITION: " + item.getCondition());
        System.out.println("OWNER: " + item.getOwner());
        System.out.println("LOCATION: " + item.getLocation());
        System.out.println("ACTUALLY IN: "+ item.getNumOffers() +" Offers");
        if(SessionManager.getInstance().getLoggedUser() != null && item.getOwner().equals(SessionManager.getInstance().getLoggedUser().getUsername())){
            System.out.println("\n[0] Edit item");
        }
        else {
            System.out.println("\n[0] Make Offer");
        }
        System.out.println("[1] Owner details");
    }

    public void setItem(FullItemBean fib) {
        this.item = fib;
    }
}