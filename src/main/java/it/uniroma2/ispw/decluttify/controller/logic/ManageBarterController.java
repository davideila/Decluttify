package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.BarterBean;
import it.uniroma2.ispw.decluttify.bean.UserBean;
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

public class ManageBarterController {

    private SessionManager sessionManager;
    private final OfferDAO offerDAO;
    private final BarterDAO barterDAO;
    private final ItemDAO itemDAO;
    private final NotificationDAO notificationDAO;

    public ManageBarterController(SessionManager sessionManager){
        this.sessionManager = sessionManager;
        this.offerDAO = DAOFactory.getDAOFactory().createOfferDAO();
        this.barterDAO = DAOFactory.getDAOFactory().createBarterDAO();
        this.itemDAO = DAOFactory.getDAOFactory().createItemDAO();
        this.notificationDAO = DAOFactory.getDAOFactory().createNotificationDAO();
    }

    public List<BarterBean> loadUserBarters(UserBean userBean) {
        String username = userBean.getUsername();
        List<BarterBean> barterslist;
        List<Barter> barters;

        barters = barterDAO.retrieveBartersByUsername(username);
        for (Barter barter : barters) {
            barter.setOffer(this.loadOffer(barter.getOffer().getId()));
        }
        barterslist = BeanConverter.toBarterBeanList(barters, username);
        return barterslist;
    }

    public boolean confirmBarter(BarterBean barterbean, UserBean confirmer) {
        Barter barter = barterDAO.retrieveBarterByID(barterbean.getId());
        barter.setOffer(this.loadOffer(barter.getOffer().getId()));
        barter.confirm(confirmer.getUsername());

        barterDAO.updateBarter(barter);

        notificationDAO.createNotification(new Notification(barterbean.getPartnerName(), "Barter Confirmed!", "BARTER"));

        return barter.isCompleted();
    }

    private Offer loadOffer(int id) {
        Offer offer;
        List<Integer> itemOfferedIds = new ArrayList<>();
        offer = offerDAO.retrieveOfferById(id);
        for (Item offeredItem : offer.getItemOffered()){
            itemOfferedIds.add(offeredItem.getId());
        }
        offer.setItemOffered(itemDAO.retrieveItemsByIds(itemOfferedIds));
        offer.setItemRequested(itemDAO.retrieveItemById(offer.getItemRequested().getId()));
        return offer;
    }

    public void disputeBarter(BarterBean selectedBarter) {
        //TODO
    }

    public void viewBarterDetails(BarterBean selectedBarter) {
        //TODO
    }
}
