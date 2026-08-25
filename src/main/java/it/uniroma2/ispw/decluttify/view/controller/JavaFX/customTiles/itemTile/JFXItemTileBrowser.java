package it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.itemTile;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import javafx.scene.control.Label;


public class JFXItemTileBrowser extends JFXItemTile{

    private Label popularityLabel;
    private String borderColor = "-fx-border-color: #ddd; ";

    public JFXItemTileBrowser(PreviewItemBean itemBean) {
        super(itemBean);
        this.setPopularityLabel();
        this.itemImageView.setFitHeight(140.0);
        this.itemImageView.setFitWidth(140.0);
        this.tileVbox.setStyle(baseStyle + borderColor);
        tileVbox.setOnMouseEntered(e -> {
            this.borderColor = "-fx-border-color: #3498db;";
            tileVbox.setStyle(baseStyle + borderColor);
        });

        tileVbox.setOnMouseExited(e -> {
            this.borderColor = "-fx-border-color: #ddd; ";
            tileVbox.setStyle(baseStyle + borderColor);
        });
    }

    private void setPopularityLabel(){
        this.popularityLabel = new Label();
        if (this.item == null) {
            this.popularityLabel.setText("");
            this.popularityLabel.setVisible(false);
            this.baseStyle = baseStyle + "-fx-background-color: #f4f5f7;";
            this.tileVbox.setStyle(baseStyle);
            return;
        }
        else{
            int activeOffers = this.item.getNumOffers();
            if(activeOffers <= 2){
                this.popularityLabel.setText("");
                this.popularityLabel.setVisible(false);
                this.baseStyle = baseStyle + "-fx-background-color: #f4f5f7;";
                this.tileVbox.setStyle(baseStyle);
            }
            else if(activeOffers <= 6){
                this.popularityLabel.setText("Popular");
                this.baseStyle = baseStyle + "-fx-background-color: #fff8e1;";
                this.tileVbox.setStyle(baseStyle);
            }
            else if(activeOffers > 6){
                this.popularityLabel.setText("Trending");
                this.baseStyle = baseStyle + "-fx-background-color: #ffe0b2;";
                this.tileVbox.setStyle(baseStyle);
            }
        }
        this.tileVbox.getChildren().add(popularityLabel);
    }
}
