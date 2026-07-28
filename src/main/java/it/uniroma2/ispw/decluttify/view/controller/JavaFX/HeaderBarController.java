package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.controller.logic.LoginController;
import it.uniroma2.ispw.decluttify.patterns.Observer.Observer;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;


public class HeaderBarController extends GraphicController implements Observer{

    private final LoginController  loginController;

    @FXML Button profileButton;
    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Button logoutButton;
    @FXML Label errorLabel;
    @FXML private StackPane badgePane;
    @FXML private Label notificationCountLabel;

    public HeaderBarController(Navigator navigator, SessionManager sm, ViewType viewType) {
        super(navigator, sm, viewType);
        this.loginController = new LoginController(sm);
    }

    public void init() {
        this.sessionManager.attach(this);
    }

    // Methods for onAction button click event linked through fxml

    @FXML
    void handleBackButton(ActionEvent event) {
        this.navigator.navigateBack();
    }

    //this button could be login or go to user details if logged already
    @FXML
    void handleProfileButton(ActionEvent event) {
        if (!sessionManager.isLoggedIn()) {
            Stage popupStage = new Stage();
            popupStage.initOwner(profileButton.getScene().getWindow());
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Login");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/LoginPopupView.fxml"));
                LoginPopupController popupController = new LoginPopupController(navigator, sessionManager, null);
                loader.setController(popupController);
                Parent root = loader.load();
                popupStage.setScene(new Scene(root));
                popupStage.showAndWait();
            }catch (IOException e){
                AlertProvider.showError("Server Error", "Service temporarily not available");
            }

            if (sessionManager.getLoggedUser() != null) {
                profileButton.setText(sessionManager.getLoggedUser().getUsername());
            }
        }
        else{
            AlertProvider.showInfo("Feature coming soon", "This feature is not yet available on this version");
        }
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        if(logoutButton.getText().equals("Sign Out")){
           loginController.logout();
           this.navigator.reset();
        }
        else{
            AlertProvider.showInfo("Feature coming soon", "This feature is not yet available on this version");
        }
    }

    @Override
    public void update() {
        if (sessionManager.isLoggedIn()) {
            profileButton.setText(sessionManager.getLoggedUser().getUsername());
            logoutButton.setText("Sign Out");
            if(sessionManager.getNotifications() != null){
                badgePane.setVisible(true);
                this.notificationCountLabel.setText(String.valueOf(sessionManager.getNotifications().size()));
            }

        } else{
            profileButton.setText("Sign In");
            logoutButton.setText("Sign Up");
            profileButton.setDisable(false);
            badgePane.setVisible(false);
        }
    }

    public void handleNotificationClick(MouseEvent mouseEvent) {
        AlertProvider.showInfo("Feature coming soon", "This feature is not yet available on this version");
    }

}
