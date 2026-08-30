package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.exception.DuplicateOfferException;
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
    private final int TIMEOUT_MINUTES = 1;
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
        this.startSessionTimer();
    }

    public List<PreviewItemBean> loadUserInventory(UserBean user) {
        if(!sessionManager.isLoggedIn() || !sessionManager.getLoggedUser().getUsername().equals(user.getUsername())) throw new DecluttifyException("Operation not permitted");
        else {
            List<PreviewItemBean> pib = new ArrayList<>();
            List<Item> itemlist;
            try {
                itemlist = itemDAO.retrieveItemsByOwner(user.getUsername());
                for (Item item : itemlist) {
                    if (item.getStatus() == ItemStatus.AVAILABLE) {
                        pib.add(BeanConverter.toPreviewItemBean(item));
                    }
                }
            }catch (DAOException e){
                throw new DecluttifyException("Unable to retrieve inventory items due to a system error.", e);
            }
            return pib;
        }
    }

    private void startSessionTimer() {
        this.isSessionExpired = false;

        // Sonawqube request to use virtual thread
        this.timerThread = Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(TIMEOUT_MINUTES * 60_000L);
                isSessionExpired = true;
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void submitOffer(List<PreviewItemBean> offeredItemsBean, PreviewItemBean targetItemBean, UserBean offererBean) {
        if (isSessionExpired) {
            throw new SessionExpiredException("Session expired! Impossible to submit offer!");
        }
        if (timerThread != null) {
            timerThread.interrupt();
        }

        try{
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
            if(offerer == null){
                throw new DecluttifyException("Offerer user does not exists");
            }

            //Create offer
            Offer offer = targetItem.proposeBarter(offerer, offeredItems);

            //Check if offer is a duplicate
            List<Offer> partnersPendingOffers = offerDAO.retrievePendingOffersByPartners(offer.getOfferer().getUsername(), offer.getReceiver().getUsername());
            for(Offer partnerOffer : partnersPendingOffers){
                if(offer.isDuplicate(partnerOffer)){
                    throw new DuplicateOfferException("You have already submitted an identical offer for this item");
                }
            }

            //Save offer on persistence: save all items new state within a single transaction, rollback if not possible and throw exception. If success, save the new offer.
            List<Integer> itemsIDs = new ArrayList<>();
            itemsIDs.add(targetItem.getId());
            itemsIDs.addAll(offeredItemIDs);
            itemDAO.incrementItemsOfferCounters(itemsIDs);
            offerDAO.createOffer(offer);

            //Save Notifications on persistence
            notificationDAO.createNotification(new Notification(offer.getReceiver().getUsername(), "New offer from " + offer.getOfferer().getUsername(), "OFFER"));

        }catch(DAOException exception){
            throw new DecluttifyException("Unable to submit offer due to a system error", exception);
        }
    }

}
