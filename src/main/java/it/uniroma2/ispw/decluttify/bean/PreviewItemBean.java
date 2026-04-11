package it.uniroma2.ispw.decluttify.bean;

import java.util.ArrayList;

public class PreviewItemBean {

    // this class is used for a representation of a previewed item, as it needs only limited information to show to users

    private int id;
    private String name;
    private String description;
    private String owner;
    private ArrayList<String> images;
    private String category;
    private String condition;

    public PreviewItemBean() {
    }

    // Costruttore completo
    public PreviewItemBean(int id, String name, String description, String owner, ArrayList<String> images,
                           String category, String condition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.setImages(images);
        this.category = category;
        this.condition = condition;
    }

    public PreviewItemBean(int id, String owner, String name, String description, java.sql.Date creationDate, int category, int condition, int numOffers) {
    }

    // --- Metodi Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        for (String image : images) {
            this.addImage(image);
        }
    }

    public void addImage(String image) {
        if (this.getImages() == null) {
            this.images = new ArrayList<>();
        }
        if (this.getImages().size() < 3) {
            this.getImages().add("uploads/item_images/" + image);
        } else
            throw new IllegalArgumentException("Images can't be more than 3"); //mettere exception giusta/////////////////////////////////////////////////
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}




