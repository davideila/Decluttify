package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.controller.logic.LoginController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginPopupController extends GraphicController{

    private int loginTries;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label failedLogin;

    public void onActionSignInButton(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        LoginController lgc = new LoginController();
        boolean loginRes = false;

        try{
            if(SessionManager.getInstance().isLoginLocked()){
                AlertProvider.showInfo("Warning", "Too many failed attempts. Try again later.");
            }
            else {
                loginRes = lgc.login(username, password);
            }
        }catch(Exception e){
            this.handleException(e);
        }

        if (loginRes) {
            failedLogin.setText("");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            loginTries=0;
        }
        else
        {
            failedLogin.setText("Incorrect username or password.");
            loginTries++;
            if(loginTries == 3) {
                SessionManager.getInstance().lockLogin();
            }
        }
        usernameField.setText("");
        passwordField.setText("");
    }
}
