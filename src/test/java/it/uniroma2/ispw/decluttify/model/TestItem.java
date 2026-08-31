package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.InvalidOfferException;
import it.uniroma2.ispw.decluttify.exception.ItemNotAvailableException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestItem {

    protected Item createTestItem(String owner, String status){
        User user = this.createTestUser(owner);
        Item item = new Item(0, user, "Test title", "Test description", LocalDate.now(), ItemCategory.MISCELLANEOUS.getCategory(), ItemCondition.GOOD.getCondition(), 0, null, "Rome", status);
        return item;
    }

    protected Item createTestItem(String owner, String status, int offCounter){
        User user = this.createTestUser(owner);
        Item item = new Item(0, user, "Test title", "Test description", LocalDate.now(), ItemCategory.MISCELLANEOUS.getCategory(), ItemCondition.GOOD.getCondition(), offCounter, null, "Rome", status);
        return item;
    }

    public Item createTestItem(int id, String owner, String status){
        User user = this.createTestUser(owner);
        Item item = this.createTestItem(owner, status);
        item.setId(id);
        return item;
    }

    protected User createTestUser(String username){
        User user = new User(username, "pwd123456", 5, "testUser@outlook.com", "pepper");
        return user;
    }

    @Test
    public void testProposeBarterNotAvailable() {
        Item requestedItem = createTestItem("claire", "TRADED");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(createTestItem("dave", "AVAILABLE"));
        assertThrows(ItemNotAvailableException.class, () ->{
            requestedItem.proposeBarter(this.createTestUser("dave"), offeredItems);});
    }

    @Test
    public void testProposeBarterOfferNotOwner() {
        Item requestedItem = createTestItem(1, "richard", "AVAILABLE");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(createTestItem(2, "dave", "AVAILABLE"));
        offeredItems.add(createTestItem(3, "dave", "AVAILABLE"));
        assertThrows(InvalidOfferException.class, () ->{
            requestedItem.proposeBarter(this.createTestUser("testUser"), offeredItems);});
    }

    @Test
    public void testProposeBarterSelfOffer() {
        Item requestedItem = createTestItem(1, "dave", "AVAILABLE");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(createTestItem(2, "dave", "AVAILABLE"));
        offeredItems.add(createTestItem(3, "dave", "AVAILABLE"));
        assertThrows(InvalidOfferException.class, () ->{
            requestedItem.proposeBarter(this.createTestUser("dave"), offeredItems);});
    }

    @Test
    public void testProposeBarterDuplicateItemsOffered(){
        Item offItem1 = createTestItem(1, "dave", "AVAILABLE");
        Item offItem2 = createTestItem(1, "dave", "AVAILABLE");
        Item requestedItem = createTestItem(2, "richard", "AVAILABLE");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(offItem1);
        offeredItems.add(offItem2);
        assertThrows(InvalidOfferException.class, () ->{requestedItem.proposeBarter(this.createTestUser("dave"), offeredItems);});
    }

    @Test
    public void moreThanThreeItemsOffered(){
        Item offItem1 = createTestItem(1, "dave", "AVAILABLE");
        Item offItem2 = createTestItem(2, "dave", "AVAILABLE");
        Item offItem3 = createTestItem(3, "dave", "AVAILABLE");
        Item offItem4 = createTestItem(4, "dave", "AVAILABLE");
        Item requestedItem = createTestItem(5, "richard", "AVAILABLE");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(offItem1);
        offeredItems.add(offItem2);
        offeredItems.add(offItem3);
        offeredItems.add(offItem4);
        assertThrows(InvalidOfferException.class, () ->{requestedItem.proposeBarter(this.createTestUser("dave"), offeredItems);});
    }

    @Test
    public void testProposeBarterCorrect() {
        Item requestedItem = createTestItem(3, "mario","AVAILABLE");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(createTestItem(1, "dave", "AVAILABLE"));
        offeredItems.add(createTestItem(2, "dave", "AVAILABLE"));
        assertNotNull(requestedItem.proposeBarter(this.createTestUser("dave"), offeredItems));
    }

    @Test
    public void testProposeBarterIncrOfferCounter() {
        Item requestedItem = createTestItem(3, "mario","AVAILABLE");
        requestedItem.setOffersCounter(5);
        int preIncr = requestedItem.getOffersCounter();
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(createTestItem(1, "dave", "AVAILABLE"));
        offeredItems.add(createTestItem(2, "dave", "AVAILABLE"));
        requestedItem.proposeBarter(this.createTestUser("dave"), offeredItems);
        assertEquals(requestedItem.getOffersCounter(), preIncr + 1);
    }

    @Test
    public void testAddImageMoreThanThree() {
        Item item = createTestItem("dave", "AVAILABLE");
        String image_path_1 = "path1";
        String image_path_2 = "path2";
        String image_path_3 = "path3";
        item.addImage(image_path_1);
        item.addImage(image_path_2);
        assertThrows(IllegalStateException.class, () ->{item.addImage(image_path_3);});
    }

}
