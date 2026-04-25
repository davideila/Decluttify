package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestOffer {

    private static TestItem testItem = new TestItem();

    protected Offer CreateTestOffer(String offererUsername, String receiverUsername){
        User offerer = testItem.CreateTestUser(offererUsername);
        User receiver = testItem.CreateTestUser(receiverUsername);
        Item itemReq = testItem.CreateTestItem(receiverUsername, "AVAILABLE", 2);
        List<Item> itemOff = new ArrayList<>();
        itemOff.add(testItem.CreateTestItem(offererUsername, "AVAILABLE", 3));
        itemOff.add(testItem.CreateTestItem(offererUsername, "AVAILABLE", 3));
        itemOff.add(testItem.CreateTestItem(offererUsername, "AVAILABLE", 1));
        Offer offer = new Offer(offerer, receiver, itemOff, itemReq);
        return offer;
    }

    @Test
    public void TestAcceptCorrect(){
        Offer offer = this.CreateTestOffer("offerer", "receiver");
        Barter barter = null;
        barter = offer.accept();
        assertNotNull(barter);
    }

    @Test
    public void TestAcceptWrongState(){
        Offer offer = this.CreateTestOffer("offerer", "receiver");
        //offer.setState(new StateMachineImpl(offer, OfferStatus.ACCEPTED));
        offer.setStatus(OfferStatus.ACCEPTED);
        assertThrows(ModelException.class, () -> {offer.accept();});
    }

    @Test
    public void TestRejectCorrect(){
        Offer offer = this.CreateTestOffer("offerer", "receiver");
        offer.reject();
        assertEquals(OfferStatus.REJECTED, offer.getStatus());
    }

    @Test
    public void TestRejectWrongState(){
        Offer offer = this.CreateTestOffer("offerer", "receiver");
        offer.setStatus(OfferStatus.ACCEPTED);
        assertThrows(ModelException.class, () -> {offer.reject();});
    }
}
