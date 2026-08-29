package it.uniroma2.ispw.decluttify.view.controller.jfx;

import it.uniroma2.ispw.decluttify.bean.NotificationBean;
import it.uniroma2.ispw.decluttify.controller.logic.LoginController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


public class JFXHeaderBarController {

    private LoginController loginController;
    private Navigator navigator;
    private SessionManager sessionManager;
    private final String LOGIN_FXML = "/it/uniroma2/ispw/decluttify/views/LoginPopupView.fxml";
    private ContextMenu notificationMenu;

    @FXML Button profileButton;
    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Button logoutButton;
    @FXML Label errorLabel;
    @FXML private StackPane badgePane;
    @FXML private Label notificationCountLabel;
    @FXML private Label notificationIcon;

    public JFXHeaderBarController(Navigator navigator, SessionManager sessionManager) {
        this.loginController = new LoginController(sessionManager);
        this.navigator = navigator;
        this.sessionManager = sessionManager;
        this.notificationMenu = new ContextMenu();
        this.notificationMenu.setStyle("-fx-max-width: 250px; -fx-min-width: 200px; -fx-font-size: 12px;");
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
                AlertProvider.showError("Operation Failed", "Unable to open the login window. Please try again.");
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
        if (notificationMenu.isShowing()) {
            notificationMenu.hide();
            return;
        }

        notificationMenu.getItems().clear();
        List<NotificationBean> notifications = this.sessionManager.getNotifications();
        if (notifications.isEmpty()) {
            MenuItem emptyItem = new MenuItem("No new notifications");
            emptyItem.setDisable(true);
            notificationMenu.getItems().add(emptyItem);
        } else {
            for (NotificationBean notif : notifications) {
                CustomMenuItem item = new CustomMenuItem(createCustomNotificationCell(notif));
                item.setHideOnClick(false);
                notificationMenu.getItems().add(item);
            }
        }
        // show the notification, but get the width and reshow it with the right top side angulus touching the notification icon instead of the left top side
        notificationMenu.show(notificationIcon, Side.BOTTOM, 0, 0);
        double menuWidth = notificationMenu.getWidth();
        double buttonWidth = notificationIcon.getWidth();
        double offsetX = buttonWidth - menuWidth;

        notificationMenu.show(notificationIcon, Side.BOTTOM, offsetX, 0);
    }

    private HBox createCustomNotificationCell(NotificationBean notif) {
        HBox cell = new HBox();
        cell.setSpacing(10);
        Label notifMessage = new Label(notif.getMessage());
        Circle unreadDot = new Circle(4);
        unreadDot.setFill(Color.RED);
        cell.setOnMouseClicked(event -> {
            this.sessionManager.getNotifications().remove(notif);
            unreadDot.setVisible(false);
            this.update();
            if(notif.getType().equalsIgnoreCase("OFFER")){
                event.consume();
                this.navigator.navigateTo(JFXViewType.MY_OFFERS);
            }
        });
        cell.getChildren().addAll(notifMessage, unreadDot);
        return cell;
    }
}
