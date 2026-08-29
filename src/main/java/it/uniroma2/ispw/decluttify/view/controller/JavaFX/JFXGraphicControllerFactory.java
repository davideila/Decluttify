package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.FullItemBean;
import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;

public class JFXGraphicControllerFactory {

    private static JFXGraphicControllerFactory me = null;

    protected JFXGraphicControllerFactory(){
    }

    public static synchronized JFXGraphicControllerFactory getInstance(){
        if ( me == null ){
            me = new JFXGraphicControllerFactory();
        }
        return me;
    }

    public JFXGraphicController createJFXGraphicController(JFXViewType viewType, SessionManager sessionManager, Navigator navigator){
        switch (viewType)
        {
            case ITEM_BROWSER: return new JFXItemBrowserController(navigator, sessionManager);
            case MY_OFFERS: return new JFXMyOffersController(navigator, sessionManager);
            case MY_BARTERS: return new JFXMyBartersController(navigator, sessionManager);
            default: throw new IllegalArgumentException("Invalid view type : " + viewType);
        }
    }

    public JFXGraphicController createJFXGraphicController(JFXViewType viewType, SessionManager sessionManager, Navigator navigator, Object data){
        switch (viewType)
        {
            case ITEM_DETAILS: return new JFXItemDetailsController(navigator, sessionManager, (PreviewItemBean) data);
            case OFFER_FORM: return new JFXOfferFormController(navigator, sessionManager, (FullItemBean) data);
            default: throw new IllegalArgumentException("Invalid view type : " + viewType);
        }
    }

}
