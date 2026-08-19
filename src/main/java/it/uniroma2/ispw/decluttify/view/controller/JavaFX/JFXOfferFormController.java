package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeOfferController;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class JFXOfferFormController extends JFXGraphicController {

    private final MakeOfferController makeBarterController;
    private PreviewItemBean targetItem;
    private List<PreviewItemBean> offeredItems = new ArrayList<>();

    @FXML private ImageView requestedItemImage;
    @FXML private Label requestedItemName;
    @FXML private Label ownerName;
    @FXML private VBox offeredItemsContainer;
    @FXML private Button confirmButton;


    public JFXOfferFormController(Navigator navigator, SessionManager sm, PreviewItemBean targetItem) {
        super(navigator, sm, JFXViewType.OFFER_FORM);
        this.targetItem = targetItem;
        this.makeBarterController = new MakeOfferController(sm);
        this.setInSidebar(false);
    }

    @Override
    public void init() {
        requestedItemName.setText(targetItem.getName());
        ownerName.setText("Owner: " + targetItem.getOwner());
        if (targetItem.getImages() != null && !targetItem.getImages().isEmpty()) {
            try (InputStream is = new FileInputStream(System.getProperty("user.dir") + "\\" + targetItem.getImages().getFirst())) {
                Image image = new Image(is);
                this.requestedItemImage.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                requestedItemImage.setImage(new Image(System.getProperty("user.dir") + "\\" + "placeholder_item.png"));
            }
        }
    }

    // Method for the button to add item to an offer. It opens a popup for the item to choose on logged user inventory
    @FXML
    private void handleAddMyItem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/InventoryPopup.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initOwner(confirmButton.getScene().getWindow());
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Select an item from your inventory");
            popupStage.setScene(new Scene(root));

            // Open popup inventory, get items and give items data to popup
            JFXInventoryPopupController popupController = loader.getController();
            List<PreviewItemBean> myItems = makeBarterController.loadUserInventory(sessionManager.getLoggedUser());
            popupController.setParameters(this, popupStage, myItems);
            popupStage.showAndWait();

        }catch(Exception e){
            this.handleException(e);
        }
    }

    public void addOfferedItem(PreviewItemBean pib) {
        offeredItems.add(pib);

        // UI for an added item
        Label itemLabel = new Label("- " + pib.getName());
        itemLabel.setStyle("-fx-font-size: 16; -fx-padding: 5;");
        offeredItemsContainer.getChildren().add(itemLabel);

        confirmButton.setDisable(offeredItems.isEmpty());
    }

    // Button to submit the barter proposal
    @FXML
    private void handleSubmit(){
        boolean result = false;
        try{
            makeBarterController.submitOffer(this.offeredItems, this.targetItem, sessionManager.getLoggedUser());
            result = true;
        }catch(Exception e){
            this.handleException(e);//TODO
        }
        if (result) {
            AlertProvider.showInfo("Success", "OfferStateMachine submitted!");
            navigator.navigateTo(JFXViewType.MY_OFFERS);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        navigator.navigateBack();
    }

}
