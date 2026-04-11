package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.BarterBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.exception.NotLoggedInException;
import it.uniroma2.ispw.decluttify.model.*;
import it.uniroma2.ispw.decluttify.persistence.dao.*;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import it.uniroma2.ispw.decluttify.utils.BeanConverter;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class MakeBarterController {

    public void makeOffer(List<PreviewItemBean> offeredItemsBean, PreviewItemBean targetItemBean) {
        //Get Target item and offered item from persistence
        ItemDAO itemdao = DAOFactory.getDAOFactory().createItemDAO();
        Item targetItem = itemdao.retrieveItemById(targetItemBean.getId());
        ArrayList<Integer> itemIDs = new ArrayList<>();
        for (PreviewItemBean bean : offeredItemsBean) {
            itemIDs.add(bean.getId());
        }
        List<Item> offeredItems = itemdao.retrieveItemsByIds(itemIDs);

        //Get offerer (=logged) user from persistence
        User offerer;
        UserDAO userdao = DAOFactory.getDAOFactory().createUserDAO();
        offerer = userdao.retrieveUserByUsername(SessionManager.getInstance().getLoggedUser().getUsername());

        //Create offer
        Offer offer = targetItem.requestBarter(offerer, offeredItems);

        //Save offer on persistence
        OfferDAO offerdao = DAOFactory.getDAOFactory().createOfferDAO();
        offerdao.createOffer(offer);
        itemdao.updateItemOfferCounter(targetItem.getId(), +1);
        for(Item item : offeredItems) {
            itemdao.updateItemOfferCounter(item.getId(), +1);
        }

        //Save Notifications on persistence
        NotificationDAO notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
        notificationDAO.createNotification(new Notification(offer.getReceiver().getUsername(), "New Offer!", "OFFER"));

    }

    public void acceptOffer(OfferBean offerBean) {
        // Get the offer from persistence
        List<Offer> collidingOffers;
        OfferDAO offerdao = DAOFactory.getDAOFactory().createOfferDAO();
        Offer offer = offerdao.retrieveOfferById(offerBean.getID());
        ItemDAO itemdao = DAOFactory.getDAOFactory().createItemDAO();
        offer.setItemRequested(itemdao.retrieveItemById(offer.getItemRequested().getId()));
        List<Integer> itemsOfferedIds = new ArrayList<>();
        for(Item item: offer.getItemOffered()){
            itemsOfferedIds.add(item.getId());
        }
        offer.setItemOffered(itemdao.retrieveItemsByIds(itemsOfferedIds));

        //Call method accept of Model instance Offer
        Barter barter = offer.accept();

        //Get colliding offers from persistence and cancel them on Model entity level
        collidingOffers = this.getCollidingOffers(offer);
        itemsOfferedIds.clear();
        for (Offer o : collidingOffers) {
            o.setItemRequested(itemdao.retrieveItemById(o.getItemRequested().getId()));
            for(Item item: o.getItemOffered()){
                itemsOfferedIds.add(item.getId());
            }
            o.setItemOffered(itemdao.retrieveItemsByIds(itemsOfferedIds));
            o.reject();
            itemsOfferedIds.clear();
        }

        //Update colliding offers and involved items on persistence, and update accepted offer. Everything as a single transaction because of concurrency
        offerdao.acceptOffer(offer, collidingOffers);

        //Save barter on persistence
        BarterDAO barterdao = DAOFactory.getDAOFactory().createBarterDAO();
        barterdao.createBarter(barter);

        //Save notification on Persistence
        NotificationDAO notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
        notificationDAO.createNotification(new Notification(offer.getOfferer().getUsername(), "Offer Accepted!", "OFFER"));

    }

    // Colliding offers are offers that have the same item ids in their item offered list or requested.
    // For this method, the author was thinking to change the "offered" table on the db to have the itemreqid in the same table on the same column "itemid"(adding
    // a column with "role" to distingush the item role in the offer). So that a single select was needed to get colliding offers and items. But the inserts for the
    // offer creation was 1 more per new offer (another row for the item requested. So the author decided to keep the db table "offered" as ["offid", "itemid"]
    // (itemid is one of the offered item) and to use UNION/JOIN with "offer" table (because writing costs more than reading)
    private List<Offer> getCollidingOffers(Offer offer) {
        List<Offer> collidingOffers;
        OfferDAO offerdao = DAOFactory.getDAOFactory().createOfferDAO();
        collidingOffers = offerdao.retrieveCollidingOffers(offer);
        return collidingOffers;
    }



    public void rejectOffer(OfferBean offerBean) {
        OfferDAO offerdao = DAOFactory.getDAOFactory().createOfferDAO();
        Offer offer = offerdao.retrieveOfferById(offerBean.getID());
        offer.reject();
        offerdao.rejectOffer(offer);

        NotificationDAO notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
        notificationDAO.createNotification(new Notification(offer.getOfferer().getUsername(), "Offer Rejected!", "OFFER"));
    }


    public List<PreviewItemBean> loadUserInventory() {
        if(!this.isUserLogged()) throw new NotLoggedInException("Log in required");
        else {
            List<PreviewItemBean> pib = new ArrayList<>();
            List<Item> itemlist;

            ItemDAO itemdao = DAOFactory.getDAOFactory().createItemDAO();
            itemlist = itemdao.retrieveItemsByOwner(SessionManager.getInstance().getLoggedUser().getUsername());
            try {
                for (Item item : itemlist) {
                    if (item.getStatus() == ItemStatus.AVAILABLE) {
                        pib.add(BeanConverter.toPreviewItemBean(item));
                    }
                }
            } catch (NullPointerException e) {
                throw new RuntimeException(e);
            }
            return pib;
        }
    }

    private boolean isUserLogged() {
        return SessionManager.getInstance().isLoggedIn();
    }

    public List<OfferBean> loadReceivedOffers(String receiver) {
        List<OfferBean> offersBeanList = new ArrayList<>();
        List<Offer> offersList;
        List<Integer> itemOfferedIds = new ArrayList<>();
        OfferDAO offerdao = DAOFactory.getDAOFactory().createOfferDAO();
        ItemDAO itemdao = DAOFactory.getDAOFactory().createItemDAO();

        offersList = offerdao.retrieveOffersByReceiver(receiver);
        for(Offer offer: offersList) {
            for (Item offeredItem : offer.getItemOffered()){
                itemOfferedIds.add(offeredItem.getId());
            }
            offer.setItemOffered(itemdao.retrieveItemsByIds(itemOfferedIds));
            offer.setItemRequested(itemdao.retrieveItemById(offer.getItemRequested().getId()));
            itemOfferedIds.clear();
        }

        for (Offer offer : offersList) {
            offersBeanList.add(BeanConverter.toOfferBean(offer));
        }
        return offersBeanList;
    }

    public List<OfferBean> loadSentOffers(String sender) {
        List<OfferBean> offersBeanList = new ArrayList<>();
        List<Offer> offersList;
        List<Integer> itemOfferedIds = new ArrayList<>();
        OfferDAO offerdao = DAOFactory.getDAOFactory().createOfferDAO();
        ItemDAO itemdao = DAOFactory.getDAOFactory().createItemDAO();

        offersList = offerdao.retrieveOffersBySender(sender);
        for(Offer offer: offersList) {
            for (Item offeredItem : offer.getItemOffered()){
                itemOfferedIds.add(offeredItem.getId());
            }
            offer.setItemOffered(itemdao.retrieveItemsByIds(itemOfferedIds));
            offer.setItemRequested(itemdao.retrieveItemById(offer.getItemRequested().getId()));
            itemOfferedIds.clear();
        }

        for (Offer offer : offersList) {
            offersBeanList.add(BeanConverter.toOfferBean(offer));
        }
        return offersBeanList;
    }

    public List<BarterBean> loadUserBarters() {
        String username = SessionManager.getInstance().getLoggedUser().getUsername();
        List<BarterBean> barterslist;
        List<Barter> barters;
        BarterDAO barterdao = DAOFactory.getDAOFactory().createBarterDAO();
        barters = barterdao.retrieveBartersByUsername(username);
        for (Barter barter : barters) {
            barter.setOffer(this.loadOffer(barter.getOffer().getId()));
        }
        barterslist = BeanConverter.toBarterBeanList(barters, username);
        return barterslist;
    }

    private Offer loadOffer(int id) {
        Offer offer;
        List<Integer> itemOfferedIds = new ArrayList<>();
        OfferDAO offerdao = DAOFactory.getDAOFactory().createOfferDAO();
        ItemDAO itemdao = DAOFactory.getDAOFactory().createItemDAO();
        offer = offerdao.retrieveOfferById(id);
        for (Item offeredItem : offer.getItemOffered()){
            itemOfferedIds.add(offeredItem.getId());
        }
        offer.setItemOffered(itemdao.retrieveItemsByIds(itemOfferedIds));
        offer.setItemRequested(itemdao.retrieveItemById(offer.getItemRequested().getId()));
        return offer;
    }

    public boolean confirmBarter(BarterBean barterbean) {
        BarterDAO barterdao = DAOFactory.getDAOFactory().createBarterDAO();
        Barter barter = barterdao.retrieveBarterByID(barterbean.getId());
        barter.setOffer(this.loadOffer(barter.getOffer().getId()));
        barter.confirm(SessionManager.getInstance().getLoggedUser().getUsername());

        barterdao.updateBarter(barter);
        NotificationDAO notificationdao = DAOFactory.getDAOFactory().createNotificationDAO();
        notificationdao.createNotification(new Notification(barterbean.getPartnerName(), "Barter Confirmed!", "BARTER"));

        return barter.isCompleted();
    }

    public void createItem(PreviewItemBean item, UserBean user) {
        //TODO
    }

    public void disputeBarter(BarterBean selectedBarter) {
        //TODO
    }

    public void viewBarterDetails(BarterBean selectedBarter) {
        //TODO
    }
}
