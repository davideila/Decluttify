package it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.itemTile;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import javafx.scene.control.Label;


public class JFXItemTileMyOffers extends JFXItemTile{

    private Label activeOffers;


    public JFXItemTileMyOffers(PreviewItemBean itemBean) {
        super(itemBean);

        this.activeOffers = new Label("Active offers: " + item.getNumOffers());
        this.tileVbox.getChildren().add(this.activeOffers);
    }

    public void setPopularity(){
        String popularity;
        if(this.getItem().getNumOffers() == 0){
            popularity = "No offers yet";
        }
        else if(this.getItem().getNumOffers() < 3){
            popularity = "In demand";
        }
        else if(this.getItem().getNumOffers() < 7){
            popularity = "Popular";
        }
        else {
            popularity = "Trending";
        }
        this.activeOffers.setText(popularity);
    }
}
