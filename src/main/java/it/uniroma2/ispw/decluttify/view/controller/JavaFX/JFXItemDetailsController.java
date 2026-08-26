package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.MediaLoader;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class JFXItemDetailsController extends JFXGraphicController implements SessionObserver {

    private FullItemBean visualizedItem;
    private final VisualizeItemController visualizeItemController;
    @FXML Button barterButton;
    @FXML Circle dot1;
    @FXML Circle dot2;
    @FXML Circle dot3;
    @FXML Button ownerButton;
    @FXML Label itemLocationLabel;
    @FXML Label popularityLabel;
    @FXML Label ownerRatingLabel;
    @FXML Label itemCreationDateLabel;
    @FXML Label itemNameLabel;
    @FXML Label itemDescriptionLabel;
    @FXML Label itemConditionLabel;
    @FXML ImageView itemImageView;


    public JFXItemDetailsController(Navigator navigator, SessionManager sm, PreviewItemBean selectedItem) {
        super(navigator, sm, JFXViewType.ITEM_DETAILS);
        this.sessionManager = sm;
        this.sessionManager.attach(this);
        this.visualizedItem = new FullItemBean(selectedItem);
        this.visualizeItemController = new VisualizeItemController();
    }

    @Override
    public void init(){
        // Show the information based on the initial bean with the partial information passed and starts an asynchronous call to get the full item information from persistence
        this.showItemDetails();
        this.loadItemDetailsAsync();
        this.update();
    }

    private void showItemDetails() {
        // Data always ready (PreviewItemBean)
        this.itemNameLabel.setText(this.visualizedItem.getName());
        this.itemDescriptionLabel.setText(this.visualizedItem.getDescription());
        this.itemConditionLabel.setText(this.visualizedItem.getCondition());
        this.ownerButton.setText(this.visualizedItem.getOwner());
        this.itemImageView.setImage(MediaLoader.getInstance().loadItemImage(this.visualizedItem.getMainImageName()));

        // Data completed after asynchronous call (FullItemBean)
        if (this.visualizedItem instanceof FullItemBean fullItem) {
            this.itemCreationDateLabel.setText(fullItem.getCreationDate());
            this.itemLocationLabel.setText(fullItem.getLocation());
            String popularity;
            if(fullItem.getNumOffers() == 0){
                popularity = "No offers yet";
            }
            else if(fullItem.getNumOffers() < 3){
                popularity = "In demand";
            }
            else if(fullItem.getNumOffers() < 7){
                popularity = "Popular";
            }
            else {
                popularity = "Trending";
            }
            this.popularityLabel.setText(popularity);
            this.setupPaginationDots(fullItem.getImages().size());
        }
    }

    private void loadItemDetailsAsync(){
        // using Task for background processing in JAVAFX as best practice (https://docs.oracle.com/javafx/2/best_practices/jfxpub-best_practices.htm)
        // The item details gets initially filled with the previewBean, meanwhile the background task fetches the full item data
        Task<FullItemBean> getFullItemTask = new Task<>() {
            @Override
            protected FullItemBean call() {
                FullItemBean fib;
                fib = visualizeItemController.loadItemDetails(visualizedItem);
                return fib;
            }
        };

        // Refresh the UI with complete data if the call is successfull
        getFullItemTask.setOnSucceeded(event -> {
            this.visualizedItem = getFullItemTask.getValue();
            this.showItemDetails();
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
    public void update() {
        if (sessionManager.getLoggedUser() != null && this.visualizedItem.getOwner() != null) {
            if (this.visualizedItem.getOwner().equals(sessionManager.getLoggedUser().getUsername())) {
                this.barterButton.setText("EDIT ITEM");
                this.barterButton.setOnAction(event -> {
                    try {
                        handleEditItem(this.visualizedItem);
                    }catch(Exception e){
                        this.handleException(e);
                    }
                });
            } else {
                this.barterButton.setText("MAKE OFFER");
                this.barterButton.setOnAction(event -> {
                    try {
                        handleMakeOffer(event);
                    }catch(Exception e){
                        this.handleException(e);
                    }
                });
            }
        };
    }

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

    // On action events FXML buttons
    public void handleMakeOffer(ActionEvent actionEvent){
        navigator.navigateTo(JFXViewType.OFFER_FORM, visualizedItem);
    }

    public void handleEditItem(PreviewItemBean item) {AlertProvider.showComingSoon();}
    public void handleOwnerClick(ActionEvent event) { AlertProvider.showComingSoon(); }
    public void handleChatClick(ActionEvent event) { AlertProvider.showComingSoon(); }

    public void handleDot1(MouseEvent event) {
        this.itemImageView.setImage(MediaLoader.getInstance().loadItemImage(this.visualizedItem.getMainImageName()));
    }

    public void handleDot2(MouseEvent event) {
        this.itemImageView.setImage(MediaLoader.getInstance().loadItemImage(((FullItemBean)this.visualizedItem).getImages().get(1)));
    }

    public void handleDot3(MouseEvent event) {
        this.itemImageView.setImage(MediaLoader.getInstance().loadItemImage(((FullItemBean)this.visualizedItem).getImages().get(2)));
    }
}
