package it.uniroma2.ispw.decluttify.bean;

import java.util.List;

public class OfferBean {
    private int id;
    private String receiver;
    private String offerer;
    private PreviewItemBean requestedItem;
    private List<PreviewItemBean> offeredItemList;
    private boolean escrow;
    private boolean shipping;
    private String status;

    public OfferBean(int id, String receiver, String offerer, PreviewItemBean requestedItem, List<PreviewItemBean> offeredItemList, boolean escrow, boolean shipping, String status) {
        this.setId(id);
        this.setReceiver(receiver);
        this.setOfferer(offerer);
        this.setRequestedItem(requestedItem);
        this.setOfferedItemList(offeredItemList);
        this.setEscrow(escrow);
        this.setShipping(shipping);
        this.setStatus(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getOfferer() {
        return offerer;
    }
    public void setOfferer(String offerer) {
        this.offerer = offerer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PreviewItemBean getRequestedItem() {
        return requestedItem;
    }

    public void setRequestedItem(PreviewItemBean requestedItem) {
        this.requestedItem = requestedItem;
    }

    public List<PreviewItemBean> getOfferedItemList() {
        return offeredItemList;
    }

    public void setOfferedItemList(List<PreviewItemBean> offeredItemList) {
        this.offeredItemList = offeredItemList;
    }

    public boolean isEscrow() {
        return escrow;
    }

    public void setEscrow(boolean escrow) {
        this.escrow = escrow;
    }

    public boolean isShipping() {
        return shipping;
    }

    public void setShipping(boolean shipping) {
        this.shipping = shipping;
    }
}
