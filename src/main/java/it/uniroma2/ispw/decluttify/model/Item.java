package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.exception.InvalidOfferException;
import it.uniroma2.ispw.decluttify.exception.ItemNotAvailableException;

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

    public Offer proposeBarter(User offerer, List<Item> offeredItems) {
        if(offerer == null || offeredItems == null || offeredItems.isEmpty()){
            throw new InvalidOfferException("Error: invalid offer");
        }
        if(offerer.getUsername().equals(this.getOwner().getUsername())){
            throw new InvalidOfferException("Self offer is not possible");
        }
        for(Item item : offeredItems){
            if (!item.getOwner().getUsername().equals(offerer.getUsername())) {
                throw new InvalidOfferException("Offered Item " + item.getName() + " with ID " + item.getId() + " is not owned by offerer " + offerer.getUsername() + ".");
            }
        }
        if(offeredItems.size() > 3){
            throw new InvalidOfferException("Cannot offer more than 3 items");
        }

        final boolean[] targetResult = { false }; /* Array because the runnable thread accepts only final local variables and to make it mutable array is used */
        final boolean[] offeredResult = { false };
        final InvalidOfferException[] threadException = new InvalidOfferException[1];

        Thread targetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isExchangeable()) {
                    targetResult[0] = true;
                }
            }
        });

        Thread offeredThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (offeredItems != null && !offeredItems.isEmpty()) {
                    boolean allOk = true;
                    for (Item item : offeredItems) {
                        if (!item.isExchangeable()) {
                            allOk = false;
                            break;
                        }
                        for(Item item2 : offeredItems){
                            if(offeredItems.indexOf(item) != offeredItems.indexOf(item2) && item.getId() == item2.getId()){
                                threadException[0] = new InvalidOfferException("Cannot propose the same item multiple times for an offer");
                            }
                        }
                    }
                    offeredResult[0] = allOk;
                }
            }
        });

        targetThread.start();
        offeredThread.start();
        try {
            targetThread.join();
            offeredThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DecluttifyException("Availability check interrupted!");
        }
        if (threadException[0] != null) {
            throw threadException[0];
        }
        if (!targetResult[0]) {
            throw new ItemNotAvailableException("Requested item is not available!");
        }
        if (!offeredResult[0]) {
            throw new ItemNotAvailableException("Some of the offered items are not available!");
        }

        for(Item item : offeredItems){
            item.incrOffersCounter();
        }
        this.incrOffersCounter();

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
            throw new IllegalStateException("Item has no offers.");
        }
        else this.offersCounter--;
    }

    public void addImage(String image){
        if(this.getStatus() == ItemStatus.TRADED) throw new IllegalStateException("Traded item cannot be edited");
        if(this.getImages() == null){
            this.images = new ArrayList<>();
        }
        if(image == null){
            throw new IllegalArgumentException("Please add a valid image.");
        }
        if (this.getImages().size() < 3) {
            this.getImages().add(image);
        }
        else throw new IllegalStateException("Cannot add new image to item: max image number reached.");
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
            case SPORT -> "Sport";
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

    public ItemStatus getStatus(){
        return this.status;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        if(this.id < 0){
            throw new IllegalArgumentException("Invalid item id.");
        }
        this.id = id;
    }

}
