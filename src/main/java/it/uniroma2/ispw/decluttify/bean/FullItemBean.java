package it.uniroma2.ispw.decluttify.bean;

import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import java.util.ArrayList;

public class FullItemBean extends PreviewItemBean{

    //This class is used for the representation of a detailed item information, needed for the Visualize Item Details CLIView. It extends the class for a summarized item view, used in the item browser view

    private String location;
    private String creationDate;
    private int numOffers;
    private ArrayList<String> images = new ArrayList<>();

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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        for (String image : images) {
            this.addImage(image);
        }
    }

    public void addImage(String image) {
        if (this.images.size() < 3) {
            this.images.add("uploads/item_images/" + image);
        } else {
            throw new DecluttifyException("Images can't be more than 3");
        }
    }

}
