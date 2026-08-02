package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeOfferController;
import it.uniroma2.ispw.decluttify.controller.logic.ManageOfferController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;

public class OfferListCellController extends GraphicController{

    private final ManageOfferController manageOfferController;
    @FXML Label labelUserLeft;
    @FXML FlowPane flowPaneLeft;
    @FXML Label labelUserRight;
    @FXML FlowPane flowPaneRight;
    private OfferBean offerBean;

    public OfferListCellController(Navigator navigator, SessionManager sm) {
        super(navigator, sm, null);
        this.manageOfferController = new ManageOfferController(sessionManager);
    }

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

    public void handleAccept(ActionEvent actionEvent) {
        try {
            manageOfferController.acceptOffer(this.offerBean);
        }catch(Exception e){
            this.handleException(e);
        }
        AlertProvider.showInfo("Success!", "Offer has been accepted");
        navigator.navigateTo(ViewType.MY_BARTERS);
    }


    public void handleReject(ActionEvent actionEvent) {
        try {
            manageOfferController.rejectOffer(this.offerBean);
        }catch(Exception e){
            this.handleException(e);
        }
        AlertProvider.showInfo("Success!", "Offer has been rejected");
    }
}
