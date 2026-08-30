package it.uniroma2.ispw.decluttify.view.cli;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class CLIMyOffersView extends CLIView {

    private List<OfferBean> sentOffers;
    private List<OfferBean> receivedOffers;
    private boolean onSelection;
    private OfferBean selectedOffer;

    public CLIMyOffersView(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public void show() {
        printHeader("My Offers View");
        super.show();
    }

    @Override
    public void showFunctions() {
        if (onSelection) {
            showSelection();
            return;
        }

        if (this.receivedOffers == null || this.receivedOffers.isEmpty()) {
            this.showMessage("You have no incoming offers", false);
            return;
        }

        System.out.println("\nSelect one offer");
        System.out.println("--------------------------------------------------------------------------------");

        int i = 1;
        for (OfferBean offer : receivedOffers) {
            printOfferDetails(i++, offer);
        }
    }

    // ################################################################################################
    // Private helper method to reduce complexity of method showFunctions as per sonarqube request

    private void printOfferDetails(int index, OfferBean offer) {
        System.out.printf("[%d] FROM: %-15s | REQUESTED: %-20s%n",
                index,
                offer.getOfferer(),
                offer.getRequestedItem().getName());

        System.out.printf("    SHIPPING: %-10s | ESCROW: %-10s%n",
                offer.isShipping() ? "ON" : "OFF",
                offer.isEscrow() ? "ON" : "OFF");

        List<String> itemNames = new ArrayList<>();
        for (PreviewItemBean item : offer.getOfferedItemList()) {
            itemNames.add(item.getName());
        }

        System.out.print("    OFFERED ITEMS: ");
        System.out.println(String.join(", ", itemNames));
        System.out.println("--------------------------------------------------------------------------------");
    }

    // #####################################################################################################

    public void showSelection() {
        onSelection = true;
        System.out.println("\nSelected offer");
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("FROM: %-15s | REQUESTED: %-20s%n",
                selectedOffer.getOfferer(),
                selectedOffer.getRequestedItem().getName());

        System.out.printf("    SHIPPING: %-10s | ESCROW: %-10s%n",
                selectedOffer.isShipping() ? "ON" : "OFF",
                selectedOffer.isEscrow() ? "ON" : "OFF");

        System.out.print("    OFFERED ITEMS: ");
        int j = 1;
        for(PreviewItemBean item: selectedOffer.getOfferedItemList()) {
            System.out.print(item.getName());
            j++;
            if(j < selectedOffer.getOfferedItemList().size()) {
                System.out.print(", ");
            }
        }
        System.out.println("\n---------------------------------------------------------------------------");
        System.out.println("\n[0] Accept offer   |   [1] Reject offer\n");
    }

    public void setSentOffers(List<OfferBean> sentOffers) {
        if (sentOffers == null) {
            this.sentOffers = new ArrayList<>();
        } else {
            this.sentOffers = sentOffers;
        }
    }

    public void setReceivedOffers(List<OfferBean> receivedOffers) {
        if (receivedOffers == null) {
            this.receivedOffers = new ArrayList<>();
        } else {
            this.receivedOffers = receivedOffers;
        }
    }

    public void setSelectedOffer(OfferBean selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    public OfferBean getSelectedOffer() {
        return selectedOffer;
    }

    public boolean isOnSelection() {
        return onSelection;
    }

    public void setOnSelection(boolean onSelection) {
        this.onSelection = onSelection;
    }

    public List<OfferBean> getReceivedOffers() {
        return receivedOffers;
    }
}
