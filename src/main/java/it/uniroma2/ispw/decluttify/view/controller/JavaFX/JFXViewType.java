package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.view.controller.ViewType;

public enum JFXViewType implements ViewType {
    ITEM_BROWSER("/it/uniroma2/ispw/decluttify/views/ItemBrowserView.fxml", false),
    ITEM_DETAILS("/it/uniroma2/ispw/decluttify/views/ItemDetailsView.fxml", false),
    OFFER_FORM("/it/uniroma2/ispw/decluttify/views/OfferFormView.fxml", true),
    MY_OFFERS("/it/uniroma2/ispw/decluttify/views/MyOffersView.fxml", true);

    private final String fxmlPath;
    private final boolean loginRequired;

    JFXViewType(String fxmlPath, boolean loginRequired) {
        this.fxmlPath = fxmlPath;
        this.loginRequired = loginRequired;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public boolean isLoginRequired() {
        return this.loginRequired;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}
