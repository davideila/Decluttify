package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import java.util.Stack;

public class NavigatorManager implements Navigator {
    protected Stack<GraphicController> navigationHistory = new Stack<>();
    protected SessionManager sessionManager;

    public NavigatorManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    private void pushAndStart(GraphicController nextController) {
        if (!navigationHistory.isEmpty()) {
            navigationHistory.peek().listen(false);
        }
        navigationHistory.push(nextController);
        nextController.initialize();
    }

    public void reset(){
        if(!navigationHistory.isEmpty()){
            navigationHistory.peek().listen(false);
            navigationHistory.clear();
        }
        this.navigateTo(ViewType.HOME);
    }

    @Override
    public void start() {
        this.navigateTo(ViewType.HOME);
    }

    @Override
    public void navigateTo(ViewType viewType) {
        switch(viewType){
            case ITEM_BROWSER -> pushAndStart(new ItemBrowserController(sessionManager, this));
            case MY_BARTERS -> pushAndStart(new  MyBartersController(sessionManager, this));
            case LOGIN ->  {
                pushAndStart(new LoginController(sessionManager, this));
                navigateBack();
            }
            case MY_OFFERS ->  pushAndStart(new MyOffersController(sessionManager, this));
            case MY_INVENTORY ->  pushAndStart(new MyInventoryController(sessionManager, this));
            case HOME ->  pushAndStart(new HomeController(sessionManager, this));
            case null, default -> {
                return; // TODO
            }
        }
    }

    @Override
    public void navigateTo(ViewType viewType, Object data) {
        switch(viewType){
            case OFFER_FORM ->  pushAndStart(new MakeOfferController((PreviewItemBean) data, sessionManager, this));
            case ITEM_DETAILS ->  pushAndStart(new ItemDetailsController((PreviewItemBean) data, sessionManager, this));
            case null, default -> {
                return; // TODO
            }
        }
    }

    @Override
    public void navigateBack() {
        GraphicController controller = null;
        if (!navigationHistory.isEmpty()) {
            controller = navigationHistory.pop();
            controller.listen(false);
        }
        while(!navigationHistory.isEmpty()){
            controller = navigationHistory.peek();
            if(sessionManager.isLoggedIn() && controller.isLoginRequired()){
                controller.listen(false);
                navigationHistory.pop();
                controller = null;
            }
            else break;
        }
        if(controller == null){
            this.navigateTo(ViewType.HOME);
        }
        else controller.start();
    }

}
