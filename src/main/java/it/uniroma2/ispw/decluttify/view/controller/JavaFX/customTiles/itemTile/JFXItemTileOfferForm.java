package it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.itemTile;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.MediaLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


public class JFXItemTileOfferForm extends JFXItemTile{

    private Label activeOffers;
    private ImageView deleteIcon;
    private StackPane placeholderOverlay;

    public JFXItemTileOfferForm() {
        super();
        PreviewItemBean template = new PreviewItemBean();
        template.setCondition("Good");
        template.setNumOffers(0);
        template.setMainImage("item_placeholder.png");
        template.setName("Item title");
        this.activeOffers = new Label();
        this.tileVbox.getChildren().add(activeOffers);
        this.setItemData(template);
        initDeleteIcon();
        initPlaceholderOverlay();
        setPlaceholder();
    }

    private void initDeleteIcon() {
        this.deleteIcon = new ImageView();
        this.deleteIcon.setFitHeight(24);
        this.deleteIcon.setFitWidth(24);
        this.deleteIcon.setPreserveRatio(true);
        StackPane.setAlignment(this.deleteIcon, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(this.deleteIcon, new Insets(0.0, 8.0, 8.0, 0.0));
        this.deleteIcon.setImage(MediaLoader.getInstance().loadUIImage("delete_icon.png"));
        this.deleteIcon.setOnMouseClicked(event -> {
            setPlaceholder();
            event.consume();
        });
        this.deleteIcon.setOnMouseEntered(event -> {
            this.deleteIcon.setOpacity(1.0);
            this.deleteIcon.setScaleX(1.15);
            this.deleteIcon.setScaleY(1.15);
        });

        this.deleteIcon.setOnMouseExited(event -> {
            this.deleteIcon.setOpacity(0.7);
            this.deleteIcon.setScaleX(1.0);
            this.deleteIcon.setScaleY(1.0);
        });
        this.getChildren().add(this.deleteIcon);
    }

    private void initPlaceholderOverlay() {
        this.placeholderOverlay = new StackPane();

        this.placeholderOverlay.setStyle(
                "-fx-background-color: -fx-background; " +
                        "-fx-border-style: dashed; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #3498db; " +
                        "-fx-border-width: 2;"
        );

        Label label = new Label("Add item");
        label.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        this.placeholderOverlay.getChildren().add(label);

        this.getChildren().add(this.placeholderOverlay);
    }

    public void setPlaceholder() {
        this.item = null;

        this.placeholderOverlay.setVisible(true);
        this.placeholderOverlay.setDisable(false);

        this.tileVbox.setVisible(false);
        this.categoryIcon.setVisible(false);
        this.deleteIcon.setVisible(false);

        this.setVisible(true);
        this.setDisable(false);
    }

    public void setItem(PreviewItemBean item) {
        this.setItemData(item);
        this.activeOffers.setText("Active offers: " + item.getNumOffers());

        this.placeholderOverlay.setVisible(false);
        this.placeholderOverlay.setDisable(true);

        this.tileVbox.setVisible(true);
        this.categoryIcon.setVisible(true);
        this.deleteIcon.setVisible(true);
    }

    public ImageView getDeleteIcon() {
        return deleteIcon;
    }

}
