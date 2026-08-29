package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.model.Barter;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.model.Notification;
import it.uniroma2.ispw.decluttify.model.Offer;
import it.uniroma2.ispw.decluttify.persistence.dao.*;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import it.uniroma2.ispw.decluttify.utils.BeanConverter;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class ManageOfferController {

    private SessionManager sessionManager;
    private final OfferDAO offerDAO;
    private final BarterDAO barterDAO;
    private final ItemDAO itemDAO;
    private final NotificationDAO notificationDAO;

    public ManageOfferController(SessionManager sessionManager){
        this.sessionManager = sessionManager;
        this.offerDAO = DAOFactory.getDAOFactory().createOfferDAO();
        this.barterDAO = DAOFactory.getDAOFactory().createBarterDAO();
        this.itemDAO = DAOFactory.getDAOFactory().createItemDAO();
        this.notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
    }

    public List<OfferBean> loadReceivedOffers(UserBean receiverBean) {
        List<OfferBean> offersBeanList = new ArrayList<>();
        List<Offer> offersList;
        List<Integer> itemOfferedIds = new ArrayList<>();
        try {
            offersList = offerDAO.retrieveOffersByReceiver(receiverBean.getUsername());
            for (Offer offer : offersList) {
                for (Item offeredItem : offer.getItemOffered()) {
                    itemOfferedIds.add(offeredItem.getId());
                }
                offer.setItemOffered(itemDAO.retrieveItemsByIds(itemOfferedIds));
                offer.setItemRequested(itemDAO.retrieveItemById(offer.getItemRequested().getId()));
                itemOfferedIds.clear();
            }
        }catch(DAOException exception){
            throw new DecluttifyException("Unable to load received offers due to a system error", exception);
        }
        for (Offer offer : offersList) {
            offersBeanList.add(BeanConverter.toOfferBean(offer));
        }
        return offersBeanList;
    }

    public List<OfferBean> loadSentOffers(UserBean senderBean) {
        List<OfferBean> offersBeanList = new ArrayList<>();
        List<Offer> offersList;
        List<Integer> itemOfferedIds = new ArrayList<>();

        try {
            offersList = offerDAO.retrieveOffersBySender(senderBean.getUsername());
            for (Offer offer : offersList) {
                for (Item offeredItem : offer.getItemOffered()) {
                    itemOfferedIds.add(offeredItem.getId());
                }
                offer.setItemOffered(itemDAO.retrieveItemsByIds(itemOfferedIds));
                offer.setItemRequested(itemDAO.retrieveItemById(offer.getItemRequested().getId()));
                itemOfferedIds.clear();
            }
        }catch(DAOException exception){
            throw new DecluttifyException("Unable to load sent offers due to a system error", exception);
        }

        for (Offer offer : offersList) {
            offersBeanList.add(BeanConverter.toOfferBean(offer));
        }
        return offersBeanList;
    }

    public void acceptOffer(OfferBean offerBean) {
        // Get the offer from persistence
        List<Offer> collidingOffers;
        Offer offer = offerDAO.retrieveOfferById(offerBean.getID());
        offer.setItemRequested(itemDAO.retrieveItemById(offer.getItemRequested().getId()));
        List<Integer> itemsOfferedIds = new ArrayList<>();
        for(Item item: offer.getItemOffered()){
            itemsOfferedIds.add(item.getId());
        }
        offer.setItemOffered(itemDAO.retrieveItemsByIds(itemsOfferedIds));

        //Call method accept of Model instance Offer
        Barter barter = offer.accept();

        //Get colliding offers from persistence and cancel them on Model entity level
        collidingOffers = this.getCollidingOffers(offer);
        itemsOfferedIds.clear();
        for (Offer o : collidingOffers) {
            o.setItemRequested(itemDAO.retrieveItemById(o.getItemRequested().getId()));
            for(Item item: o.getItemOffered()){
                itemsOfferedIds.add(item.getId());
            }
            o.setItemOffered(itemDAO.retrieveItemsByIds(itemsOfferedIds));
            o.reject();
            itemsOfferedIds.clear();
        }

        //Update colliding offers and involved items on persistence, and update accepted offer. Everything as a single transaction because of concurrency
        offerDAO.acceptOffer(offer, collidingOffers);

        //Save barter on persistence
        barterDAO.createBarter(barter);

        //Save notification on Persistence
        notificationDAO.createNotification(new Notification(offer.getOfferer().getUsername(), "Offer Accepted!", "OFFER"));

    }

    // Colliding offers are offers that have the same item ids in their item offered list or requested.
    // For this method, the author was thinking to change the "offered" table on the db to have the itemreqid in the same table on the same column "itemid"(adding
    // a column with "role" to distingush the item role in the offer). So that a single select was needed to get colliding offers and items. But the inserts for the
    // offer creation was 1 more per new offer (another row for the item requested. So the author decided to keep the db table "offered" as ["offid", "itemid"]
    // (itemid is one of the offered item) and to use UNION/JOIN with "offer" table (because writing costs more than reading)
    private List<Offer> getCollidingOffers(Offer offer) {
        List<Offer> collidingOffers;
        collidingOffers = offerDAO.retrieveCollidingOffers(offer);
        return collidingOffers;
    }

    public void rejectOffer(OfferBean offerBean) {
        Offer offer = offerDAO.retrieveOfferById(offerBean.getID());
        offer.reject();
        offerDAO.rejectOffer(offer);

        notificationDAO.createNotification(new Notification(offer.getOfferer().getUsername(), "Offer Rejected!", "OFFER"));
    }

}
