package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.exception.LoginException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import javafx.stage.Stage;
import java.util.Stack;

public class JFXNavigatorManager implements Navigator {

    private final SessionManager sessionManager;
    private final Stack<JFXGraphicController> navigationStack = new Stack<>();
    private JFXUI ui;

    public JFXNavigatorManager(Stage stage) {
        this.sessionManager = new SessionManager();
        this.ui = new JFXUI(this, this.sessionManager, stage);
    }

    @Override
    public void navigateTo(ViewType viewType) {
        if(checkAuthRequirement(viewType)){
            triggerLogin();
            if(sessionManager.getLoggedUser() == null || !sessionManager.isLoggedIn()) return;
        }
        try {
            pushAndInit(JFXGraphicControllerFactory.getInstance().createJFXGraphicController(viewType, sessionManager, this));
        } catch (Exception e) {
            e.printStackTrace();
            AlertProvider.showError("Server error", "Service temporarily not available");
        }
    }

    @Override
    public void navigateTo(ViewType viewType, Object data) {
        if(checkAuthRequirement(viewType)){
            triggerLogin();
            if(sessionManager.getLoggedUser() == null || !sessionManager.isLoggedIn()) return;
        }
        try {
            pushAndInit(JFXGraphicControllerFactory.getInstance().createJFXGraphicController(viewType, sessionManager, this, data));
        } catch (Exception e) {
            e.printStackTrace();
            AlertProvider.showError("Server error", "Service temporarily not available");
        }
    }

    private void pushAndInit(JFXGraphicController controller) {
        this.ui.loadViewAndSetController(controller);
        navigationStack.push(controller);
        controller.init();
    }

    private boolean checkAuthRequirement(ViewType viewType) {
        boolean requiresAuth = switch (viewType) {
            case MY_OFFERS, MY_BARTERS, OFFER_FORM -> true;
            default -> false;
        };
        return requiresAuth;
    }

    @Override
    public void navigateBack() {
        if (navigationStack.size() > 1) {
            navigationStack.pop();
            JFXGraphicController previousController = navigationStack.peek();
            if (previousController != null) {
                this.ui.loadViewFromController(previousController);
            }
        }
    }

    public void triggerLogin() {
        this.ui.requestLogin();
    }

    @Override
    public void reset() {
        this.navigationStack.clear();
        this.navigateTo(ViewType.ITEM_BROWSER);
    }

    @Override
    public void start() {
        this.navigateTo(ViewType.ITEM_BROWSER);
    }

}

