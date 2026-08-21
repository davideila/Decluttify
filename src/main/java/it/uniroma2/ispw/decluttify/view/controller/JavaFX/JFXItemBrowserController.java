package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JFXItemBrowserController extends JFXGraphicController {

    private final VisualizeItemController visualizeItemController;
    private final List<PreviewItemBean> items = new ArrayList<>();
    @FXML private TilePane tilePane;

    public JFXItemBrowserController(Navigator navigator, SessionManager sm) {
        super(navigator, sm, JFXViewType.ITEM_BROWSER);
        this.visualizeItemController = new VisualizeItemController();
    }

    @Override
    public void init() {
        try {
            items.clear();
            items.addAll(this.visualizeItemController.loadAvailableItems());
            refreshTilePane();
        } catch (Exception e) {
            this.handleException(e);
        }
    }

    private void refreshTilePane(){
        if (tilePane != null) {
            tilePane.getChildren().clear();
            for (PreviewItemBean item : items) {
                tilePane.getChildren().add(createItemTile(item));
            }
        }
    }

    private Button createItemTile(PreviewItemBean item) {
        VBox tileContent = new VBox();
        tileContent.setAlignment(Pos.CENTER);
        tileContent.setSpacing(5);

        // Vbox content
        ImageView itemImage = createImageView(item);
        Label itemNameLabel = new Label(item.getName());
        Label itemConditionLabel = new Label("Condition: " + item.getCondition());
        Label itemDescriptionLabel = new Label(item.getDescription());
        Button itemOwnerButton = new Button(item.getOwner());

        //Item owner button click handler
        itemOwnerButton.setOnAction(event -> {
            event.consume(); // if owner button gets clicked, the outer button of the item tile doesn't have to get activated
            // navigator.navigateTo(JFXViewType.USER_DETAILS, item.getOwner());
            AlertProvider.showComingSoon();
        });

        // Adding content to vbox
        tileContent.getChildren().addAll(itemImage, itemNameLabel, itemDescriptionLabel, itemConditionLabel, itemOwnerButton);

        // Create Button and set Vbox
        Button itemButton = new Button();
        itemButton.setGraphic(tileContent);
        itemButton.setPrefSize(200, 150);

        // Item tile click handler
        itemButton.setOnAction(event -> {
            try {
                this.navigator.navigateTo(JFXViewType.ITEM_DETAILS, item);
            }catch(Exception e){
                this.handleException(e);
            }
        });

        return itemButton;
    }

    private ImageView createImageView(PreviewItemBean item) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(140);
        imageView.setFitHeight(140);

        String imagePath = System.getProperty("user.dir") + File.separator + item.getMainImagePath();

        try (InputStream is = new FileInputStream(imagePath)) {
            imageView.setImage(new Image(is));
        } catch (IOException e) {
            //If no image found, then use placeholder
            String placeholderPath = System.getProperty("user.dir") + File.separator + "placeholder_item.png";
            try (InputStream is = new FileInputStream(placeholderPath)) {
                imageView.setImage(new Image(is));
            } catch (IOException ex) {
                //if also placeholder image cannot be loaded, then do nothing and leave it empty
            }
        }
        return imageView;
    }

}
