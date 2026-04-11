package it.uniroma2.ispw.decluttify.bean;

import java.util.List;

public class BarterBean {
    private int id;
    private String partnerName;
    private String itemsSummary;
    private List<String> partnerItems;
    private List<String> myItems;
    private String startDate;
    private String completionDate;
    private String status;
    private boolean shipping;
    private boolean escrow;
    private boolean partnerConfirmed;
    private boolean youConfirmed;

    public BarterBean() {
    }

    public BarterBean(int id, String partnerName, String itemsSummary, List<String> partnerItems, List<String> myItems, String startDate, String completionDate, boolean partnerConfirmed, boolean youConfirmed,  boolean shipping, boolean escrow) {
        this.id = id;
        this.partnerName = partnerName;
        this.itemsSummary = itemsSummary;
        this.partnerItems = partnerItems;
        this.myItems = myItems;
        this.startDate = startDate;
        this.completionDate = completionDate;
        this.partnerConfirmed = partnerConfirmed;
        this.youConfirmed = youConfirmed;
        this.shipping = shipping;
        this.escrow = escrow;
    }

    // Getter & Setter

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPartnerName() { return partnerName; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }

    public List<String> getMyItems() {return myItems;}
    public void setMyItems(List<String> myItems) { this.myItems = myItems; }

    public List<String> getPartnerItems() {return partnerItems;}
    public void setPartnerItems(List<String> partnerItems) { this.partnerItems = partnerItems; }

    public String getItemsSummary() { return itemsSummary; }
    public void setItemsSummary(String itemsSummary) { this.itemsSummary = itemsSummary; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getCompletionDate() { return completionDate; }
    public void setCompletionDate(String completionDate) { this.completionDate = completionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isPartnerConfirmed() { return partnerConfirmed; }
    public void setPartnerConfirmed(boolean partnerConfirmed) { this.partnerConfirmed = partnerConfirmed; }

    public boolean isYouConfirmed() { return youConfirmed; }
    public void setYouConfirmed(boolean youConfirmed) { this.youConfirmed = youConfirmed; }

    public boolean isShipping() { return shipping; }
    public void setShipping(boolean shipping) { this.shipping = shipping; }

    public boolean isEscrow() { return escrow; }
    public void setEscrow(boolean escrow) { this.escrow = escrow; }

}
