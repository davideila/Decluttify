package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.model.Notification;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.ItemDAO;
import it.uniroma2.ispw.decluttify.persistence.dao.NotificationDAO;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestMakeBarterController {

    @BeforeEach
    public void setupTestEnvironment(){
        PersistenceManager.getInstance().setupTestEnvironment();
    }

    @BeforeEach
    public void setupSession() {
        LoginController loginController = new LoginController();
        loginController.login("dave", "dave");
    }


    @Test
    public void testMakeOfferCounterIncr() {
        List<PreviewItemBean> itemOfferList = new ArrayList<>();
        PreviewItemBean itemBean1 = new PreviewItemBean();
        itemBean1.setId(1); // owner dave
        itemOfferList.add(itemBean1);

        ItemDAO itemdao = DAOFactory.getDAOFactory().createItemDAO();
        //int offersCounterItem1_PreIncr = itemdao.retrieveItemById(itemBean1.getId()).getOffersCounter();

        PreviewItemBean targetItemBean = new PreviewItemBean();
        targetItemBean.setId(3); // owner richard
        int offersCounterItemTarget_PreIncr = itemdao.retrieveItemById(targetItemBean.getId()).getOffersCounter();

        MakeBarterController makeBarterController = new MakeBarterController();
        makeBarterController.makeOffer(itemOfferList,targetItemBean);

        //int offersCounterItem1_PostIncr = itemdao.retrieveItemById(itemBean1.getId()).getOffersCounter();
        int offersCounterItemTarget_PostIncr = itemdao.retrieveItemById(targetItemBean.getId()).getOffersCounter();
        //assertEquals(offersCounterItem1_PreIncr + 1, offersCounterItem1_PostIncr);
        assertEquals(offersCounterItemTarget_PreIncr + 1, offersCounterItemTarget_PostIncr);
    }

    @Test
    public void testMakeOfferSelf() {
        MakeBarterController controller = new MakeBarterController();
        PreviewItemBean targetBean = new PreviewItemBean();
        targetBean.setId(1);
        List<PreviewItemBean> itemOfferList = new ArrayList<>();
        PreviewItemBean itemBean1 = new PreviewItemBean();
        itemBean1.setId(1);
        itemOfferList.add(itemBean1);
        assertThrows(ModelException.class, () -> controller.makeOffer(itemOfferList, targetBean));
    }

    @Test
    public void testAcceptOfferCorrectNotification() {
        MakeBarterController controller = new MakeBarterController();
        List<OfferBean> offerList = new ArrayList<>();
        offerList = controller.loadReceivedOffers("richard");
        controller.acceptOffer(offerList.getLast());
        NotificationDAO notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
        List<Notification> notifications = notificationDAO.retrieveNotificationByUser("dave");
        assertEquals("Offer Accepted!", notifications.getLast().getMessage());
    }


}
