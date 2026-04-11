package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.controller.logic.LoginController;
import it.uniroma2.ispw.decluttify.patterns.Observer.Observer;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderBarController implements Initializable, Observer{

    private int loginTries = 0;
    @FXML
    Button profileButton;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button logoutButton;
    @FXML
    Label errorLabel;
    @FXML private StackPane badgePane;
    @FXML private Label notificationCountLabel;

    private SessionManager sessionManager;


    // Methods for onAction button click event linked through fxml

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        MainGraphicController mainController = MainGraphicController.getInstance();
        mainController.goBack();
    }

    @FXML
    void handleProfileButton(ActionEvent event) throws IOException {
        if (SessionManager.getInstance().getLoggedUser() == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/LoginPopupView.fxml"));
                Parent root = loader.load();

                LoginPopupController loginPopupController = loader.getController();
                Stage popupStage = new Stage();
                popupStage.initOwner(profileButton.getScene().getWindow());
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setScene(new Scene(root));

                // Wait for the popup to be closed
                popupStage.showAndWait();

                if (SessionManager.getInstance().getLoggedUser() != null) {
                    profileButton.setText(SessionManager.getInstance().getLoggedUser().getUsername());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            // TODO MainGraphicController.getInstance().showUserDetailsView(SessionManager.getUser());
        }
        this.update();
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        LoginController  loginController = new LoginController();
        loginController.logout();
        MainGraphicController.getInstance().handleLogout();
        this.update();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.sessionManager = SessionManager.getInstance();
        this.sessionManager.attach(this);
    }

    @Override
    public void update() {
        if (SessionManager.getInstance().isLoggedIn()) {
            profileButton.setText(SessionManager.getInstance().getLoggedUser().getUsername());
            logoutButton.setText("Sign out");
            if(SessionManager.getInstance().getNotifications() != null){
                badgePane.setVisible(true);
                this.notificationCountLabel.setText(String.valueOf(SessionManager.getInstance().getNotifications().size()));
            }

        } else{
            profileButton.setText("Sign in");
            logoutButton.setText("Sign on");
            profileButton.setDisable(false);
            badgePane.setVisible(false);
        }
    }

    public void handleNotificationClick(MouseEvent mouseEvent) {

    }
}
