package it.uniroma2.ispw.decluttify.bean;

public class FullItemBean extends PreviewItemBean{

    //This class is used for the representation of a detailed item information, needed for the Visualize Item Details View. It extends the class for a summarized item view, used in the item browser view

    private String location;
    private String creationDate;
    private int numOffers;


    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() { return location; }

    public void setNumOffers(int numOffers) {
        this.numOffers = numOffers;
    }
    public int getNumOffers() { return numOffers; }

}
