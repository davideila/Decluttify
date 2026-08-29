package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestVisualizeItemController {

    protected PreviewItemBean createPreviewItemBean(){
        PreviewItemBean previewItemBean = new PreviewItemBean(1, "name", "description", "owner", "item_1_1.png", null, null, 0);
        return previewItemBean;
    }

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
        assertEquals(FullItemBean.class, controller.loadItemDetails(createPreviewItemBean()).getClass());
    }

    @Test
    public void testLoadItemDetailsInvalidId() {
        VisualizeItemController controller = new VisualizeItemController();
        PreviewItemBean item = createPreviewItemBean();
        item.setId(-1);
        assertThrows(DecluttifyException.class, () -> controller.loadItemDetails(item));
    }

}
