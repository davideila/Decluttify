package it.uniroma2.ispw.decluttify.persistence.dao;

import it.uniroma2.ispw.decluttify.model.Item;
import java.util.List;

public abstract class ItemDAO {
    public abstract Item retrieveItemById(int itemId);
    public abstract List<Item> retrieveItemsByIds(List<Integer> itemIDs);
    public abstract List<Item> retrieveAllAvailableItems();
    public abstract void createItem(Item item);
    public abstract void deleteItemById(int itemId);
    public abstract void updateItemOfferCounter(int id, int num);
    public abstract List<Item> retrieveItemsByOwner(String username);
}
