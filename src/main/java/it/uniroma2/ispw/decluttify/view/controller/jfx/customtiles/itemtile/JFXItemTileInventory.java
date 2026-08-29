package it.uniroma2.ispw.decluttify.view.controller.jfx.customtiles.itemtile;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class JFXItemTileInventory extends JFXItemTile{

    private Label activeOffers;
    private CheckBox selectCheckBox;

    public JFXItemTileInventory(PreviewItemBean itemBean) {
        super(itemBean);
        this.selectCheckBox = new CheckBox();
        this.selectCheckBox.setStyle("-fx-scale-x: 1.5; -fx-scale-y: 1.5;");

        // Make the checkbox not clickable, the vbox (all the item tile) will handle mouse click
        this.selectCheckBox.setMouseTransparent(true);
        this.getChildren().add(selectCheckBox);
        StackPane.setAlignment(this.selectCheckBox, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(this.selectCheckBox, new Insets(0.0, 8.0, 8.0, 0.0));
        this.activeOffers = new Label("Active offers: " + item.getNumOffers());
        this.tileVbox.getChildren().add(this.activeOffers);
    }

    public boolean isSelected() {
        return this.selectCheckBox.isSelected();
    }

    public void setSelected(boolean selected) {
        this.selectCheckBox.setSelected(selected);
        updateTileSelectionStyle(selected);
    }

    private void updateTileSelectionStyle(boolean selected) {
        if (selected) {
            this.setStyle("-fx-border-color: #3498db; -fx-border-width: 3px; -fx-border-radius: 5px;");
        } else {
            this.setStyle("-fx-border-color: #3498db; -fx-border-width: 2px; -fx-border-radius: 5px;");
        }
    }
}
