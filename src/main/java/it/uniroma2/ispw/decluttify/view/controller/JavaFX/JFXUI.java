package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.patterns.Observer.Observer;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class JFXUI implements Observer {

    private static final double RESIZE_CONSTANT = 0.75;
    private final String ITEM_BROWSER_FXML = "/it/uniroma2/ispw/decluttify/views/ItemBrowserView.fxml";
    private final String ITEM_DETAILS_FXML = "/it/uniroma2/ispw/decluttify/views/ItemDetailsView.fxml";
    private final String OFFER_FORM_FXML = "/it/uniroma2/ispw/decluttify/views/OfferFormView.fxml";
    private final String MY_OFFERS_FXML = "/it/uniroma2/ispw/decluttify/views/MyOffersView.fxml";
    private final String MY_BARTERS_FXML = "/it/uniroma2/ispw/decluttify/views/MyBartersView.fxml";
    private final String HEADER_BAR_FXML = "/it/uniroma2/ispw/decluttify/views/HeaderBar.fxml";
    private final String SIDE_BAR_FXML = "/it/uniroma2/ispw/decluttify/views/SideBar.fxml";
    private final String JFXUI_FXML = "/it/uniroma2/ispw/decluttify/views/JFXUI.fxml";
    private JFXHeaderBarController headerBarController;
    private JFXSidebarController sidebarController;
    private SessionManager sessionManager;
    @FXML private BorderPane borderPane;
    @FXML private VBox vbox;

    public JFXUI(Navigator navigator, SessionManager sessionManager, Stage stage) {
        this.sessionManager = sessionManager;
        sessionManager.attach(this);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXUI_FXML));
            loader.setController(this);
            Parent root = loader.load();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();
            double sceneWidth = screenWidth * RESIZE_CONSTANT;
            double sceneHeight = screenHeight * RESIZE_CONSTANT;
            stage.setResizable(false);
            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            stage.setScene(scene);

            FXMLLoader loaderHead = new FXMLLoader(getClass().getResource(HEADER_BAR_FXML));
            FXMLLoader loaderSide = new FXMLLoader(getClass().getResource(SIDE_BAR_FXML));
            headerBarController = new JFXHeaderBarController(navigator, sessionManager);
            sidebarController = new JFXSidebarController(navigator);
            loaderHead.setController(headerBarController);
            loaderSide.setController(sidebarController);
            Parent headerBarView = loaderHead.load();
            Parent sideBarView = loaderSide.load();
            sidebarController.init();
            this.borderPane.setTop(headerBarView);
            this.vbox.getChildren().add(sideBarView);

            stage.show();
        }catch(IOException e){
            e.printStackTrace();
            AlertProvider.showError("Server Error", "Service temporarily unavailable.");
            Platform.exit();
        }
    }

    void loadViewAndSetController(JFXGraphicController graphicController) {
        FXMLLoader loader;
        switch(graphicController.getViewType()) {
            case ITEM_BROWSER:
                loader = new FXMLLoader(getClass().getResource(ITEM_BROWSER_FXML));
                break;
            case ITEM_DETAILS:
                loader = new FXMLLoader(getClass().getResource(ITEM_DETAILS_FXML));
                break;
            case OFFER_FORM:
                loader = new FXMLLoader(getClass().getResource(OFFER_FORM_FXML));
                break;
            case MY_OFFERS:
                loader = new FXMLLoader(getClass().getResource(MY_OFFERS_FXML));
                break;
            case MY_BARTERS:
                loader = new FXMLLoader(getClass().getResource(MY_BARTERS_FXML));
                break;
            case null, default:
                throw new RuntimeException("Cannot load view: view type unknown");
        }
        loader.setController(graphicController);
        sidebarController.refreshCurrentButton(graphicController.getViewType());
        sidebarController.update(sessionManager.isLoggedIn());
        try {
            Parent nextView = loader.load();
            borderPane.setCenter(nextView);
        } catch (IOException e) {
            e.printStackTrace();
            AlertProvider.showError("Server error", "Service temporarily unavailable");
        }
    }

    public void loadViewFromController(JFXGraphicController graphicController) {
        borderPane.setCenter(graphicController.getView());
        sidebarController.refreshCurrentButton(graphicController.getViewType());
        sidebarController.update(sessionManager.isLoggedIn());
    }

    @Override
    public void update() {
        this.headerBarController.update();
        this.sidebarController.update(sessionManager.isLoggedIn());
    }

    public void requestLogin(){
        headerBarController.handleProfileButton(null);
    }

}
