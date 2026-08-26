package it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.offerTile;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.MediaLoader;
import it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.itemTile.JFXItemTileMyOffers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;


public class JFXOfferTile extends HBox {

    private JFXItemTileMyOffers requestedItemTile;
    private List<JFXItemTileMyOffers> offeredItemTiles;
    private boolean escrow = false;
    private boolean shipping = false;
    private Button acceptButton;
    private Button rejectButton;
    private Button cancelButton;

    public JFXOfferTile(OfferBean offer) {
        this.offeredItemTiles = new ArrayList<>();
        for (PreviewItemBean item : offer.getOfferedItemList()) {
            this.offeredItemTiles.add(new JFXItemTileMyOffers(item));
        }
        this.requestedItemTile = new JFXItemTileMyOffers(offer.getRequestedItem());
        this.escrow = offer.isEscrow();
        this.shipping = offer.isShipping();
    }

    public void init(boolean isReceivedTab){
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setStyle(
                "-fx-border-color: #ddd; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        VBox youGetVbox = new VBox();
        youGetVbox.setFillWidth(true);
        youGetVbox.setSpacing(5);
        HBox youGetHbox = new HBox();
        youGetHbox.setSpacing(20);
        youGetHbox.setAlignment(Pos.CENTER);

        Label partnerLabel = new Label();

        VBox optionsVbox = new VBox();
        optionsVbox.setFillWidth(true);
        optionsVbox.setSpacing(20);
        optionsVbox.setAlignment(Pos.CENTER);

        ImageView shippingIcon = new ImageView();
        if(shipping) {
            shippingIcon.setImage(MediaLoader.SHIPPING_ICON);
            shippingIcon.setFitHeight(80);
            shippingIcon.setFitWidth(80);
            optionsVbox.getChildren().add(shippingIcon);
        }

        ImageView escrowIcon = new ImageView();
        if(escrow) {
            escrowIcon.setImage(MediaLoader.ESCROW_ICON);
            escrowIcon.setFitHeight(80);
            escrowIcon.setFitWidth(80);
            optionsVbox.getChildren().add(escrowIcon);
        }

        if(!shipping && !escrow) {
            Label noOptionsLabel = new Label("No options set");
            noOptionsLabel.setAlignment(Pos.CENTER);
            noOptionsLabel.setStyle("-fx-text-fill: gray;");
            noOptionsLabel.setStyle("-fx-font-size: 12px;");
            optionsVbox.getChildren().add(noOptionsLabel);
        }

        VBox youGiveVbox = new VBox();
        youGiveVbox.setFillWidth(true);
        youGiveVbox.setSpacing(5);
        HBox youGiveHbox = new HBox();
        youGiveHbox.setSpacing(20);
        youGiveHbox.setAlignment(Pos.CENTER);

        VBox actionButtonsVbox = new VBox();
        actionButtonsVbox.setFillWidth(true);
        actionButtonsVbox.setSpacing(20);
        actionButtonsVbox.setAlignment(Pos.BOTTOM_RIGHT);
        actionButtonsVbox.setPrefWidth(80);

        Region region0 = new Region();
        HBox.setHgrow(region0, Priority.ALWAYS);
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        Region region3 = new Region();
        HBox.setHgrow(region3, Priority.ALWAYS);

        if (isReceivedTab) {
            partnerLabel.setText("Partner: " + offeredItemTiles.getFirst().getItem().getOwner());
            youGetHbox.getChildren().addAll(offeredItemTiles);
            youGetVbox.getChildren().addAll(partnerLabel, youGetHbox);

            Label invisibleLabel = new Label("Invisible");
            invisibleLabel.setVisible(false);
            youGiveVbox.getChildren().addAll(invisibleLabel, requestedItemTile);
            youGiveVbox.getChildren().addAll(youGiveHbox);

            for(JFXItemTileMyOffers tile: offeredItemTiles){
                tile.setPopularity();
            }

            this.acceptButton = new Button("Accept");
            this.acceptButton.setStyle(
                    "-fx-background-color: #2ecc71; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 5px;" +
                            "-fx-cursor: hand;"
            );
            this.rejectButton = new Button("Reject");
            this.rejectButton.setStyle(
                    "-fx-background-color: #e74c3c; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 5px;" +
                            "-fx-cursor: hand;"
            );
            this.acceptButton.setMaxWidth(Double.MAX_VALUE);
            this.rejectButton.setMaxWidth(Double.MAX_VALUE);
            actionButtonsVbox.getChildren().addAll(rejectButton, acceptButton);

            this.getChildren().add(region0);
            this.getChildren().add(youGetVbox);
            this.getChildren().add(region1);
            this.getChildren().add(optionsVbox);
            this.getChildren().add(region2);
            this.getChildren().add(youGiveVbox);
            this.getChildren().add(region3);
            this.getChildren().add(actionButtonsVbox);
        }
        else{
            youGetHbox.getChildren().addAll(requestedItemTile);
            youGetVbox.getChildren().addAll(partnerLabel, youGetHbox);

            partnerLabel.setText("Partner: " + requestedItemTile.getItem().getOwner());
            Label invisibleLabel = new Label("Invisible");
            invisibleLabel.setVisible(false);
            youGiveHbox.getChildren().addAll(offeredItemTiles);
            youGiveVbox.getChildren().addAll(invisibleLabel, youGiveHbox);

            requestedItemTile.setPopularity();

            this.cancelButton = new Button("Cancel");
            this.cancelButton.setStyle(
                    "-fx-background-color: #3498db; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 5px;" +
                            "-fx-cursor: hand;"
            );
            this.cancelButton.setMaxWidth(Double.MAX_VALUE);
            actionButtonsVbox.getChildren().addAll(cancelButton);

            this.getChildren().add(region0);
            this.getChildren().add(youGiveVbox);
            this.getChildren().add(region1);
            this.getChildren().add(optionsVbox);
            this.getChildren().add(region2);
            this.getChildren().add(youGetVbox);
            this.getChildren().add(region3);
            this.getChildren().add(actionButtonsVbox);
        }
    }

    public Button getAcceptButton(){
        return this.acceptButton;
    }

    public Button getRejectButton(){
        return this.rejectButton;
    }

    public Button getCancelButton(){
        return this.cancelButton;
    }

}
