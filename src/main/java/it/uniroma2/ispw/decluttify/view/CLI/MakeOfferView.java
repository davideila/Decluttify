package it.uniroma2.ispw.decluttify.view.CLI;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;

import java.util.ArrayList;
import java.util.List;

public class MakeOfferView extends View{

    List<PreviewItemBean> items;
    List<Integer> addedItems = new ArrayList<>();
    PreviewItemBean requestedItem;

    public void show() {
        printHeader("Make Offer View");
        super.show();
    }

    @Override
    public void showFunctions() {
        System.out.println("\nRequested item");
        System.out.printf("%-20s %-20s %-20s %-20s\n", requestedItem.getName(), requestedItem.getOwner(), requestedItem.getCategory(), requestedItem.getCondition());
        if(addedItems != null && !addedItems.isEmpty()) {
            System.out.println("\nYou are offering");
            System.out.printf("%-20s %-20s %-20s\n", "NAME", "CATEGORY", "CONDITION");
            for(Integer i : addedItems) {
                System.out.printf("%-20s %-20s %-20s\n", items.get(i-1).getName(), items.get(i-1).getCategory(), items.get(i-1).getCondition());
            }
        }
        if(items == null || items.isEmpty()) {
            this.showMessage("You have no items to offer", true);
        }
        else {
            System.out.println("\nAdd items to offer: ");
            System.out.printf("%-10s %-20s %-20s %-20s\n", "LINE", "NAME", "CATEGORY", "CONDITION");
            int i = 1;
            for (PreviewItemBean item : items) {
                System.out.printf("[" + i + "]        %-20s %-20s %-20s\n", item.getName(), item.getCategory(), item.getCondition());
                i++;
            }
        }
        if(addedItems != null && !addedItems.isEmpty()) {
            System.out.println("\n[0] Send Offer");
        }
    }

    public void setItems(List<PreviewItemBean> userInventory) {
        if (userInventory == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = userInventory;
        }
    }

    public void addIndex(int index){
        this.addedItems.add(index);
    }

    public void setRequestedItem(PreviewItemBean requestedItem) {
        this.requestedItem = requestedItem;
    }
}
