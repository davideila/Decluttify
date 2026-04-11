package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeBarterController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OfferListCellController{

    @FXML Label labelUserLeft;
    @FXML FlowPane flowPaneLeft;
    @FXML Label labelUserRight;
    @FXML FlowPane flowPaneRight;
    private OfferBean offerBean;

    public void setData(OfferBean offerBean, boolean isReceived) {
        flowPaneLeft.getChildren().clear();
        flowPaneRight.getChildren().clear();
        PreviewItemBean itemreq;
        this.offerBean = offerBean;
        List<PreviewItemBean> itemsoff;
        itemreq = offerBean.getRequestedItem();
        itemsoff = offerBean.getOfferedItemList();

        if (isReceived) {
            this.labelUserLeft.setText("FROM: " + offerBean.getOfferer());
            this.labelUserRight.setText("YOUR ITEM");

            for (PreviewItemBean item : itemsoff) {
                flowPaneLeft.getChildren().add(createItemTile(item));
            }
            flowPaneRight.getChildren().add(createItemTile(itemreq));
        }
        else{
            this.labelUserLeft.setText("YOU ASKED FOR");
            this.labelUserRight.setText("TO: " + offerBean.getReceiver());

            flowPaneLeft.getChildren().add(createItemTile(itemreq));
            for (PreviewItemBean item : itemsoff) {
                flowPaneRight.getChildren().add(createItemTile(item));
            }
        }
    }

    private VBox createItemTile(PreviewItemBean itemBean) {

        VBox tile = new VBox(5); //
        tile.setAlignment(Pos.CENTER);
        tile.setPrefWidth(100);
        tile.setStyle("-fx-background-color: #ffffff; -fx-padding: 5; -fx-border-color: #eeeeee; -fx-border-radius: 5;");

        // Label for item title
        Label nameLabel = new Label(itemBean.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 10px; -fx-text-fill: black;");
        nameLabel.setWrapText(false);

        // Only first image of the item
        ImageView imageView = new ImageView();
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        imageView.setPreserveRatio(true);
        // imageView.setImage(item.getImage()); // Se hai l'immagine nel bean

        // Condition Label
        Label condLabel = new Label(itemBean.getCondition());
        condLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #666666;");

        tile.getChildren().addAll(nameLabel, imageView, condLabel);
        return tile;
    }

    public void handleAccept(ActionEvent actionEvent) throws Exception {
        MakeBarterController mbc = new MakeBarterController();
        mbc.acceptOffer(this.offerBean);
        AlertProvider.showInfo("Success!", "Offer has been accepted");
        MainGraphicController.getInstance().showMyBartersView();
    }


    public void handleReject(ActionEvent actionEvent) {
        MakeBarterController mbc = new MakeBarterController();
        mbc.rejectOffer(this.offerBean);
    }
}
