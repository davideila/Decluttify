package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.persistence.dao.ItemDAO;
import it.uniroma2.ispw.decluttify.persistence.dao.factory.DAOFactory;
import it.uniroma2.ispw.decluttify.utils.BeanConverter;
import java.util.ArrayList;
import java.util.List;

public class VisualizeItemController {

    private final ItemDAO itemDAO;

    public VisualizeItemController() {
        this.itemDAO = DAOFactory.getDAOFactory().createItemDAO();
    }

    //Method for loading (previewed) items from persistence to tile pane in item browse view
    public ArrayList<PreviewItemBean> loadAvailableItems() {
        ArrayList<PreviewItemBean> itemBeans = new ArrayList<>();
        List<Item> items;
        try {
            items = itemDAO.retrieveAllAvailableItems();
            PreviewItemBean pib;
            for (Item item : items) {
                pib = BeanConverter.toPreviewItemBean(item);
                itemBeans.add(pib);
            }
        }catch(DAOException exception){
            throw new DecluttifyException("Unable to load available items due to a system error", exception);
        }
        return itemBeans;
    }

   // Method for loading the full detailed item view
   public FullItemBean loadItemDetails(PreviewItemBean pib) {
       Item item;
       try {
           item = itemDAO.retrieveItemById(pib.getId());
       }catch(DAOException exception){
           throw new DecluttifyException("Unable to load item data due to a system error", exception);
       }
       FullItemBean fib;
       fib = BeanConverter.toFullItemBean(item);
    return fib;
    }
}
