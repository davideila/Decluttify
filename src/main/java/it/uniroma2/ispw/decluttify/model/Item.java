package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class Item{
    private int id;
    private String name;
    private String description;
    private User owner;
    private ArrayList<String> images;
    private String location;
    private LocalDate creationDate;
    private ItemCategory category;
    private ItemCondition condition;
    private int offersCounter;
    private ItemStatus status;

    //CONSTRUCTORS

    public Item(int id, User owner, String name, String description, LocalDate creationDate, String category, String condition, int offersCounter, List<String> images, String location, String status) {
        this.id = id;
        this.owner = owner;
        this.setName(name);
        this.setDescription(description);
        this.creationDate = creationDate;
        this.offersCounter = offersCounter;
        this.setCondition(condition);
        this.setCategory(category);
        this.setImages(images);
        this.location = location;
        this.status = ItemStatus.valueOf(status);
    }

    public Item(int id, User owner, String name, String description, LocalDate creationDate, String category, String condition, int offersCounter, List<String> images) {
        this.id = id;
        this.owner = owner;
        this.setName(name);
        this.setDescription(description);
        this.creationDate = creationDate;
        this.offersCounter = offersCounter;
        this.setCondition(condition);
        this.setCategory(category);
        this.setImages(images);
    }

    public Item(String name, String description, String category, User owner, List<String> images, String location, String condition){
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
        this.setOwner(owner);
        this.setImages(images);
        this.setLocation(location);
        this.setCondition(condition);
    }

    public Item(int id){
        this.id = id;
    }


    //Business methods

    public void updateStatus(ItemStatus status){
        switch (this.status) {
            case TRADED:
                throw new ModelException("Cannot change item status: item already traded.");
            case AVAILABLE:
                this.setStatus(status);
                break;
            default:
                throw new ModelException("Unexpected error: status " + this.status + " is not handled.");
        }
    }

    public Offer requestBarter(User offerer, List<Item> offeredItems) {
        if(offerer == null || offeredItems == null || offeredItems.isEmpty()){
            throw new ModelException("Error: invalid barter request");
        }
        if(offerer.getUsername().equals(this.getOwner().getUsername())){
            throw new ModelException("Self offer is not possible");
        }
        if (!this.isExchangeable()){
            throw new ModelException("Requested item " + this.getName() + " with ID " + this.getId() + " is not exchangeable");
        }
        for(Item item : offeredItems){
            if (!item.getOwner().getUsername().equals(offerer.getUsername())) {
                throw new ModelException("Offered Item " + item.getName() + " with ID " + item.getId() + " is not owned by offerer " + offerer.getUsername() + ".");
            }
            if (!item.isExchangeable()){
                throw new ModelException("Offered item " + item.getName() + " with ID " + item.getId() + " is not exchangeable");
            }
        }
        return new Offer(offerer, this.getOwner(), offeredItems, this);
    }

    public boolean isExchangeable(){
        return this.getStatus().canBeExchanged();
    }

    public void incrOffersCounter(){
        this.offersCounter++;
    }

    public void decrOffersCounter(){
        if(this.offersCounter <= 0){
            throw new ModelException("Cannot decrease offers counter when offers count is less than 0.");
        }
        else this.offersCounter--;
    }

    public void addImage(String image){
        if(this.getStatus() == ItemStatus.TRADED) throw new ModelException("Traded item cannot be edited");
        if(this.getImages() == null){
            this.images = new ArrayList<>();
        }
        if(image == null){
            throw new ModelException("Please add a valid image.");
        }
        if (this.getImages().size() < 3) {
            this.getImages().add(image);
        }
        else throw new ModelException("Cannot add new image to item: max image number reached.");
    }

    public void edit(){
        //TODO
    }

    //GETTERS & SETTERS

    private void setImages(List<String> images) {
        if (images == null){
            this.addImage("placeholder_item.png");
        }
        else {
            for (String image : images) {
                this.addImage(image);
            }
        }
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCategory(String category){
        this.category = ItemCategory.valueOf(category.toUpperCase());
    }

    public String getCategory(){
        return switch (this.category) {
            case MUSIC -> "Music";
            case TECH -> "Tech";
            case BOOK -> "Book";
            case CLOTHING -> "Clothing";
            case MISCELLANEOUS -> "Miscellaneous";
            default -> "Miscellaneous";
        };
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setOffersCounter(int offersCounter) {
        this.offersCounter = offersCounter;
    }

    public int getOffersCounter() {
        return this.offersCounter;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }

    public User getOwner(){
        return this.owner;
    }

    private void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setCondition(String condition){
        this.condition = ItemCondition.valueOf(condition.toUpperCase());
    }

    public String getCondition(){
        return switch (this.condition) {
            case EXCELLENT -> "Excellent";
            case GOOD -> "Good";
            case NORMAL -> "Normal";
            case BAD -> "Bad";
            default -> null;
        };

    }

    private void setStatus(ItemStatus status){
        this.status = status;
    }

    public ItemStatus getStatus(){
        return this.status;
    }

    public int getId(){
        return this.id;
    }

}
