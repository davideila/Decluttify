package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeBarterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MakeOfferController extends GraphicController implements Initializable, DataReceiver<PreviewItemBean> {

    @FXML private ImageView requestedItemImage;
    @FXML private Label requestedItemName;
    @FXML private Label ownerName;
    @FXML private VBox offeredItemsContainer;
    @FXML private Button confirmButton;

    private PreviewItemBean targetItem;
    private final List<PreviewItemBean> offeredItems = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.isInSidebar = false;
    }

    public void setTargetItem(PreviewItemBean item) {
        this.targetItem = item;
        requestedItemName.setText(item.getName());
        ownerName.setText("Owner: " + item.getOwner());
        if (item.getImages() != null && !item.getImages().isEmpty()) {
            try (InputStream is = new FileInputStream(System.getProperty("user.dir") + "\\" + item.getImages().getFirst())) {
                Image image = new Image(is);
                this.requestedItemImage.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the error, e.g., showMenu a placeholder image TODO
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

            // Otteniamo il controller del popup e gli passiamo i dati
            InventoryPopupController popupController = loader.getController();

            // Get user inventory
            MakeBarterController makeBarterController = new MakeBarterController();
            List<PreviewItemBean> myItems = makeBarterController.loadUserInventory();

            popupController.setParameters(this, popupStage, myItems);

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    @FXML
    private void handleSendProposal() throws Exception {
        // Button to submit the barter proposal
        System.out.println("Offer sent for: " + targetItem.getName());
        MakeBarterController makeBarterController = new MakeBarterController();
        makeBarterController.makeOffer(this.offeredItems, this.targetItem);
        MainGraphicController.getInstance().handleOfferSent();
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        MainGraphicController.getInstance().goBack();
    }

    @Override
    public void initData(PreviewItemBean data) {
        this.setTargetItem(data);
    }
}
