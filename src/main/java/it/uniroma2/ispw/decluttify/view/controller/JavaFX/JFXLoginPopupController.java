package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.controller.logic.LoginController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class JFXLoginPopupController{

    private LoginController loginController;
    private SessionManager sessionManager;
    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Label failedLogin;

    public JFXLoginPopupController(SessionManager sm, LoginController loginController) {
        this.loginController = loginController;
        this.sessionManager = sm;
    }

    public void onActionSignInButton(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean loginRes = false;
        try{
            loginRes = loginController.login(username, password);
            if(sessionManager.isLoginLocked()) {
                AlertProvider.showInfo("Warning", "Too many failed attempts. Try again later.");
            }
        }catch(Exception e){
            AlertProvider.showInfo("Error", "Login failed.");
        }

        if (loginRes) {
            failedLogin.setText("");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        else
        {
            failedLogin.setText("Incorrect username or password.");
        }
        usernameField.setText("");
        passwordField.setText("");
    }

    public void onActionSignUpButton(ActionEvent actionEvent) {
        AlertProvider.showInfo("Feature coming soon", "This feature is not yet available on this version");
    }
}
