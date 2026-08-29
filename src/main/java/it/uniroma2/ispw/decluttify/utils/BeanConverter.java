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
        PreviewItemBean ib = new PreviewItemBean(item.getId(), item.getName(), item.getDescription(), item.getOwner().getUsername(),
                item.getImages().getFirst(), item.getCategory(), item.getCondition(), item.getOffersCounter());
        return ib;
    }

    public static FullItemBean toFullItemBean(Item item){
        FullItemBean ib = new FullItemBean(toPreviewItemBean(item));
        ib.setImages(item.getImages());
        ib.setCreationDate(item.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ib.setLocation(item.getLocation());
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

}
