package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeOfferController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.MediaLoader;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.itemTile.JFXItemTileOfferForm;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JFXOfferFormController extends JFXGraphicController {

    private final MakeOfferController makeOfferController;
    private FullItemBean targetItem;
    private List<PreviewItemBean> offeredItems = new ArrayList<>();
    private Stage popupStage;
    private JFXInventoryPopupController inventoryPopupController;
    private JFXItemTileOfferForm item1;
    private JFXItemTileOfferForm item2;
    private JFXItemTileOfferForm item3;

    @FXML private ImageView requestedItemImage;
    @FXML private Label requestedItemName;
    @FXML private VBox vboxItemsOffered;
    @FXML private Button submitButton;
    @FXML private Label ownerLabel;
    @FXML private Label locationLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label conditionsLabel;
    @FXML private Label activeOffersLabel;


    public JFXOfferFormController(Navigator navigator, SessionManager sm, FullItemBean targetItem) {
        super(navigator, sm, JFXViewType.OFFER_FORM);
        this.targetItem = targetItem;
        this.makeOfferController = new MakeOfferController(sm);
    }

    @Override
    public void init() {
        // Target item layout
        requestedItemName.setText(targetItem.getName());
        ownerLabel.setText("Owner: " + targetItem.getOwner());
        this.requestedItemImage.setImage(MediaLoader.getInstance().loadItemImage(targetItem.getMainImageName()));
        this.locationLabel.setText("Location: " + targetItem.getLocation());
        this.descriptionLabel.setText("Description: " + targetItem.getDescription());
        this.activeOffersLabel.setText("Active offers: " + targetItem.getNumOffers());
        this.conditionsLabel.setText("Conditions: " + targetItem.getCondition());

        // Offered items layout
        this.vboxItemsOffered.getChildren().clear();
        item1 = new JFXItemTileOfferForm();
        item2 = new JFXItemTileOfferForm();
        item3 = new JFXItemTileOfferForm();
        item1.getDeleteIcon().setOnMouseClicked(event -> removeItem(item1, event));
        item2.getDeleteIcon().setOnMouseClicked(event -> removeItem(item2, event));
        item3.getDeleteIcon().setOnMouseClicked(event -> removeItem(item3, event));
        this.vboxItemsOffered.getChildren().addAll(item1, item2, item3);
        for(Node node: vboxItemsOffered.getChildren()){
            node.setOnMouseClicked(new javafx.event.EventHandler<javafx.scene.input.MouseEvent>(){
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    handleAddItems();
                }
            });
        }
    }

    private void removeItem(JFXItemTileOfferForm tile, javafx.scene.input.MouseEvent event) {
        PreviewItemBean itemToRemove = tile.getItem();
        if (itemToRemove != null) {
            offeredItems.removeIf(item -> item.getId() == itemToRemove.getId());
            tile.setItemData(null);
            updateOfferedItemsView();
        }
        event.consume();
    }

    // Method for the button to add item to an offer. It opens a popup for the item to choose on logged user inventory
    @FXML
    public void handleAddItems() {
        try {
            if (this.popupStage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/InventoryPopup.fxml"));
                Parent root = loader.load();
                this.inventoryPopupController = loader.getController();

                this.popupStage = new Stage();
                popupStage.initOwner(submitButton.getScene().getWindow());
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Select an item from your inventory");
                popupStage.setScene(new Scene(root));
            }
            this.inventoryPopupController.init(makeOfferController, sessionManager.getLoggedUser(), offeredItems);
            popupStage.showAndWait();
            if (this.inventoryPopupController.isConfirmed()) {
                this.offeredItems = this.inventoryPopupController.getSelectedItems();
                this.updateOfferedItemsView();
            }
        } catch (IOException e) {
            e.printStackTrace();
            AlertProvider.showError("Error", "Cannot open inventory, service temporarily unavailable.");
        }
    }

    private void updateOfferedItemsView(){
        int totalTiles = this.vboxItemsOffered.getChildren().size();
        int size = this.offeredItems.size();

        for (int i = 0; i < size && i < totalTiles; i++) {
            JFXItemTileOfferForm tile = (JFXItemTileOfferForm) this.vboxItemsOffered.getChildren().get(i);
            tile.setItem(this.offeredItems.get(i));
        }

        for (int j = size; j < totalTiles; j++) {
            JFXItemTileOfferForm tile = (JFXItemTileOfferForm) this.vboxItemsOffered.getChildren().get(j);
            tile.setPlaceholder();
        }

        if(!this.offeredItems.isEmpty()){
            this.submitButton.setDisable(false);
        }
        else{
            this.submitButton.setDisable(true);
        }
    }

    // Button to submit the offer
    @FXML
    public void handleSubmit(){
        boolean result = false;
        try{
            makeOfferController.submitOffer(this.offeredItems, this.targetItem, sessionManager.getLoggedUser());
            result = true;
        }catch(Exception e){
            this.handleException(e);//TODO
        }
        if (result) {
            AlertProvider.showInfo("Success", "Offer submitted!");
            navigator.reset();
            navigator.navigateTo(JFXViewType.MY_OFFERS);
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        navigator.navigateBack();
    }

}
