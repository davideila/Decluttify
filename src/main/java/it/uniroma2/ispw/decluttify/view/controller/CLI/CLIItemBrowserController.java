package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.CLI.CLIItemBrowserView;

import java.util.List;


public class CLIItemBrowserController extends CLIGraphicController<CLIItemBrowserView> {

    List<PreviewItemBean> items;

    public CLIItemBrowserController(SessionManager sessionManager, CLINavigatorManager navigatorManager) {
        super(sessionManager, navigatorManager);
    }

    @Override
    protected CLIItemBrowserView createView() {
        return new CLIItemBrowserView(sessionManager);
    }

    @Override
    protected void handleInput(String choice) {
            super.handleInput(choice);
    }

    @Override
    protected void handleViewChoice(int index) {
        navigatorManager.navigateTo(CLIViewType.ITEM_DETAILS, items.get(index-1));
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

