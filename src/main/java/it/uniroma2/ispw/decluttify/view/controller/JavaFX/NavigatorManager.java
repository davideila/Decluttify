package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.exception.LoginException;
import it.uniroma2.ispw.decluttify.patterns.Observer.Observer;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.Stack;


public class NavigatorManager implements Observer, Navigator {

    private static final String ACTIVE_BUTTON_STYLE =
            "-fx-opacity: 1.0; -fx-background-color: #4CAF50; -fx-effect: dropshadow(gaussian, rgba(76, 175, 80, 0.7), 10, 0, 0, 0);";

    private final SessionManager sessionManager;
    private final Stack<GraphicController> navigationStack = new Stack<>();
    private final HeaderBarController headerBarController;

    @FXML private Button offerButton;
    @FXML private Button barterButton;
    @FXML private Button inventoryButton;
    @FXML private Button itemBrowseButton;
    @FXML private Button donateButton;
    @FXML private BorderPane borderPane;

    public NavigatorManager() {
        this.sessionManager = new SessionManager();
        this.headerBarController = new HeaderBarController(this, sessionManager, null);
    }

    @Override
    public void navigateTo(ViewType viewType) {
        checkAuthRequirement(viewType);
        switch (viewType) {
            case ITEM_BROWSER -> pushAndInit("/it/uniroma2/ispw/decluttify/views/ItemBrowserView.fxml",
                    new ItemBrowserController(this, sessionManager));
            case MY_OFFERS -> pushAndInit("/it/uniroma2/ispw/decluttify/views/MyOffersView.fxml",
                    new MyOffersController(this, sessionManager));
            case MY_BARTERS -> pushAndInit("/it/uniroma2/ispw/decluttify/views/MyBartersView.fxml",
                    new MyBartersController(this, sessionManager));
            case LOGIN -> this.triggerLogin();
            default -> AlertProvider.showError("Server error", "Unexpected view type");
        }
        refreshSidebar();
    }

    @Override
    public void navigateTo(ViewType viewType, Object data) {
        checkAuthRequirement(viewType);
        switch (viewType) {
            case ITEM_DETAILS -> pushAndInit("/it/uniroma2/ispw/decluttify/views/ItemDetailsView.fxml",
                    new ItemDetailsController(this, sessionManager, (PreviewItemBean) data));
            case OFFER_FORM -> pushAndInit("/it/uniroma2/ispw/decluttify/views/OfferFormView.fxml",
                    new OfferFormController(this, sessionManager, (PreviewItemBean) data));
            default -> AlertProvider.showError("Server error", "Unexpected view type");
        }
        refreshSidebar();
    }

    private void pushAndInit(String fxmlPath, GraphicController controller) {
        FXMLLoader loader = loadView(fxmlPath, controller);
        if (loader != null) {
            GraphicController gc = loader.getController();
            navigationStack.push(gc);
            gc.init();
        }
    }

    private FXMLLoader loadView(String fxmlPath, GraphicController graphicController) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setController(graphicController);
        try {
            Parent nextView = loader.load();
            borderPane.setCenter(nextView);
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
            AlertProvider.showError("Server error", "Service temporarily unavailable");
            return null;
        }
    }

    private void checkAuthRequirement(ViewType viewType) {
        boolean requiresAuth = switch (viewType) {
            case MY_OFFERS, MY_BARTERS, OFFER_FORM -> true;
            default -> false;
        };
        if (requiresAuth && !sessionManager.isLoggedIn()) {
            throw new LoginException("Login required for: " + viewType);
        }
    }

    @Override
    public void navigateBack() {
        if (navigationStack.size() > 1) {
            navigationStack.pop();
            GraphicController previousController = navigationStack.peek();
            if (previousController != null) {
                borderPane.setCenter(previousController.getView());
                refreshSidebar();
            }
        }
    }

    public void triggerLogin() {
        headerBarController.handleProfileButton(null);
    }

    // Handlers button FXML
    @FXML void handleInventoryButton(ActionEvent event) { showComingSoon(); }
    @FXML void handleDonateButton(ActionEvent event) { showComingSoon(); }
    @FXML public void handleItemBrowseButton(ActionEvent event) { navigateTo(ViewType.ITEM_BROWSER); }
    @FXML public void handleOfferButton(ActionEvent event) { navigateTo(ViewType.MY_OFFERS); }
    @FXML public void handleBarterButton(ActionEvent event) { navigateTo(ViewType.MY_BARTERS); }

    private void showComingSoon() {
        AlertProvider.showInfo("Feature coming soon", "This feature is not yet available on this version");
    }

   /* Method to adjust view of buttons on sidebar */
    public void refreshSidebar() {
        boolean isLoggedIn = sessionManager.isLoggedIn();

        // Reset stile
        itemBrowseButton.setStyle("");
        barterButton.setStyle("");
        offerButton.setStyle("");
        inventoryButton.setStyle("");

        // Disable buttons if user not logged
        inventoryButton.setDisable(!isLoggedIn);
        barterButton.setDisable(!isLoggedIn);
        offerButton.setDisable(!isLoggedIn);
        itemBrowseButton.setDisable(false);

        // Highlight current view sidebar button
        if (!navigationStack.isEmpty()) {
            ViewType currentView = navigationStack.peek().getViewType();
            Button currentButton = switch (currentView) {
                case ITEM_BROWSER -> itemBrowseButton;
                case MY_BARTERS -> barterButton;
                case MY_OFFERS -> offerButton;
                default -> null;
            };
            if (currentButton != null) {
                currentButton.setDisable(true);
                currentButton.setStyle(ACTIVE_BUTTON_STYLE);
            }
        }
    }

    @Override
    public void reset() {
        this.navigationStack.clear();
        this.navigateTo(ViewType.ITEM_BROWSER);
    }

    @Override
    public void start() {
        sessionManager.attach(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/HeaderBar.fxml"));
        loader.setController(headerBarController);
        try {
            Parent headerView = loader.load();
            borderPane.setTop(headerView);
        } catch (IOException e) {
            AlertProvider.showError("Server Error", "Service temporarily unavailable.");
            Platform.exit();
            return;
        }
        this.headerBarController.init();
        this.navigateTo(ViewType.ITEM_BROWSER);
    }

    @Override
    public void update() {
        refreshSidebar();
    }
}

