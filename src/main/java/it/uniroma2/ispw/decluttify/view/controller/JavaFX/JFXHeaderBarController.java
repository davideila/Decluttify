package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.controller.logic.LoginController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
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


public class JFXHeaderBarController {

    private LoginController loginController;
    private Navigator navigator;
    private SessionManager sessionManager;
    private final String LOGIN_FXML = "/it/uniroma2/ispw/decluttify/views/LoginPopupView.fxml";

    @FXML Button profileButton;
    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Button logoutButton;
    @FXML Label errorLabel;
    @FXML private StackPane badgePane;
    @FXML private Label notificationCountLabel;

    public JFXHeaderBarController(Navigator navigator, SessionManager sessionManager) {
        this.loginController = new LoginController(sessionManager);
        this.navigator = navigator;
        this.sessionManager = sessionManager;
    }

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
                JFXLoginPopupController popupController = new JFXLoginPopupController(loginController);
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
            if (event != null){
                AlertProvider.showComingSoon();
            }
        }
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        if(logoutButton.getText().equals("Logout")){
           loginController.logout();
           this.navigator.reset();
        }
        else{
            AlertProvider.showComingSoon();
        }
    }

    public void update() {
        if (sessionManager.isLoggedIn()) {
            profileButton.setText(sessionManager.getLoggedUser().getUsername());
            logoutButton.setText("Logout");
            if(sessionManager.getNotifications() != null){
                badgePane.setVisible(true);
                this.notificationCountLabel.setText(String.valueOf(sessionManager.getNotifications().size()));
            }

        } else{
            profileButton.setText("Login");
            logoutButton.setText("Register");
            profileButton.setDisable(false);
            badgePane.setVisible(false);
        }
    }

    public void handleNotificationClick(MouseEvent mouseEvent) {
        AlertProvider.showComingSoon();
    }

}
