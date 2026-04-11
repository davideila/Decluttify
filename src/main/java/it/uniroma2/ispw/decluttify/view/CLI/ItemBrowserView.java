package it.uniroma2.ispw.decluttify.view.CLI;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;

import java.util.ArrayList;
import java.util.List;

public class ItemBrowserView extends View{

    List<PreviewItemBean> items;

    public void show() {
        printHeader("Item Browser View");
        super.show();
    }

    @Override
    public void showFunctions() {
        if(this.items==null){
            this.showMessage("There are no available items, please come try again later", false);
        }
        else {
            System.out.println("\nSelect one item to visualize details");
            System.out.printf("%-10s %-20s %-20s %-20s %-10s%n",
                    "LINE", "ITEM NAME", "CATEGORY", "OWNER", "CONDITION");
            int i = 1;
            for (PreviewItemBean item : items) {
                System.out.printf("[%d]        %-20s %-20s %-20s %-10s%n",
                        i,
                        item.getName(),
                        item.getCategory(),
                        item.getOwner(),
                        item.getCondition());
                i++;
            }
        }
    }

    public void setItems(List<PreviewItemBean> pib){
        if(pib == null){
            this.items = new ArrayList<>();
        }
        else {
            this.items = pib;
        }
    }

}
