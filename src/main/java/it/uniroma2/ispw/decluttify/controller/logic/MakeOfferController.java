package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.LoginException;
import it.uniroma2.ispw.decluttify.exception.SessionExpiredException;
import it.uniroma2.ispw.decluttify.model.*;
import it.uniroma2.ispw.decluttify.persistence.dao.*;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import it.uniroma2.ispw.decluttify.utils.BeanConverter;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class MakeOfferController {

    private SessionManager sessionManager;
    private boolean isSessionExpired;
    private final int TIMEOUT_MINUTES = 15;
    private Thread timerThread;
    private final OfferDAO offerDAO;
    private final UserDAO userDAO;
    private final ItemDAO itemDAO;
    private final NotificationDAO notificationDAO;

    public MakeOfferController(SessionManager sessionManager){
        this.sessionManager = sessionManager;
        this.offerDAO = DAOFactory.getDAOFactory().createOfferDAO();
        this.userDAO = DAOFactory.getDAOFactory().createUserDAO();
        this.itemDAO = DAOFactory.getDAOFactory().createItemDAO();
        this.notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
        this.isSessionExpired = false;
    }

    public List<PreviewItemBean> loadUserInventory(UserBean user) {
        if(!sessionManager.isLoggedIn() || sessionManager.getLoggedUser().getUsername() != user.getUsername()) throw new LoginException("Log in required");
        else {
            this.startSessionTimer();
            List<PreviewItemBean> pib = new ArrayList<>();
            List<Item> itemlist;
            
            itemlist = itemDAO.retrieveItemsByOwner(user.getUsername());
            for (Item item : itemlist) {
                if (item.getStatus() == ItemStatus.AVAILABLE) {
                    pib.add(BeanConverter.toPreviewItemBean(item));
                }
            }
            return pib;
        }
    }

    private void startSessionTimer() {
        this.isSessionExpired = false;

        this.timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(TIMEOUT_MINUTES * 60000);
                    isSessionExpired = true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        this.timerThread.setDaemon(true);
        this.timerThread.start();
    }

    public void submitOffer(List<PreviewItemBean> offeredItemsBean, PreviewItemBean targetItemBean, UserBean offererBean) {
        if (isSessionExpired) {
            throw new SessionExpiredException("Session expired! Impossible to submit offer!");
        }
        if (timerThread != null) {
            timerThread.interrupt();
        }

        //Get Target item and offered item from persistence
        Item targetItem = itemDAO.retrieveItemById(targetItemBean.getId());
        ArrayList<Integer> offeredItemIDs = new ArrayList<>();
        for (PreviewItemBean bean : offeredItemsBean) {
            offeredItemIDs.add(bean.getId());
        }
        List<Item> offeredItems = itemDAO.retrieveItemsByIds(offeredItemIDs);

        //Get offerer (=logged) user from persistence
        User offerer;
        offerer = userDAO.retrieveUserByUsername(offererBean.getUsername());

        //Create offer
        Offer offer = targetItem.proposeBarter(offerer, offeredItems);

        //Save offer on persistence: save all items new state within a single transaction, rollback if not possible and throw exception. If success, save the new offer.
        try {
            List<Integer> itemsIDs = new ArrayList<>();
            itemsIDs.add(targetItem.getId());
            itemsIDs.addAll(offeredItemIDs);
            itemDAO.incrementItemsOfferCounters(itemsIDs);
            offerDAO.createOffer(offer);
        }catch(DAOException exception){
            exception.printStackTrace();
            throw new DAOException(exception.getMessage());
        }

        //Save Notifications on persistence
        notificationDAO.createNotification(new Notification(offer.getReceiver().getUsername(), "New Offer!", "OFFER"));
    }

}
