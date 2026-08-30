package it.uniroma2.ispw.decluttify.view.controller.jfx;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeOfferController;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.view.controller.jfx.customtiles.itemtile.JFXItemTileInventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class JFXInventoryPopupController {
    @FXML private GridPane grid;
    private List<PreviewItemBean> inventoryItems = new ArrayList<>();
    private List<JFXItemTileInventory> itemTiles = new ArrayList<>();
    private boolean confirmed = false;
    private final int MAX_SELECTIONS = 3;

    // This method is to pass data to this object from caller
    public void init(MakeOfferController makeOfferController, UserBean user, List<PreviewItemBean> alreadySelectedItems) {
        try {
            if (inventoryItems.isEmpty()) {
                this.inventoryItems.addAll(makeOfferController.loadUserInventory(user));
            }
        }catch(DecluttifyException e) {
            AlertProvider.showError("Operation failed", e.getMessage());
        }catch (Exception e) {
            AlertProvider.showError("System failure", "An unexpected error occurred.");
            //e.printStackTrace();
        }
        populateGrid();
        for(PreviewItemBean item : alreadySelectedItems) {
            for(JFXItemTileInventory tile : itemTiles) {
                if (item.getId() == tile.getItem().getId()){
                    if(!isSelectionLimitReached()) {
                        tile.setSelected(true);
                    }
                }
                else{
                    tile.setSelected(false);
                }
            }
        }
        this.confirmed = false;
    }

    private void populateGrid() {
        this.grid.getChildren().clear();
        this.itemTiles.clear();

        if(this.inventoryItems.isEmpty()) {
            Label label = new Label("You currently have no items");
            Button button = new Button("POST ITEM");
            button.setStyle("-fx-background-color: #3498db;");
            button.setOnAction(event -> {
                AlertProvider.showComingSoon();
            });
            grid.add(label, 0, 0);
            grid.add(button, 0, 1);
            return;
        }

        for (int i = 0; i < inventoryItems.size(); i++) {
            PreviewItemBean item = inventoryItems.get(i);
            JFXItemTileInventory tile = new JFXItemTileInventory(item);
            tile.setOnMouseClicked(new javafx.event.EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    handleTileClick(tile);
                }
            });

            int col = i % 2; // column is 0 or 1
            int row = i / 2; // every 2 item there will be a new row

            this.itemTiles.add(tile);
            this.grid.add(tile, col, row);
        }
    }

    private void handleTileClick(JFXItemTileInventory clickedTile) {
        // if tile was already selected, deselect it
        if (clickedTile.isSelected()) {
            clickedTile.setSelected(false);
        }
        else {
            if (!isSelectionLimitReached()) {
                clickedTile.setSelected(true);
            } else {
                AlertProvider.showInfo("Items number limit reached", "You cannot select more than " + MAX_SELECTIONS + " items.");
            }
        }
    }

    private boolean isSelectionLimitReached(){
        int count = 0;
        for (JFXItemTileInventory tile : this.itemTiles) {
            if (tile.isSelected()) {
                count++;
            }
        }
        if (count < MAX_SELECTIONS) {
            return false;
        }
        else{
            return true;
        }
    }

    @FXML
    private void handleAddItems(ActionEvent event) {
        this.confirmed = true;
        closeStage(event);;
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        this.confirmed = false;
        closeStage(event);
    }

    public List<PreviewItemBean> getSelectedItems() {
        List<PreviewItemBean> selectedItems = new ArrayList<>();
        for (JFXItemTileInventory tile : this.itemTiles) {
            if (tile.isSelected()) {
                selectedItems.add(tile.getItem());
            }
        }
        return selectedItems;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void closeStage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}