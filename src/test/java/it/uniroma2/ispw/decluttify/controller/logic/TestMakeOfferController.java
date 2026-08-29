package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.exception.DuplicateOfferException;
import it.uniroma2.ispw.decluttify.model.*;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.ItemDAO;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestMakeOfferController {

    MakeOfferController controller;

    @BeforeEach
    public void setupSession() {
        PersistenceManager.getInstance().setupTestEnvironment();
        SessionManager sessionManager = new SessionManager();
        LoginController loginController = new LoginController(sessionManager);
        loginController.login("dave", "dave");
        controller = new MakeOfferController(sessionManager);
    }


    @Test
    public void testSubmitOfferCounterIncr() {
        List<PreviewItemBean> itemOfferList = new ArrayList<>();
        PreviewItemBean itemBean1 = new PreviewItemBean();
        itemBean1.setId(1);
        itemOfferList.add(itemBean1);

        ItemDAO itemdao = DAOFactory.getDAOFactory().createItemDAO();
        //int offersCounterItem1_PreIncr = itemdao.retrieveItemById(itemBean1.getId()).getOffersCounter();

        PreviewItemBean targetItemBean = new PreviewItemBean();
        targetItemBean.setId(6);
        int offersCounterItemTarget_PreIncr = itemdao.retrieveItemById(targetItemBean.getId()).getOffersCounter();
        UserBean userBean = new UserBean("dave", 0);
        controller.submitOffer(itemOfferList,targetItemBean, userBean);

        //int offersCounterItem1_PostIncr = itemdao.retrieveItemById(itemBean1.getId()).getOffersCounter();
        int offersCounterItemTarget_PostIncr = itemdao.retrieveItemById(targetItemBean.getId()).getOffersCounter();
        //assertEquals(offersCounterItem1_PreIncr + 1, offersCounterItem1_PostIncr);
        assertEquals(offersCounterItemTarget_PreIncr + 1, offersCounterItemTarget_PostIncr);
    }

    @Test
    public void testSubmitOfferDuplicateOffer() {
        List<PreviewItemBean> offeredItems = new ArrayList<>();
        PreviewItemBean offeredItemBean = new PreviewItemBean();
        offeredItemBean.setId(1);
        offeredItems.add(offeredItemBean);
        PreviewItemBean targetItemBean = new PreviewItemBean();
        targetItemBean.setId(2);
        UserBean userBean = new UserBean("dave", 0);
        assertThrows(DuplicateOfferException.class, () -> controller.submitOffer(offeredItems, targetItemBean, userBean));
    }

}
