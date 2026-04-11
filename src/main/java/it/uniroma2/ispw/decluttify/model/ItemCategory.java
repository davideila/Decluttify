package it.uniroma2.ispw.decluttify.model;

public enum ItemCategory{
    MUSIC ("Music"),
    TECH ("Tech"),
    BOOK ("Book"),
    CLOTHING ("Clothing"),
    MISCELLANEOUS ("Miscellaneous"),;

    private final String category;

    ItemCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
};
