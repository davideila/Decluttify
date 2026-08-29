package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import java.util.Stack;

public class CLINavigatorManager implements Navigator {
    protected Stack<CLIGraphicController> navigationHistory = new Stack<>();
    protected SessionManager sessionManager;

    public CLINavigatorManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    private void pushAndStart(CLIGraphicController nextController) {
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
        this.navigateTo(CLIViewType.HOME);
    }

    @Override
    public void start() {
        this.navigateTo(CLIViewType.HOME);
    }

    @Override
    public void navigateTo(ViewType viewType) {
        if (!(viewType instanceof CLIViewType)) {
            throw new IllegalArgumentException("Unsupported view for CLI: " + viewType);
        }
            switch((CLIViewType) viewType){
                case ITEM_BROWSER -> pushAndStart(new CLIItemBrowserController(sessionManager, this));
                case MY_BARTERS -> pushAndStart(new CLIMyBartersController(sessionManager, this));
                case LOGIN ->  {pushAndStart(new CLILoginController(sessionManager, this));}
                case MY_OFFERS ->  pushAndStart(new CLIMyOffersController(sessionManager, this));
                case HOME ->  pushAndStart(new CLIHomeController(sessionManager, this));
                case null, default -> {
                    throw new IllegalArgumentException("Unsupported view type: " + viewType);
                }
            }
        }

    @Override
    public void navigateTo(ViewType viewType, Object data) {
        if (!(viewType instanceof CLIViewType)) {
            throw new IllegalArgumentException("Unsupported view for CLI: " + viewType);
        }
        switch((CLIViewType) viewType){
            case OFFER_FORM ->  pushAndStart(new CLIOfferFormController((PreviewItemBean) data, sessionManager, this));
            case ITEM_DETAILS ->  pushAndStart(new CLIItemDetailsController((PreviewItemBean) data, sessionManager, this));
            case null, default -> {
                throw new IllegalArgumentException("Unsupported view type: " + viewType);
            }
        }
    }

    @Override
    public void navigateBack() {
        CLIGraphicController controller = null;
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
            this.navigateTo(CLIViewType.HOME);
        }
        else controller.start();
    }

}
