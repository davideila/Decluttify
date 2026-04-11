package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class InventoryPopupController {
    @FXML
    private ListView<PreviewItemBean> inventoryListView;

    private MakeOfferController callerController;
    private Stage stage;

    // Metodo fondamentale per collegare i due controller
    public void setParameters(MakeOfferController caller, Stage stage, List<PreviewItemBean> myItems) {
        this.callerController = caller;
        this.stage = stage;
        //calling setCellFactory method for the customized list cell in the list view
        this.inventoryListView.setCellFactory(lv -> new InventoryListCell());
        this.inventoryListView.getItems().addAll(myItems);
    }

    @FXML
    private void handleSelectItem() {
        PreviewItemBean selected = inventoryListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Give the object to the caller controller
            callerController.addOfferedItem(selected);
            stage.close();
        }
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        // 1. Prendi l'oggetto selezionato (esempio)
        // PreviewItemBean selected = listView.getSelectionModel().getSelectedItem();

        // 2. Passalo al padre
        // if (selected != null) parentController.addOfferedItem(selected);

        // 3. Chiudi il popup
        handleClose(event);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // This class is for customizing a list cell in the list view as stated in oracle doc:
    // https://openjfx.io/javadoc/14/javafx.controls/javafx/scene/control/Cell.html#updateItem(T,boolean)
    public static class InventoryListCell extends ListCell<PreviewItemBean> {

        private final VBox vbox = new VBox();
        private final VBox vbox2 = new VBox();
        private final HBox hbox = new HBox();
        //private CheckBox checkBox = new CheckBox();
        private final Label nameLabel = new Label();
        private final Label conditionLabel = new Label();
        private final Label conditionHeaderLabel = new Label();
        private final Label descriptionLabel = new Label();
        private final ImageView itemImage = new ImageView();

        public InventoryListCell() {
            hbox.setSpacing(15);
            vbox.setSpacing(5);
            vbox2.setSpacing(5);
            vbox.getChildren().addAll(nameLabel, itemImage);
            vbox2.getChildren().addAll(conditionHeaderLabel, conditionLabel);
            hbox.getChildren().addAll(vbox, vbox2, descriptionLabel); //+ checkbox
            hbox.setAlignment(Pos.CENTER_LEFT);
            Region filler = new Region();
            HBox.setHgrow(filler, javafx.scene.layout.Priority.ALWAYS);
            vbox.setAlignment(Pos.CENTER);
            conditionHeaderLabel.setText("Condition");
            conditionHeaderLabel.setAlignment(Pos.CENTER);
            vbox2.setAlignment(Pos.CENTER);
            //checkBox.setMaxHeight(Double.MAX_VALUE);

        }

        @Override
        protected void updateItem(PreviewItemBean item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(hbox);
                nameLabel.setText(item.getName());
                Image image = null;
                //Get the item image form the uploads folder of the project
                try (InputStream is = new FileInputStream(System.getProperty("user.dir") + "\\" + item.getImages().getFirst())) {
                    image = new Image(is);
                    itemImage.setImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the error, e.g., showMenu a placeholder image
                }
                itemImage.setFitWidth(60);
                itemImage.setFitHeight(60);
                conditionLabel.setText(item.getCondition());
                descriptionLabel.setText(item.getDescription());
            }
        }
    }
}