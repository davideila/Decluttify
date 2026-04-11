package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import it.uniroma2.ispw.decluttify.view.CLI.ItemBrowserView;
import java.util.List;


public class ItemBrowserController extends GraphicController<ItemBrowserView> {

    List<PreviewItemBean> items;

    @Override
    protected ItemBrowserView createView() {
        return new ItemBrowserView();
    }

    @Override
    protected void handleInput(String choice) {
            super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {
        navigatorManager.navigateToItemDetails(items.get(index-1).getId());
    }

    @Override
    protected void setupData(){
        VisualizeItemController vic = new VisualizeItemController();
        try{
            this.items = vic.loadAvailableItems();
            this.view.setItems(items);
        }catch(Exception e){
            this.handleException(e);
        }
    }
}

