package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.exception.NotLoggedInException;
import it.uniroma2.ispw.decluttify.patterns.Observer.Observer;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ItemDetailsController extends GraphicController implements Initializable, DataReceiver<PreviewItemBean>, Observer {

    @FXML Button barterButton;
    @FXML Circle dot1;
    @FXML Circle dot2;
    @FXML Circle dot3;
    @FXML Button ownerButton;
    @FXML Label itemLocationLabel;
    @FXML Label itemNumOfferLabel;
    @FXML Label ownerRatingLabel;
    @FXML Label itemCreationDateLabel;
    @FXML Label itemNameLabel;
    @FXML Label itemDescriptionLabel;
    @FXML Label itemConditionLabel;
    @FXML ImageView mainImageView;
    private FullItemBean fullItemBean;


    public void handleMakeOffer(ActionEvent actionEvent) throws IOException {
        try {
            MainGraphicController.getInstance().makeOfferView(this.fullItemBean);

        } catch (NotLoggedInException e) {
            MainGraphicController.getInstance().triggerLogin();
        }
    }

    private void handleEditItem(PreviewItemBean item) throws IOException {
        System.out.println("Edit item button clicked");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.isInSidebar = false;
        SessionManager.getInstance().attach(this);
    }

    public void loadItemDetails(PreviewItemBean pib){
        this.itemNameLabel.setText(pib.getName());
        this.itemDescriptionLabel.setText(pib.getDescription());
        this.itemConditionLabel.setText(pib.getCondition());
        this.ownerButton.setText(pib.getOwner());
        try (InputStream is = new FileInputStream(System.getProperty("user.dir") + "\\" + pib.getImages().getFirst())) {
            Image image = new Image(is);
            this.mainImageView.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., showMenu a placeholder image TODO
        }

        // using Task for background processing in JAVAFX as best practice (https://docs.oracle.com/javafx/2/best_practices/jfxpub-best_practices.htm)
        // The item details gets initially filled with the previewBean, meanwhile the background task fetches the full item data
        // The following shows the definition of the task
        Task<FullItemBean> getFullItemTask = new Task<>() {
            @Override
            protected FullItemBean call() {
                FullItemBean fib;
                VisualizeItemController vic = new VisualizeItemController();
                fib = vic.loadItemDetails(pib.getId());
                return fib;
            }
        };

        // Complete the UI if the call is successfull
        getFullItemTask.setOnSucceeded(event -> {
            FullItemBean fullData = getFullItemTask.getValue();
            this.itemCreationDateLabel.setText(fullData.getCreationDate());
            this.itemLocationLabel.setText(fullData.getLocation());
            this.itemNumOfferLabel.setText(String.valueOf(fullData.getNumOffers()));
            //TODO this.ownerRatingLabel.setText(this.formatRating(fullData.getRating()));
            this.setupPaginationDots(fullData.getImages().size());
            this.fullItemBean = fullData;
        });

        // Exception handling if fails
        getFullItemTask.setOnFailed(event -> {
            Throwable e = getFullItemTask.getException();
            e.printStackTrace();
            if (e instanceof DAOException) {
                AlertProvider.showError("System error", "Service not available. Please try again later.");
            } else if (e instanceof ModelException) {
                AlertProvider.showError("Invalid request", e.getMessage());
            } else {
                AlertProvider.showError("Unexpected error", e.getMessage());
            }
        });

        // Start the Background Task
        Thread t = new Thread(getFullItemTask);
        t.setDaemon(true); //as stated in https://docs.oracle.com/javase/8/javafx/api/javafx/concurrent/Task.html, this is to terminate the threads when all stages are closed
        t.start();
    }

    @Override
    public void initData(PreviewItemBean data) {
        // Shows the information based on the bean passed and starts an asynchronous call to get the full item information from persistence
        this.loadItemDetails(data);
        this.refresh(data);
    }

    public void refresh(PreviewItemBean data) {
        if (SessionManager.getInstance().getLoggedUser() != null && data.getOwner() != null) {
            if (data.getOwner().equals(SessionManager.getInstance().getLoggedUser().getUsername())) {
                this.barterButton.setText("EDIT ITEM");
                this.barterButton.setOnAction(event -> {
                    try {
                        handleEditItem(data);
                    }catch(Exception e){
                        this.handleException(e);
                    }
                });
            } else {
                this.barterButton.setText("PROPOSE BARTER");
                this.barterButton.setOnAction(event -> {
                    try {
                        handleMakeOffer(event);
                    }catch(Exception e){
                        this.handleException(e);
                    }
                });
            }
        }
    }

    public void handleOwnerClick(ActionEvent actionEvent) {
    }

    public void handleChatClick(ActionEvent actionEvent) {
    }

    public void handleDot1(MouseEvent mouseEvent) {
    }

    public void handleDot2(MouseEvent mouseEvent) {
    }

    public void handleDot3(MouseEvent mouseEvent) {

    }

    // Private methods the controller needs to adjust the UI

    private String formatRating(double rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (int) rating; i++) {
            sb.append("★");
        }

        if (rating % 1 >= 0.5 && sb.length() < 5) {
            sb.append("⯪");
        }

        while (sb.length() < 5) {
            sb.append("☆");
        }

        return sb.toString();
    }

    private void setupPaginationDots(int numImages) {

        dot1.setVisible(false);
        dot1.setManaged(false); // managed(false) = no space used by dot on layout
        dot2.setVisible(false);
        dot2.setManaged(false);
        dot3.setVisible(false);
        dot3.setManaged(false);

        // Show only the dots based on te number of images
        if (numImages >= 1) {
            dot1.setVisible(true);
            dot1.setManaged(true);
        }
        if (numImages >= 2) {
            dot2.setVisible(true);
            dot2.setManaged(true);
        }
        if (numImages >= 3) {
            dot3.setVisible(true);
            dot3.setManaged(true);
        }
    }

    @Override
    public void update() {
        this.refresh(this.fullItemBean);
    }
}
