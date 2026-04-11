package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.exception.NotLoggedInException;
import it.uniroma2.ispw.decluttify.patterns.Observer.Observer;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;


public class MainGraphicController implements Initializable, Observer {

    private static MainGraphicController instance;
    private final Stack<Button> sidebarButtonStack = new Stack<>(); // stack needed for the navigation logic to disable the button for the navigation to current view and for the integration with back logic
    private final Stack<GraphicController> navigationStack = new Stack<>(); // Stack with objects that will be controllers that holds the state of respective view
    @FXML private Button offerButton;
    @FXML private Button barterButton;
    @FXML
    private Button inventoryButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button itemBrowseButton;
    @FXML
    private HeaderBarController headerBarController;

    public MainGraphicController(){
        instance = this;
    }

    public static MainGraphicController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.attach(this);
        itemBrowseButton.fire(); // The applications starts on the Item Browse View by firing the correspondent button automatically at the start.
    }

    // Methods to load the views

    public void showUserDetailsView(String selectedUser) throws IOException {
        loadView("/it/uniroma2/ispw/decluttify/views/UserDetailsView.fxml");
    }

    public void showItemDetailsView(PreviewItemBean selectedItem) throws IOException {
        DataReceiver<PreviewItemBean> receiver = loadView("/it/uniroma2/ispw/decluttify/views/ItemDetailsView.fxml").getController();
        if (receiver != null) {
            receiver.initData(selectedItem);
        }
    }

    public void makeOfferView(PreviewItemBean targetItem) throws IOException {
        if(!SessionManager.getInstance().isLoggedIn()){
            throw new NotLoggedInException("Log in required");
        }
        else {
            DataReceiver<PreviewItemBean> receiver = loadView("/it/uniroma2/ispw/decluttify/views/MakeOfferView.fxml").getController();
            if (receiver != null) {
                receiver.initData(targetItem);
            }
        }
    }

    public void showMyOfferView() throws IOException {
        loadView("/it/uniroma2/ispw/decluttify/views/MyOffersView.fxml");
    }

    public void showOwnItemListView() throws IOException {
        loadView("/it/uniroma2/ispw/decluttify/ownItemListView.fxml");
        //TODO
    }

    public void showMyBartersView() throws IOException, NotLoggedInException {
        loadView("/it/uniroma2/ispw/decluttify/views/MyBartersView.fxml");
    }

    public void showItemBrowseView() throws IOException {
        loadView("/it/uniroma2/ispw/decluttify/views/ItemBrowserView.fxml");
    }

    private FXMLLoader loadView(String fxmlPath) throws NotLoggedInException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent nextView = loader.load();
        GraphicController nextGraphicController = loader.getController();
        borderPane.setCenter(nextView);
        nextGraphicController.setView(nextView);
        navigationStack.push(nextGraphicController);
        if (!nextGraphicController.isInSidebar){
            sidebarButtonStack.getLast().setDisable(false);
            sidebarButtonStack.getLast().setStyle("");
        }
        return loader; // needed to pass objects to the viewController (aka graphic controller), since FXMLLoader creates it when the view is loaded and this output is the reference
    }

    //Methods for back button logic, it uses a stack of view controller (= graphic controllers) as history of visited views

    public void goBack() {
        if (navigationStack.size() > 1) {
            GraphicController currentGraphicController = navigationStack.pop();
            GraphicController previousGraphicController = navigationStack.peek();
            Parent previousView = navigationStack.getLast().getView();
            borderPane.setCenter(previousView);
            Button button;
            if (currentGraphicController.isInSidebar) {
                button = this.sidebarButtonStack.pop();
                button.setDisable(false);
                button.setStyle("");
            }

            // navigation buttons on the sidebar need to be distinguished by the others button that can trigger navigation, because the button on sidebar that correspond
            // to the actual view has to be highlighted
            if(previousGraphicController.isInSidebar){
                button = this.sidebarButtonStack.getLast();
                button.setDisable(true);
                button.setStyle("-fx-opacity: 1.0;" +
                        "-fx-background-color: #4CAF50;" +
                        "-fx-effect: dropshadow(gaussian, rgba(76, 175, 80, 0.7), 10, 0, 0, 0);");
            }
        }
    }

    // Methods to handle buttons that triggers navigations on different views

    @FXML
    void handleInventoryButton(ActionEvent event) {
       //TODO
    }

    @FXML
    public void handleItemBrowseButton(ActionEvent event) throws IOException {
        this.showItemBrowseView();
        this.lockUnlockSidebar(event);
    }

    @FXML
    public void handleOfferButton(ActionEvent actionEvent) throws IOException {
        this.showMyOfferView();
        this.lockUnlockSidebar(actionEvent);
    }

    @FXML
    public void handleDonateButton(ActionEvent actionEvent) {
    }

    @FXML
    public void handleBarterButton(ActionEvent actionEvent) throws IOException {
        this.showMyBartersView();
        this.lockUnlockSidebar(actionEvent);
    }

    //Method for sidebar button stile and enabling/disabling logic

    public void lockUnlockSidebar(ActionEvent event) {
        Button button = (Button) event.getSource();
        if(!this.sidebarButtonStack.isEmpty()){
            this.sidebarButtonStack.getLast().setDisable(false);
            this.sidebarButtonStack.getLast().setStyle("");
        }
        button.setDisable(true);
        button.setStyle("-fx-opacity: 1.0;" + "-fx-background-color: #4CAF50;" + "-fx-effect: dropshadow(gaussian, rgba(76, 175, 80, 0.7), 10, 0, 0, 0);");
        this.sidebarButtonStack.push(button);
        boolean bool = SessionManager.getInstance().isLoggedIn();
        this.offerButton.setDisable(!bool);
        this.inventoryButton.setDisable(!bool);
        this.barterButton.setDisable(!bool);

    }

    public void triggerLogin() {
        if (headerBarController != null) {
            ActionEvent e = null;
            headerBarController.handleProfileButton(e);
        }
    }

    public void handleLogout() {
        this.sidebarButtonStack.clear();
        this.navigationStack.clear();
        itemBrowseButton.fire();
        this.offerButton.setStyle("");
        this.inventoryButton.setStyle("");
        this.barterButton.setStyle("");
        this.itemBrowseButton.setDisable(false);
    }

    public void handleOfferSent() throws IOException {
        while(navigationStack.size() > 1){
            navigationStack.pop();
        }
        this.sidebarButtonStack.clear();
        this.showMyOfferView();
    }

    @Override
    public void update() {
        if (SessionManager.getInstance().isLoggedIn()) {
            offerButton.setDisable(false);
            inventoryButton.setDisable(false);
            barterButton.setDisable(false);
        }
        else{
            offerButton.setDisable(true);
            inventoryButton.setDisable(true);
            barterButton.setDisable(true);
        }
    }
}

