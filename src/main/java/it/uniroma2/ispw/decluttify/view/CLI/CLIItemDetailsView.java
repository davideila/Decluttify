package it.uniroma2.ispw.decluttify.view.CLI;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.utils.SessionManager;

public class CLIItemDetailsView extends CLIView {

    FullItemBean item;

    public CLIItemDetailsView(SessionManager sessionManager) {
        super(sessionManager);
    }

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
        if(sessionManager.getLoggedUser() != null && item.getOwner().equals(sessionManager.getLoggedUser().getUsername())){
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