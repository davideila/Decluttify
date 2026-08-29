package it.uniroma2.ispw.decluttify.model;

public enum ItemCondition{
    EXCELLENT ("Excellent"),
    GOOD ("Good"),
    NORMAL ("Normal"),
    BAD ("Bad");


    private final String condition;

    ItemCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
