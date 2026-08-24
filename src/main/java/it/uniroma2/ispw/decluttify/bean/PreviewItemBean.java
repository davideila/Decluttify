package it.uniroma2.ispw.decluttify.bean;

public class PreviewItemBean {

    // this class is used for a representation of a previewed item, as it needs only limited information to show to users

    private int id;
    private String name;
    private String description;
    private String owner;
    private String mainImageName;
    private String category;
    private String condition;
    private int numOffers;

    public PreviewItemBean() {
    }

    public PreviewItemBean(int id, String name, String description, String owner, String imageName,
                           String category, String condition, int numOffers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.mainImageName = imageName;
        this.category = category;
        this.condition = condition;
        this.numOffers = numOffers;
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

    public void setNumOffers(int numOffers) {
        this.numOffers = numOffers;
    }

    public int getNumOffers() { return numOffers; }

    public String getMainImageName() {
        return mainImageName;
    }

    public void setMainImage(String mainImagePath) {this.mainImageName = mainImagePath;}

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
