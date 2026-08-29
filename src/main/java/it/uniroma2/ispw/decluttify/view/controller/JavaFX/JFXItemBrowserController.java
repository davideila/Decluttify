package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.controller.logic.VisualizeItemController;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.JavaFX.customTiles.itemTile.JFXItemTileBrowser;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import java.util.ArrayList;
import java.util.List;

public class JFXItemBrowserController extends JFXGraphicController {

    private final VisualizeItemController visualizeItemController;
    private final List<PreviewItemBean> browsingItems = new ArrayList<>();
    @FXML private TilePane tilePane;
    @FXML private Button filtersButton;

    public JFXItemBrowserController(Navigator navigator, SessionManager sm) {
        super(navigator, sm, JFXViewType.ITEM_BROWSER);
        this.visualizeItemController = new VisualizeItemController();
    }

    @Override
    public void init() {
        this.filtersButton.setOnAction(e -> {
            AlertProvider.showComingSoon();
        });
        try {
            browsingItems.clear();
            browsingItems.addAll(this.visualizeItemController.loadAvailableItems());
            refreshTilePane();
        } catch (DecluttifyException e) {
            handleErrorAlert("Application error", e);
        }catch (Exception e) {
            handleUnexpectedErrorAlert();
            e.printStackTrace();
        }
    }

    private void refreshTilePane(){
        if (tilePane != null) {
            tilePane.getChildren().clear();
            for (PreviewItemBean item : browsingItems) {
                this.addItemTile(item);
            }
        }
    }

    private void addItemTile(PreviewItemBean item) {
        JFXItemTileBrowser tile = new JFXItemTileBrowser(item);
        this.tilePane.getChildren().add(tile);

        // Item tile click handler
        tile.setOnMouseClicked(event -> {
            this.navigator.navigateTo(JFXViewType.ITEM_DETAILS, item);
        });
    }

}
