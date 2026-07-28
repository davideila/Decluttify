package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.BarterBean;
import it.uniroma2.ispw.decluttify.controller.logic.MakeBarterController;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

public class MyBartersController extends GraphicController{

    private final MakeBarterController makeBarterController;
    private List<BarterBean> barters;
    @FXML ListView<BarterBean> barterList;

    public MyBartersController(Navigator navigator, SessionManager sm) {
        super(navigator, sm, ViewType.MY_BARTERS);
        this.makeBarterController = new MakeBarterController(sessionManager);
    }

    @Override
    public void init() {
        barterList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        barterList.setCellFactory(lv -> new BarterListCell(sessionManager));
        try {
            this.barters = this.makeBarterController.loadUserBarters(sessionManager.getLoggedUser());
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
        private final MakeBarterController makeBarterController;
        private final SessionManager sessionManager;

        public BarterListCell(SessionManager sm) {
            this.makeBarterController = new MakeBarterController(sm);
            this.sessionManager = sm;
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
            AlertProvider.showInfo("Feature coming soon", "This feature is not yet available on this version");
        }

        public void handleConfirmBarter(ActionEvent actionEvent) {
            try {
                this.makeBarterController.confirmBarter(barterBean, sessionManager.getLoggedUser());
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
            AlertProvider.showInfo("Feature coming soon", "This feature is not yet available on this version");
        }

    }
}
