package it.uniroma2.ispw.decluttify.view.controller.jfx.customtiles.itemtile;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.MediaLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public abstract class JFXItemTile extends StackPane {

    protected ImageView itemImageView;
    protected Label nameLabel;
    protected Label conditionLabel;
    protected ImageView categoryIcon;
    protected VBox tileVbox;
    protected PreviewItemBean item;
    protected String baseStyle = "-fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; ";


    protected JFXItemTile() {
        this.setCursor(javafx.scene.Cursor.HAND);
        initTile();
    }

    protected JFXItemTile(PreviewItemBean item) {
        this.setCursor(javafx.scene.Cursor.HAND);
        initTile();
        setItemData(item);
    }

    protected void initTile() {
        // Tile Vbox configuration
        this.tileVbox = new VBox();
        tileVbox.setAlignment(Pos.CENTER_LEFT);
        tileVbox.setSpacing(5.0);
        tileVbox.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        tileVbox.setStyle(baseStyle + "-fx-border-color: #ddd;");

        // Top VBox containing the ImageView and title
        VBox topVbox = new VBox();
        topVbox.setSpacing(5.0);
        topVbox.setAlignment(Pos.TOP_CENTER);
        this.itemImageView = new ImageView();
        this.itemImageView.setFitHeight(120.0);
        this.itemImageView.setFitWidth(120.0);
        this.itemImageView.setNodeOrientation(NodeOrientation.INHERIT);
        this.itemImageView.setPreserveRatio(true);
        this.nameLabel = new Label();
        this.nameLabel.setStyle("-fx-font-weight: bold;");
        topVbox.getChildren().add(this.itemImageView);
        topVbox.getChildren().add(this.nameLabel);

        // Next element after the topVbox
        this.conditionLabel = new Label();

        // Adding all elements to tile
        tileVbox.getChildren().addAll(
                topVbox,
                this.conditionLabel
        );

        // Category icon on top of item vbox tile in stack pane
        this.categoryIcon = new ImageView();
        this.categoryIcon.setFitWidth(30.0);
        this.categoryIcon.setFitHeight(30.0);
        this.categoryIcon.setPreserveRatio(true);
        StackPane.setAlignment(this.categoryIcon, Pos.TOP_RIGHT);
        StackPane.setMargin(this.categoryIcon, new Insets(5));

        this.getChildren().addAll(tileVbox, this.categoryIcon);
    }

    public void setItemData(PreviewItemBean item) {
        this.item = item;
        if (item == null) {
            return;
        }
        this.nameLabel.setText(item.getName());
        this.conditionLabel.setText("Conditions: " + item.getCondition());
        this.itemImageView.setImage(MediaLoader.getInstance().loadItemImage(item.getMainImageName()));
        loadCategoryIcon();
    }

    
    protected void loadCategoryIcon(){
        if (item == null || item.getCategory() == null) {
            this.categoryIcon.setImage(MediaLoader.MISC_ICON);
            return;
        }

        switch (item.getCategory()) {
            case "Music" -> this.categoryIcon.setImage(MediaLoader.MUSIC_ICON);
            case "Tech" -> this.categoryIcon.setImage(MediaLoader.TECH_ICON);
            case "Book" -> this.categoryIcon.setImage(MediaLoader.BOOK_ICON);
            case "Clothing" -> this.categoryIcon.setImage(MediaLoader.CLOTH_ICON);
            case "Sport" -> this.categoryIcon.setImage(MediaLoader.SPORT_ICON);
            default -> this.categoryIcon.setImage(MediaLoader.MISC_ICON);
        }

    }

    public PreviewItemBean getItem() {
        return item;
    }

}
