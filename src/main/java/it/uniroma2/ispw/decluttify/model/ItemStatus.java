package it.uniroma2.ispw.decluttify.model;

public enum ItemStatus {
    AVAILABLE,
    TRADED;

    public boolean canBeExchanged() {
        return this.equals(ItemStatus.AVAILABLE);
    }
}
