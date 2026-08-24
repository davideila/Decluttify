package it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.itemTile;

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
    private final String ITEM_IMAGES_PATH_FROM_RESOURCES = "/it.uniroma2.ispw.decluttify/images" ;


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
        tileVbox.setStyle("-fx-border-color: #3498db; -fx-border-width: 2px; -fx-border-radius: 5px;");

        // Category icon on top of stack pane and tile on bottom
        this.categoryIcon = new ImageView();
        this.categoryIcon.setFitWidth(24.0);
        this.categoryIcon.setFitHeight(24.0);
        this.categoryIcon.setPreserveRatio(true);
        this.getChildren().addAll(tileVbox, this.categoryIcon);
        StackPane.setAlignment(this.categoryIcon, Pos.TOP_RIGHT);
        StackPane.setMargin(this.categoryIcon, new Insets(0.0, 8.0, 0.0, 8.0));


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
        topVbox.getChildren().add(this.itemImageView);
        topVbox.getChildren().add(this.nameLabel);

        // Next element after the topVbox
        this.conditionLabel = new Label();

        // Adding all elements to tile
        tileVbox.getChildren().addAll(
                topVbox,
                this.conditionLabel
        );
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
        switch (item.getCategory()) {
            case "Music":
                this.categoryIcon.setImage(MediaLoader.getInstance().loadItemImage("music_icon.png"));
                break;
            case "Tech":
                this.categoryIcon.setImage(MediaLoader.getInstance().loadItemImage("tech_icon.png"));
                break;
            case "Book":
                this.categoryIcon.setImage(MediaLoader.getInstance().loadItemImage("book_icon.png"));
                break;
            case "Clothing":
                this.categoryIcon.setImage(MediaLoader.getInstance().loadItemImage("cloth_icon.png"));
                break;
            case "Miscellaneous":
                this.categoryIcon.setImage(MediaLoader.getInstance().loadItemImage("misc_icon"));
                break;
            case null, default:
                this.categoryIcon.setImage(MediaLoader.getInstance().loadItemImage("category_icon.png"));
                break;
        }
    }

    public PreviewItemBean getItem() {
        return item;
    }

}
