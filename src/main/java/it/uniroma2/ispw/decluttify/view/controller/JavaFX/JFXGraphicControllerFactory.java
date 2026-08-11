package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.bean.PreviewItemBean;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;

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

    public JFXGraphicController createJFXGraphicController(ViewType viewType, SessionManager sessionManager, Navigator navigator) throws Exception{
        switch (viewType)
        {
            case ITEM_BROWSER: return new JFXItemBrowserController(navigator, sessionManager);
            case LOGIN: return new JFXLoginPopupController(navigator, sessionManager);
            case MY_OFFERS: return new JFXMyOffersController(navigator, sessionManager);
            case MY_BARTERS: return new JFXMyBartersController(navigator, sessionManager);
            default: throw new Exception("Invalid view type : " + viewType);
        }
    }

    public JFXGraphicController createJFXGraphicController(ViewType viewType, SessionManager sessionManager, Navigator navigator, Object data) throws Exception{
        switch (viewType)
        {
            case ITEM_DETAILS: return new JFXItemDetailsController(navigator, sessionManager, (PreviewItemBean) data);
            case OFFER_FORM: return new JFXOfferFormController(navigator, sessionManager, (PreviewItemBean) data);
            default: throw new Exception("Invalid view type : " + viewType);
        }
    }

}
