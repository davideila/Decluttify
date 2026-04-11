package it.uniroma2.ispw.decluttify.view.controller.CLI;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import java.util.Stack;

public class NavigatorManager {
    Stack<GraphicController> navigationHistory = new Stack<>();

    private static NavigatorManager instance = null;

    protected NavigatorManager(){
    }

    public void start(){
        this.navigateToHome();
    }

    public static synchronized NavigatorManager getInstance() {
        if(instance == null){
            instance = new NavigatorManager();
        }
        return instance;
    }

    private void pushAndStart(GraphicController nextController) {
        if (!navigationHistory.isEmpty()) {
            navigationHistory.peek().listen(false);
        }
        navigationHistory.push(nextController);
        nextController.initialize();
    }

    public void navigateToHome(){
        GraphicController controller = new HomeController();
        this.pushAndStart(controller);
    }

    public void reset(){
        if(!navigationHistory.isEmpty()){
            navigationHistory.peek().listen(false);
            navigationHistory.clear();
        }
        this.navigateToHome();
    }

    public void navigateToBack() {
        GraphicController controller = null;
        if (!navigationHistory.isEmpty()) {
            controller = navigationHistory.pop();
            controller.listen(false);
        }
        while(!navigationHistory.isEmpty()){
            controller = navigationHistory.peek();
            if(!SessionManager.getInstance().isLoggedIn() && controller.isLoginRequired()){
                controller.listen(false);
                navigationHistory.pop();
                controller = null;
            }
            else break;
        }
        if(controller == null){
            this.navigateToHome();
        }
        else controller.start();
    }

    public void handleLogout() {
        it.uniroma2.ispw.decluttify.controller.logic.LoginController loginController = new it.uniroma2.ispw.decluttify.controller.logic.LoginController(); //names!!!
        loginController.logout();
        reset();
    }

    public void navigateToProfile() {
    }

    public void navigateToLogin() {
        pushAndStart(new LoginController());
        navigateToBack();
    }

    public void navigateToItemBrowser() {
        pushAndStart(new ItemBrowserController());
    }

    public void navigateToMyItems() {
        pushAndStart(new MyInventoryController());
    }

    public void navigatetoMyBarters() {
        pushAndStart(new MyBartersController());
    }

    public void navigateToMyOffers() {
        pushAndStart(new MyOffersController());
    }

    public void navigateToItemDetails(int id) {
        pushAndStart(new ItemDetailsController(id));
    }

    public void navigateToMakeOffer(PreviewItemBean pib) {
        pushAndStart(new MakeOfferController(pib));
    }
}
