package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeOfferController;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.offerTile.JFXOfferTile;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.List;

public class JFXMyOffersController extends JFXGraphicController {

    private final VisualizeOfferController visualizeOfferController;

    @FXML VBox vboxReceivedOffers;
    @FXML VBox vboxSentOffers;

    public JFXMyOffersController(Navigator navigator, SessionManager sm) {
        super(navigator, sm, JFXViewType.MY_OFFERS);
        this.visualizeOfferController = new VisualizeOfferController(sm);
    }

    public void init() {
        List<OfferBean> receivedOffers = List.of();
        try {
            receivedOffers = visualizeOfferController.loadReceivedOffers(this.sessionManager.getLoggedUser());
        }catch(DecluttifyException e){
            this.handleErrorAlert("Application error", e);
        }catch (Exception e) {
            handleUnexpectedErrorAlert();
            e.printStackTrace();
        }
        List<OfferBean> sentOffers = List.of();
        try {
            sentOffers = visualizeOfferController.loadSentOffers(this.sessionManager.getLoggedUser());
        }catch(DecluttifyException e){
            this.handleErrorAlert("Application error", e);
        }catch (Exception e) {
            handleUnexpectedErrorAlert();
            e.printStackTrace();
        }

        this.vboxReceivedOffers.getChildren().clear();
        this.vboxSentOffers.getChildren().clear();
        if (receivedOffers.isEmpty()) {
            this.vboxReceivedOffers.getChildren().add(new Label("You currently have no pending offers received"));
        }
        else {
            for (OfferBean offer : receivedOffers) {
                JFXOfferTile offerTile = new JFXOfferTile(offer);
                offerTile.init(true);
                this.vboxReceivedOffers.getChildren().add(offerTile);
                Button acceptButton = offerTile.getAcceptButton();
                Button rejectButton = offerTile.getRejectButton();
                acceptButton.setOnAction(event -> {
                    handleAcceptOffer(offer);
                });
                rejectButton.setOnAction(event -> {
                    handleRejectOffer(offer);
                });
            }
        }

        if(sentOffers.isEmpty()) {
            this.vboxSentOffers.getChildren().add(new Label("You currently have no pending offers sent"));
        }
        else{
            for (OfferBean offer : sentOffers) {
                JFXOfferTile offerTile = new JFXOfferTile(offer);
                offerTile.init(false);
                this.vboxSentOffers.getChildren().add(offerTile);
                Button withdrawButton = offerTile.getCancelButton();
                withdrawButton.setOnAction(event -> {
                    AlertProvider.showComingSoon();
                });
            }
        }

    }

    private void handleAcceptOffer(OfferBean offer) {
        AlertProvider.showComingSoon();
    }

    private void handleRejectOffer(OfferBean offer){
        AlertProvider.showComingSoon();
    }

}
