package it.uniroma2.ispw.decluttify.view.controller.cli;

import it.uniroma2.ispw.decluttify.view.controller.ViewType;

public enum CLIViewType implements ViewType {
    ITEM_BROWSER(false),
    ITEM_DETAILS(false),
    OFFER_FORM(true),
    MY_OFFERS(true),
    LOGIN(false),
    MY_INVENTORY(true),
    HOME(false);

    private final boolean loginRequired;

    CLIViewType(boolean loginRequired) {
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
}
