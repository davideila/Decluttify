package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.BarterBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeBarterController;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyBartersController extends GraphicController implements Initializable {

    @FXML ListView<BarterBean> barterList;
    private List<BarterBean> barters;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barterList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        barterList.setCellFactory(lv -> new BarterListCell());

        MakeBarterController mbc = new MakeBarterController();
        try {
            this.barters = mbc.loadUserBarters();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.refreshView();
    }

    private void refreshView(){
        this.barterList.getItems().clear();
        this.barterList.getItems().addAll(barters);
    }

    public static class BarterListCell extends ListCell<BarterBean> {
        @FXML Button btnDispute;
        @FXML Button btnDetails;
        @FXML Button btnConfirm;
        @FXML HBox youGiveBox;
        @FXML HBox youGetBox;
        @FXML Label partnerLabel;
        private Parent root;
        private BarterBean barterBean;

        public BarterListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/barter_list_cell.fxml"));
                loader.setController(this);
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                AlertProvider.showError("System error", "Service not available. Please try again later");
            }
        }

        @Override
        protected void updateItem(BarterBean barterBean, boolean empty) {
            super.updateItem(barterBean, empty);
            this.barterBean = barterBean;
            if (empty || barterBean == null) {
                setGraphic(null);
                setText(null);
            } else {
                // Give data to the new controller of the specific listcell
                setGraphic(root);
                this.youGiveBox.getChildren().clear();
                this.youGetBox.getChildren().clear();
                this.btnConfirm.setOnAction(this::handleConfirmBarter);
                this.btnDispute.setOnAction(this::handleOpenDispute);
                this.btnDetails.setOnAction(this::handleShowDetails);
                this.partnerLabel.setText("Partner: " + barterBean.getPartnerName());
                for(String itemName: barterBean.getMyItems()){
                    VBox vbox = new VBox();
                    this.youGiveBox.getChildren().add(vbox);
                    vbox.getChildren().add(new Label(itemName));
                }
                for(String itemName: barterBean.getPartnerItems()){
                    VBox vbox = new VBox();
                    this.youGetBox.getChildren().add(vbox);
                    vbox.getChildren().add(new Label(itemName));
                }
                if(barterBean.isYouConfirmed()){
                    this.btnConfirm.setDisable(true);
                    this.btnConfirm.setText("Confirmed");
                    this.btnDispute.setVisible(false);
                }
                else {
                    this.btnConfirm.setDisable(false);
                }
                if(barterBean.getStatus().equals("COMPLETED")){
                    this.btnConfirm.setDisable(true);
                    this.btnConfirm.setText("Completed!");
                    this.btnDispute.setVisible(false);
                }
                else {
                    this.btnConfirm.setDisable(false);
                    this.btnConfirm.setText("Confirm");
                }
            }
        }

        public void handleShowDetails(ActionEvent actionEvent) {
            //TODO
        }

        public void handleConfirmBarter(ActionEvent actionEvent) {
            MakeBarterController makeBarterController = new MakeBarterController();
            try {
                makeBarterController.confirmBarter(barterBean);
            }catch(Exception e){
                if (e instanceof DAOException) {
                    AlertProvider.showError("System error", "Service not available. Please try again later.");
                } else if (e instanceof ModelException) {
                    AlertProvider.showError("Invalid request", e.getMessage());
                } else {
                    AlertProvider.showError("Unexpected error", e.getMessage());
                }
                e.printStackTrace();
            }

        }

        public void handleOpenDispute(ActionEvent actionEvent) {
            //TODO
        }

    }
}
