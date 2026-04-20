package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.persistence.dao.ItemDAO;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import it.uniroma2.ispw.decluttify.utils.BeanConverter;
import java.util.ArrayList;
import java.util.List;

public class VisualizeItemController {

    //Method for loading (previewed) items from persistence to tile pane in item browse view
    public ArrayList<PreviewItemBean> loadAvailableItems() {
        ArrayList<PreviewItemBean> itemBeans = new ArrayList<>();
        List<Item> items;
        ItemDAO itemDAO = DAOFactory.getDAOFactory().createItemDAO();
        items = itemDAO.retrieveAllAvailableItems();
        PreviewItemBean pib;
        for (Item item : items) {
            pib = BeanConverter.toPreviewItemBean(item);
            itemBeans.add(pib);
        }
        return itemBeans;
    }

   // Method for loading the full detailed item view
   public FullItemBean loadItemDetails(int id) {
       ItemDAO itemDAO = DAOFactory.getDAOFactory().createItemDAO();
       Item item;
       item = itemDAO.retrieveItemById(id);
       FullItemBean fib;
       fib = BeanConverter.toFullItemBean(item);
    return fib;
    }
}
