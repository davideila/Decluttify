package it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.itemTile;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import javafx.scene.control.Label;


public class JFXItemTileBrowser extends JFXItemTile{

    private Label offersCountLabel;

    public JFXItemTileBrowser() {
        super();
        this.offersCountLabel = new Label();
        this.getChildren().add(this.offersCountLabel);
    }

    public JFXItemTileBrowser(PreviewItemBean itemBean) {
        super(itemBean);
        this.offersCountLabel = new Label("Active offers: " + itemBean.getNumOffers());
        this.getChildren().add(this.offersCountLabel);
    }
}
