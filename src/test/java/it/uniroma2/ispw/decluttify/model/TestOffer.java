package it.uniroma2.ispw.decluttify.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestOffer {

    private TestItem testItem = new TestItem();

    public Offer CreateTestOffer(String offererUsername, String receiverUsername){
        User offerer = testItem.createTestUser(offererUsername);
        User receiver = testItem.createTestUser(receiverUsername);
        Item itemReq = testItem.createTestItem(4, receiverUsername, "AVAILABLE");
        List<Item> itemOff = new ArrayList<>();
        itemOff.add(testItem.createTestItem(1, offererUsername, "AVAILABLE"));
        itemOff.add(testItem.createTestItem(2, offererUsername, "AVAILABLE"));
        itemOff.add(testItem.createTestItem(3, offererUsername, "AVAILABLE"));
        Offer offer = new Offer(offerer, receiver, itemOff, itemReq);
        return offer;
    }

    @Test
    public void IsDuplicateTrue(){
        Offer offer1 = CreateTestOffer("dave", "claire");
        Offer offer2 = CreateTestOffer("dave", "claire");
        assertTrue(offer1.isDuplicate(offer2));
    }

    @Test
    public void IsDuplicateFalse(){
        Offer offer1 = CreateTestOffer("dave", "claire");
        Offer offer2 = CreateTestOffer("dave", "claire");
        offer2.getItemOffered().getFirst().setId(0);
        assertFalse(offer1.isDuplicate(offer2));
    }

}
