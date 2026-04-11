package it.uniroma2.ispw.decluttify.utils;

import it.uniroma2.ispw.decluttify.bean.*;
import it.uniroma2.ispw.decluttify.model.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BeanConverter {

    private BeanConverter() {} // non-instantiability with a private constructor

    public static UserBean toUserBean(User user) {
        return new UserBean(user.getUsername(), user.getRating());
    }

    public static PreviewItemBean toPreviewItemBean(Item item) {
        PreviewItemBean ib = new PreviewItemBean();
        //TODO
        ib.setId(item.getId());
        ib.setName(item.getName());
        ib.setDescription(item.getDescription());
        ib.setCategory(item.getCategory());
        ib.setCondition(item.getCondition());
        ib.setOwner(item.getOwner().getUsername());
        ib.setImages(item.getImages());
        return ib;
    }

    public static FullItemBean toFullItemBean(Item item){
        FullItemBean ib = new FullItemBean();
        ib.setId(item.getId());
        ib.setName(item.getName());
        ib.setDescription(item.getDescription());
        ib.setCategory(item.getCategory());
        ib.setCondition(item.getCondition());
        ib.setOwner(item.getOwner().getUsername());
        ib.setImages(item.getImages());
        ib.setCreationDate(item.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ib.setLocation(item.getLocation());
        ib.setNumOffers(item.getOffersCounter());
        return ib;
    }

    public static List<PreviewItemBean> toPreviewItemBeanList(List<Item> items){
        List<PreviewItemBean> ibs = new ArrayList<>();
        for (Item item : items) {
            ibs.add(toFullItemBean(item));
        }
        return ibs;
    }

    public static OfferBean toOfferBean(Offer offer) {
        return new OfferBean(offer.getId(),
                offer.getReceiver().getUsername(),
                offer.getOfferer().getUsername(),
                toPreviewItemBean(offer.getItemRequested()),
                toPreviewItemBeanList(offer.getItemOffered()),
                offer.isEscrowOn(),
                offer.isShippingOn(),
                offer.getStatus().name());
    }

    public static List<NotificationBean> toNotificationBeanList(List<Notification> notifications) {
        List<NotificationBean> ibs = new ArrayList<>();
        for (Notification notification : notifications) {
            ibs.add(new NotificationBean(
                    notification.getId(),
                    notification.getMessage(),
                    notification.getType(),
                    notification.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                    notification.isRead()
                    ));
        }
        return ibs;
    }

    public static List<BarterBean> toBarterBeanList(List<Barter> barters, String currentUser) {
        List<BarterBean> ibs = new ArrayList<>();
        for(Barter barter : barters){
            ibs.add(toBarterBean(barter, currentUser));
        }
        return ibs;
    }

    public static BarterBean toBarterBean(Barter barter, String currentUser) {
        BarterBean bean = new BarterBean();
        Offer offer = barter.getOffer();

        bean.setId(barter.getId());
        bean.setStartDate(barter.getStartDate());
        bean.setStatus(barter.getStatus().toString());
        bean.setCompletionDate(barter.getCompletionDate());
        bean.setEscrow(offer.isEscrowOn());
        bean.setShipping(offer.isShippingOn());

        String partnerName;
        List<String> partnerItems;
        List<String> myItems;
        List<String> offeredItemNames = new ArrayList<>();

        for (Item item : offer.getItemOffered()) {
            offeredItemNames.add(item.getName());
        }
        String targetItemName = offer.getItemRequested().getName();

        if (offer.getOfferer().getUsername().equals(currentUser)) {
            partnerName = offer.getReceiver().getUsername();
            myItems = offeredItemNames;
            partnerItems = new ArrayList<>();
            partnerItems.add(targetItemName);
            bean.setPartnerConfirmed(barter.isReceiverConfirmed());
            bean.setYouConfirmed(barter.isOffererConfirmed());
        } else {
            partnerName = offer.getOfferer().getUsername();
            partnerItems = offeredItemNames;
            myItems = new ArrayList<>();
            myItems.add(targetItemName);
            bean.setPartnerConfirmed(barter.isOffererConfirmed());
            bean.setYouConfirmed(barter.isReceiverConfirmed());
        }

        bean.setPartnerName(partnerName);
        bean.setPartnerItems(partnerItems);
        bean.setMyItems(myItems);

        return bean;
    }
}
