package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestItem {

    protected Item CreateTestItem(String owner, String status){
        User user = this.CreateTestUser(owner);
        Item item = new Item(0, user, "Test title", "Test description", LocalDate.now(), ItemCategory.MISCELLANEOUS.getCategory(), ItemCondition.GOOD.getCondition(), 0, null, "Rome", status);
        return item;
    }

    protected Item CreateTestItem(String status){
        User user = this.CreateTestUser();
        Item item = new Item(0, user, "Test title", "Test description", LocalDate.now(), ItemCategory.MISCELLANEOUS.getCategory(), ItemCondition.GOOD.getCondition(), 0, null, "Rome", status);
        return item;
    }

    protected Item CreateTestItem(String owner, String status, int offCounter){
        User user = this.CreateTestUser(owner);
        Item item = new Item(0, user, "Test title", "Test description", LocalDate.now(), ItemCategory.MISCELLANEOUS.getCategory(), ItemCondition.GOOD.getCondition(), offCounter, null, "Rome", status);
        return item;
    }

    protected User CreateTestUser(){
        User user = new User("testUser", "pwd123456", 5, "testUser@outlook.com", "pepper");
        return user;
    }

    protected User CreateTestUser(String username){
        User user = new User(username, "pwd123456", 5, "testUser@outlook.com", "pepper");
        return user;
    }

    @Test
    public void testUpdateStatusFromTraded() {
        Item item = CreateTestItem("TRADED");
        assertThrows(ModelException.class, () ->{ item.updateStatus(ItemStatus.TRADED);});
    }

    @Test
    public void testUpdateStatusCorrect() {
        Item item = CreateTestItem("AVAILABLE");
        item.updateStatus(ItemStatus.TRADED);
        assertEquals(item.getStatus(), ItemStatus.TRADED);
    }

    @Test
    public void testRequestBarterNotExchangeable() {
        Item item = CreateTestItem("TRADED");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(CreateTestItem("AVAILABLE"));
        assertThrows(ModelException.class, () ->{item.requestBarter(this.CreateTestUser(), offeredItems);});
    }

    @Test
    public void testRequestBarterOfferNotOwner() {
        Item item = CreateTestItem("richard", "AVAILABLE");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(CreateTestItem("dave", "AVAILABLE"));
        offeredItems.add(CreateTestItem("dave", "AVAILABLE"));
        assertThrows(ModelException.class, () ->{item.requestBarter(this.CreateTestUser("testUser"), offeredItems);});
    }

    @Test
    public void testRequestBarterSelfOffer() {
        Item item = CreateTestItem("dave", "AVAILABLE");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(CreateTestItem("dave", "AVAILABLE"));
        offeredItems.add(CreateTestItem("dave", "AVAILABLE"));
        assertThrows(ModelException.class, () ->{item.requestBarter(this.CreateTestUser("dave"), offeredItems);});
    }

    @Test
    public void testRequestBarterCorrect() {
        Item item = CreateTestItem("mario","AVAILABLE");
        List<Item> offeredItems = new ArrayList<>();
        offeredItems.add(CreateTestItem("dave", "AVAILABLE"));
        offeredItems.add(CreateTestItem("dave", "AVAILABLE"));
        assertNotNull(item.requestBarter(this.CreateTestUser("dave"), offeredItems));
    }

    @Test
    public void testDecrOfferCounterLessThanZero() {
        Item item = CreateTestItem("dave", "AVAILABLE", 0);
        assertThrows(ModelException.class, () ->{item.decrOffersCounter();});
    }

    @Test
    public void testAddImageMoreThanThree() {
        Item item = CreateTestItem("dave", "AVAILABLE");
        String image_path_1 = "path1";
        String image_path_2 = "path2";
        String image_path_3 = "path3";
        item.addImage(image_path_1);
        item.addImage(image_path_2);
        assertThrows(ModelException.class, () ->{item.addImage(image_path_3);});
    }
}
