package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ItemBrowserController extends GraphicController implements Initializable {

    @FXML
    private TilePane tilePane;

    private ObservableList<PreviewItemBean> items = FXCollections.observableArrayList();


    public Button initializeItemTile(PreviewItemBean item) {
        VBox tileContent = new VBox();
        tileContent.setAlignment(Pos.CENTER);
        tileContent.setSpacing(5);

        // Vbox content
        ImageView itemImage = new ImageView();
        Image image = null;
        //Get the item image form the uploads folder of the project
        try (InputStream is = new FileInputStream(System.getProperty("user.dir") + "\\" + item.getImages().getFirst())) {
            image = new Image(is);
            itemImage.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show a placeholder image
        }
        itemImage.setFitWidth(140);
        itemImage.setFitHeight(140);
        Label itemNameLabel = new Label(item.getName());
        Label itemConditionLabel = new Label("Condition: " + item.getCondition());
        Label itemDescriptionLabel = new Label(item.getDescription());
        Button itemOwnerButton = new Button(item.getOwner());
        itemOwnerButton.setUserData(item);

        // Adding content to vbox
        tileContent.getChildren().addAll(itemImage, itemNameLabel, itemDescriptionLabel, itemConditionLabel, itemOwnerButton);

        // Create Button and set Vbox
        Button itemButton = new Button();
        itemButton.setGraphic(tileContent);
        itemButton.setPrefSize(200, 150);

        // Set PreviewItemBean data in the button object
        itemButton.setUserData(item);

        // Item tile click handler
        itemButton.setOnAction(event -> {
            PreviewItemBean selectedItem = (PreviewItemBean) itemButton.getUserData();
            try {
                MainGraphicController.getInstance().showItemDetailsView(selectedItem);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //Item owner click handler
        itemOwnerButton.setOnAction(event -> {
            PreviewItemBean selectedUser = (PreviewItemBean) itemOwnerButton.getUserData();
            try {
                MainGraphicController.getInstance().showUserDetailsView(selectedUser.getOwner());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.consume();
        });

        return itemButton;
    }

    public void refreshTilePane(){
        for (PreviewItemBean ib : items) {
            tilePane.getChildren().add(initializeItemTile(ib));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.isInSidebar = true;
        VisualizeItemController vic = new VisualizeItemController();
        items.addAll(vic.loadAvailableItems());
        refreshTilePane();
    }

}
