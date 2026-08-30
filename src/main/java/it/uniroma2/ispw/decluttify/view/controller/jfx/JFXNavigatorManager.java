package it.uniroma2.ispw.decluttify.view.controller.jfx;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
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

    public JFXNavigatorManager(Stage stage, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.ui = new JFXUI(this, sessionManager, stage);
    }

    @Override
    public void navigateTo(ViewType viewType) {
        if (!(viewType instanceof JFXViewType)) {
            throw new IllegalArgumentException("Unsupported view for jfx: " + viewType);
        }
        if(checkAuthRequirement((JFXViewType) viewType)){
            triggerLogin();
            if(sessionManager.getLoggedUser() == null || !sessionManager.isLoggedIn()) return;
        }
        try {
            pushAndInit(JFXGraphicControllerFactory.getInstance().createJFXGraphicController((JFXViewType) viewType, sessionManager, this));
        } catch (RuntimeException e) {
            AlertProvider.showError("Navigation Error", "Unable to display the requested page.");
        }catch (Exception e) {
            AlertProvider.showError("System failure", "An unexpected error occurred.");
            //e.printStackTrace();
        }
    }

    @Override
    public void navigateTo(ViewType viewType, Object data) {
        if (!(viewType instanceof JFXViewType)) {
            throw new IllegalArgumentException("Unsupported view for jfx: " + viewType);
        }
        if(checkAuthRequirement((JFXViewType) viewType)){
            triggerLogin();
            if(sessionManager.getLoggedUser() == null || !sessionManager.isLoggedIn()) return;
            if(JFXViewType.OFFER_FORM == viewType) {
                if(sessionManager.getLoggedUser().getUsername().equals(((PreviewItemBean) data).getOwner())){
                    return;
                }
            }
        }
        try {
            pushAndInit(JFXGraphicControllerFactory.getInstance().createJFXGraphicController((JFXViewType) viewType, sessionManager, this, data));
        } catch (RuntimeException e) {
            AlertProvider.showError("Navigation Error", "Unable to display the requested page.");
        }catch (Exception e) {
            AlertProvider.showError("System failure", "An unexpected error occurred.");
            //e.printStackTrace();
        }
    }

    private void pushAndInit(JFXGraphicController controller) {
        boolean success = this.ui.loadViewAndSetController(controller);
        if(success) {
            navigationStack.push(controller);
            controller.init();
        }
    }

    private boolean checkAuthRequirement(JFXViewType viewType) {
        return viewType.isLoginRequired();
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
        this.navigateTo(JFXViewType.ITEM_BROWSER);
    }

    @Override
    public void start() {
        this.navigateTo(JFXViewType.ITEM_BROWSER);
    }

}

