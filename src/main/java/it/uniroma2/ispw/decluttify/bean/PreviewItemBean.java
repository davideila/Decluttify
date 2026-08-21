package it.uniroma2.ispw.decluttify.bean;

public class PreviewItemBean {

    // this class is used for a representation of a previewed item, as it needs only limited information to show to users

    private int id;
    private String name;
    private String description;
    private String owner;
    private String mainImagePath;
    private String category;
    private String condition;

    public PreviewItemBean() {
    }

    public PreviewItemBean(int id, String name, String description, String owner, String imagePath,
                           String category, String condition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.mainImagePath = imagePath;
        this.category = category;
        this.condition = condition;
    }

    public PreviewItemBean(int id, String owner, String name, String description, java.sql.Date creationDate, int category, int condition, int numOffers) {
    }

    // --- Getters & Setters ---

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

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImage(String mainImagePath) {this.mainImagePath = "uploads/item_images/" + mainImagePath;}

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
