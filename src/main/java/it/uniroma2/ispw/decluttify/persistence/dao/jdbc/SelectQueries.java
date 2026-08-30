package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

public final class SelectQueries {
    private SelectQueries() {
        // private constructor for non instantiability
    }

    public static final String SELECT_ALL_AVAILABLE_ITEMS = "SELECT * FROM items WHERE status = 'AVAILABLE'";
    public static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    public static final String SELECT_ITEM_BY_ID = "SELECT * FROM items WHERE id = ?";
    public static final String SELECT_IMAGES_BY_ITEM = "SELECT image FROM images WHERE item = ?";
    public static final String SELECT_ITEM_BY_USER = "SELECT * FROM items WHERE owner = ?";
    public static final String SELECT_OFFERS_BY_RECEIVER = "SELECT * FROM offers WHERE receiver = ? AND status LIKE 'PENDING'";
    public static final String SELECT_OFFERS_BY_SENDER = "SELECT * FROM offers WHERE offerer = ? AND status LIKE 'PENDING'";
    public static final String SELECT_NOTIFICATIONS_BY_USER = "SELECT * FROM notifications WHERE user = ? AND is_read = 0";
    public static final String SELECT_ITEMS_OFFERED_BY_OFFER_ID = "SELECT item FROM offered WHERE offer = ?";
    public static final String SELECT_PENDING_OFFERS_BY_PARTNERS = "SELECT * FROM offers WHERE offerer = ? AND receiver = ?";
}

