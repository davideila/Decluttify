package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestVisualizeItemController {

    @Test
    public void testLoadAvailableItemsCorrect()
    {
        VisualizeItemController controller = new VisualizeItemController();
        assertFalse(controller.loadAvailableItems().isEmpty());
    }

    @Test
    public void testLoadAvailableItemsBeanConversion() {
        VisualizeItemController controller = new VisualizeItemController();
        assertEquals(PreviewItemBean.class, controller.loadAvailableItems().getLast().getClass());
    }

    @Test
    public void testLoadItemDetailsCorrect()
    {
        VisualizeItemController controller = new VisualizeItemController();
        assertEquals(FullItemBean.class, controller.loadItemDetails(3).getClass());
    }

    @Test
    public void testLoadItemDetailsInvalidId() {
        VisualizeItemController controller = new VisualizeItemController();
        assertThrows(DAOException.class, () -> controller.loadItemDetails(-1));
    }

}
