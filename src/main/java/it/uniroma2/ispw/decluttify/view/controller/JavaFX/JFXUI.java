package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

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

public class JFXUI implements SessionObserver {

    private static final double RESIZE_CONSTANT = 0.75;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(graphicController.getViewType().getFxmlPath()));
        loader.setController(graphicController);
        sidebarController.refreshCurrentButton(graphicController.getViewType());
        sidebarController.update(sessionManager.isLoggedIn());
        try {
            Parent nextView = loader.load();
            graphicController.setView(nextView);
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
