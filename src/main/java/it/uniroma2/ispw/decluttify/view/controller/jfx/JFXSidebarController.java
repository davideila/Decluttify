package it.uniroma2.ispw.decluttify.view.controller.jfx;

import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
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
            "-fx-opacity: 1.0; -fx-background-color: #3498db; -fx-border-color: #1d6fa5; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;";

    public JFXSidebarController(Navigator navigator) {
        this.navigator = navigator;
    }

    // Handlers button FXML
    @FXML void handleInventoryButton(ActionEvent event) { AlertProvider.showComingSoon(); }
    @FXML void handleDonateButton(ActionEvent event) { AlertProvider.showComingSoon(); }
    @FXML void handleItemBrowseButton(ActionEvent event) {
        navigator.navigateTo(JFXViewType.ITEM_BROWSER);
    }
    @FXML void handleOfferButton(ActionEvent event) {
        navigator.navigateTo(JFXViewType.MY_OFFERS);
    }
    @FXML void handleBarterButton(ActionEvent event) {
        AlertProvider.showComingSoon();
    }

    public void update(boolean enable){
        this.offerButton.setDisable(!enable);
        this.barterButton.setDisable(!enable);
        this.inventoryButton.setDisable(!enable);
        this.donateButton.setDisable(!enable);
    }

    public void init(){
        this.update(false);
        refreshCurrentButton(JFXViewType.ITEM_BROWSER);
    }

    public void refreshCurrentButton(JFXViewType viewType) {
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
                default:
                    this.barterButton.setStyle("");
                    this.itemBrowseButton.setStyle("");
                    this.offerButton.setStyle("");
                    this.itemBrowseButton.setDisable(false);
                    break;
        }
    }

}
