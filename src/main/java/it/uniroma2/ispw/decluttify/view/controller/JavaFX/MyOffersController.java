package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeBarterController;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyOffersController extends GraphicController implements Initializable {

    @FXML private ListView<OfferBean> listViewReceived;
    @FXML private ListView<OfferBean> listViewSent;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setInSidebar(true);
        MakeBarterController mbc = new MakeBarterController();
        List<OfferBean> received;
        this.listViewReceived.setCellFactory(lv -> new OfferListCell(true));
        this.listViewSent.setCellFactory(lv -> new OfferListCell(false));
        try {
            received = mbc.loadReceivedOffers(SessionManager.getInstance().getLoggedUser().getUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<OfferBean> sent;
        try {
            sent = mbc.loadSentOffers(SessionManager.getInstance().getLoggedUser().getUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        listViewReceived.getItems().addAll(received);
        listViewSent.getItems().addAll(sent);
    }

    // This class is for customizing a list cell in the list view as stated in oracle doc:
    // https://openjfx.io/javadoc/14/javafx.controls/javafx/scene/control/Cell.html#updateItem(T,boolean)
    public static class OfferListCell extends ListCell<OfferBean> {
        private boolean isReceived; // Needed to identify the tab of the offer view
        private OfferListCellController controller;
        private Parent root;

        public OfferListCell(boolean isReceived) {
            this.isReceived = isReceived;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/offer_list_cell.fxml"));
                root = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
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
