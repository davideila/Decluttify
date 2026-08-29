package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.controller.logic.LoginController;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.exception.LoginException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class JFXLoginPopupController{

    private LoginController loginController;
    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Label failedLogin;

    public JFXLoginPopupController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void onActionSignInButton(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean loginRes = false;

        try{
            loginRes = loginController.login(username, password);
            if (loginRes) {
                failedLogin.setText("");
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
            else{
                failedLogin.setText("Incorrect username or password");
            }
        }catch(LoginException e){
            failedLogin.setText(e.getMessage());
        }catch(DecluttifyException e){
            AlertProvider.showError("Application error", e.getMessage());
        }catch (Exception e) {
            AlertProvider.showError("System failure", "An unexpected error occurred.");
            e.printStackTrace();
        }

        usernameField.setText("");
        passwordField.setText("");
    }

    public void onActionSignUpButton(ActionEvent actionEvent) {
        AlertProvider.showInfo("Feature coming soon", "This feature is not yet available on this version");
    }
}
