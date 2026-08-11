package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class JFXSidebarController {

    Navigator navigator;
    @FXML private Button offerButton;
    @FXML private Button barterButton;
    @FXML private Button inventoryButton;
    @FXML private Button itemBrowseButton;
    @FXML private Button donateButton;
    @FXML private BorderPane borderPane;
    private static final String ACTIVE_BUTTON_STYLE =
            "-fx-opacity: 1.0; -fx-background-color: #4CAF50; -fx-effect: dropshadow(gaussian, rgba(76, 175, 80, 0.7), 10, 0, 0, 0);";

    public JFXSidebarController(Navigator navigator) {
        this.navigator = navigator;
    }

    // Handlers button FXML
    @FXML void handleInventoryButton(ActionEvent event) { AlertProvider.showComingSoon(); }
    @FXML void handleDonateButton(ActionEvent event) { AlertProvider.showComingSoon(); }
    @FXML void handleItemBrowseButton(ActionEvent event) {
        navigator.navigateTo(ViewType.ITEM_BROWSER);
    }
    @FXML void handleOfferButton(ActionEvent event) {
        navigator.navigateTo(ViewType.MY_OFFERS);
    }
    @FXML void handleBarterButton(ActionEvent event) {
        navigator.navigateTo(ViewType.MY_BARTERS);
    }

    public void update(boolean enable){
        this.offerButton.setDisable(!enable);
        this.barterButton.setDisable(!enable);
        this.inventoryButton.setDisable(!enable);
        this.donateButton.setDisable(!enable);
    }

    public void init(){
        this.update(false);
        refreshCurrentButton(ViewType.ITEM_BROWSER);
    }

    public void refreshCurrentButton(ViewType viewType) {
            switch (viewType) {
                case ITEM_BROWSER:
                    this.itemBrowseButton.setDisable(true);
                    this.itemBrowseButton.setStyle(ACTIVE_BUTTON_STYLE);
                    this.barterButton.setStyle("");
                    this.offerButton.setStyle("");
                    break;
                case MY_OFFERS:
                    this.offerButton.setDisable(true);
                    this.offerButton.setStyle(ACTIVE_BUTTON_STYLE);
                    this.barterButton.setStyle("");
                    this.itemBrowseButton.setStyle("");
                    this.itemBrowseButton.setDisable(false);
                    break;
                case MY_BARTERS:
                    this.barterButton.setDisable(true);
                    this.barterButton.setStyle(ACTIVE_BUTTON_STYLE);
                    this.itemBrowseButton.setStyle("");
                    this.offerButton.setStyle("");
                    this.itemBrowseButton.setDisable(false);
                    break;
                default:
                    this.barterButton.setStyle("");
                    this.itemBrowseButton.setStyle("");
                    this.offerButton.setStyle("");
                    this.itemBrowseButton.setDisable(false);
                    break;
        }
    }

}
