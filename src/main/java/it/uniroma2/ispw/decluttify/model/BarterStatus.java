package it.uniroma2.ispw.decluttify.model;

public enum BarterStatus {
    ONGOING("ONGOING"),
    COMPLETED("COMPLETED"),
    DISPUTED("DISPUTED"),
    CONFIRMED("CONFIRMED"),
    ;
    //TODO Status for shipping for each user

    private String status;

    BarterStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
