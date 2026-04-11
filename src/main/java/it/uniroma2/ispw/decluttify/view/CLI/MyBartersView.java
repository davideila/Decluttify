package it.uniroma2.ispw.decluttify.view.CLI;

import it.uniroma2.ispw.decluttify.bean.BarterBean;

import java.util.ArrayList;
import java.util.List;

public class MyBartersView extends View{

    private List<BarterBean> ongoingBarters;
    private boolean onSelection;
    private BarterBean selectedBarter;

    public void show() {
        printHeader("My Barters View");
        super.show();
    }

    @Override
    public void showFunctions() {
        if(!onSelection) {
            if (ongoingBarters == null) {
                this.showMessage("You have no ongoing barters", false);
            } else {
                System.out.println("\nSelect one barter");
                System.out.println("--------------------------------------------------------------------------------");
                int i = 1;
                for (BarterBean barter : ongoingBarters) {
                    if (!barter.getStatus().equalsIgnoreCase("COMPLETED")) {
                        System.out.printf("[%d] PARTNER: %-15s | Partner confirmed: %-10s | You confirmed: %-10s%n",
                                i,
                                barter.getPartnerName(),
                                barter.isPartnerConfirmed() ? "Yes" : "No",
                                barter.isYouConfirmed() ? "Yes" : "No");
                    } else {
                        System.out.printf("[%d] PARTNER: %-15s | Barter: COMPLETED%n",
                                i,
                                barter.getPartnerName());
                    }
                    System.out.printf("    SHIPPING: %-14s | ESCROW: %-15s%n",
                            barter.isShipping() ? "ON" : "OFF",
                            barter.isEscrow() ? "ON" : "OFF");

                    System.out.printf("    YOU GIVE: %-30s%n    YOU GET: %-30s",
                            barter.getMyItems(),
                            barter.getPartnerItems());
                    System.out.println("\n--------------------------------------------------------------------------------");
                    i++;
                }
            }
        }
        else{
            showSelection();
        }
    }

    private void showSelection() {
        this.onSelection = true;
        System.out.println("\nSelected barter");
        System.out.println("---------------------------------------------------------------------------");
        if (!selectedBarter.getStatus().equalsIgnoreCase("COMPLETED")) {
            System.out.printf("    PARTNER: %-15s | Partner confirmed: %-10s | You confirmed: %-10s%n",
                    selectedBarter.getPartnerName(),
                    selectedBarter.isPartnerConfirmed() ? "Yes" : "No",
                    selectedBarter.isYouConfirmed() ? "Yes" : "No");
        }
        else{
            System.out.printf("    PARTNER: %-15s | Barter: COMPLETED%n",
                    selectedBarter.getPartnerName());
        }

        System.out.printf("    SHIPPING: %-14s | ESCROW: %-15s%n",
                selectedBarter.isShipping() ? "ON" : "OFF",
                selectedBarter.isEscrow() ? "ON" : "OFF");

        System.out.printf("    YOU GIVE: %-30s%n    YOU GET: %-30s",
                selectedBarter.getMyItems(),
                selectedBarter.getPartnerItems());
        System.out.println("\n---------------------------------------------------------------------------\n");
        if(selectedBarter.isYouConfirmed() || selectedBarter.getStatus().equalsIgnoreCase("COMPLETED")){
            System.out.println("\n[0] Write Review   |   [1] View details\n\n");
        }
        else {
            System.out.println("\n[0] Confirm barter   |   [1] View details   |   [2] Open dispute\n\n");
        }
    }

    public boolean isOnSelection() {
        return onSelection;
    }

    public void setOnSelection(boolean onSelection) {
        this.onSelection = onSelection;
    }

    public BarterBean getSelectedBarter() {
        return selectedBarter;
    }

    public void setSelectedBarter(BarterBean selectedBarter) {
        this.selectedBarter = selectedBarter;
    }

    public List<BarterBean> getOngoingBarters() {
        return ongoingBarters;
    }

    public void setOngoingBarters(List<BarterBean> ongoingBarters) {
        if (ongoingBarters == null) {
            this.ongoingBarters = new ArrayList<>();
        } else {
            this.ongoingBarters = ongoingBarters;
        }
    }
}
