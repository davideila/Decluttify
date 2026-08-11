package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.controller.logic.ManageOfferController;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.List;

public class JFXMyOffersController extends JFXGraphicController {

    private final ManageOfferController manageOfferController;

    @FXML private ListView<OfferBean> listViewReceived;
    @FXML private ListView<OfferBean> listViewSent;

    public JFXMyOffersController(Navigator navigator, SessionManager sm) {
        super(navigator, sm, ViewType.MY_OFFERS);
        this.manageOfferController = new ManageOfferController(sm);
    }

    public void init() {
        this.setInSidebar(true);
        List<OfferBean> received = List.of();
        this.listViewReceived.setCellFactory(lv -> new OfferListCell(true));
        this.listViewSent.setCellFactory(lv -> new OfferListCell(false));
        try {
            received = manageOfferController.loadReceivedOffers(this.sessionManager.getLoggedUser());
        }catch(Exception e){
            this.handleException(e);
        }
        List<OfferBean> sent = List.of();
        try {
            sent = manageOfferController.loadSentOffers(this.sessionManager.getLoggedUser());
        }catch(Exception e){
            this.handleException(e);
        }
        listViewReceived.getItems().addAll(received);
        listViewSent.getItems().addAll(sent);
    }

    // This class is for customizing a list cell in the list view as stated in oracle doc:
    // https://openjfx.io/javadoc/14/javafx.controls/javafx/scene/control/Cell.html#updateItem(T,boolean)
    public static class OfferListCell extends ListCell<OfferBean> {
        private boolean isReceived; // Needed to identify the tab of the offer view
        private JFXOfferListCellController controller;
        private Parent root;

        public OfferListCell(boolean isReceived) {
            this.isReceived = isReceived;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/offer_list_cell.fxml"));
                root = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
                AlertProvider.showError("System error", "Service not available. Please try again later");
            }
        }

        @Override
        protected void updateItem(OfferBean offerBean, boolean empty) {
            super.updateItem(offerBean, empty);
            if (empty || offerBean == null) {
                setGraphic(null);
                setText(null);
            } else {
                // Give data to the new controller of the specific listcell
                controller.setData(offerBean, isReceived);
                setGraphic(root);

            }
        }
    }
}
