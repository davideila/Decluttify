package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.OfferBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.model.Offer;
import it.uniroma2.ispw.decluttify.persistence.dao.*;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import it.uniroma2.ispw.decluttify.utils.BeanConverter;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class VisualizeOfferController {

    private SessionManager sessionManager;
    private final OfferDAO offerDAO;
    private final ItemDAO itemDAO;
    private final NotificationDAO notificationDAO;

    public VisualizeOfferController(SessionManager sessionManager){
        this.sessionManager = sessionManager;
        this.offerDAO = DAOFactory.getDAOFactory().createOfferDAO();
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

}
