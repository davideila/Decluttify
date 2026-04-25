package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBarter {

    public Barter createTestBarter(){
        TestOffer to = new TestOffer();
        return to.CreateTestOffer("offerer", "receiver").accept();
    }

    @Test
    public void testBarterConfirm(){
        Barter barter = createTestBarter();
        barter.confirm("offerer");
        barter.confirm("receiver");
        assertEquals(BarterStatus.COMPLETED, barter.getStatus());
    }

    @Test
    public void testBarterConfirmOnlyByOfferer(){
        Barter barter = createTestBarter();
        barter.confirm("offerer");
        assertEquals(BarterStatus.CONFIRMED, barter.getStatus());
    }

    @Test
    public void testBarterConfirmByUnknown(){
        Barter barter = createTestBarter();
        assertThrows(ModelException.class , () -> barter.confirm("wrongUser"));
    }

}
