package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectQueries {

    public static ResultSet selectAllAvailableItems(Statement stmt) throws SQLException  {
        String sql = "SELECT * FROM items WHERE status = 'AVAILABLE'";
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectItemById(Statement stmt, int id) throws SQLException  {
        String sql = String.format("SELECT * FROM items WHERE id = \"%d\"", id);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectUserByUsername(Statement stmt, String username) throws SQLException  {
        String sql = String.format("SELECT * FROM users WHERE username = \"%s\"", username);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectImagesByItem(Statement stmt, int id) throws SQLException {
        String sql = String.format("SELECT image FROM images WHERE item = \"%d\"", id);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectItemByUser(Statement stmt, String username) throws SQLException {
        String sql = String.format("SELECT * FROM items WHERE owner =\"%s\"", username);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectOffersByReceiver(Statement stmt, String receiver) throws SQLException {
        String sql = String.format("SELECT * FROM offers WHERE receiver = \"%s\" AND status like 'PENDING'", receiver);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectOffersBySender(Statement stmt, String sender) throws SQLException {
        String sql = String.format("SELECT * FROM offers WHERE offerer = \"%s\" AND status like 'PENDING'", sender);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectItemsOfferedIds(Statement stmt, int id) throws SQLException {
        String sql = String.format("SELECT item FROM offers JOIN offered ON offers.id = offered.offer WHERE offers.id = \"%d\"", id);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectOfferById(Statement stmt, int id) throws SQLException {
        String sql = String.format("SELECT * FROM offers WHERE offers.id = \"%d\"", id);
        return stmt.executeQuery(sql);
    }

    public static List<Integer> selectCollidingOffers(Connection conn, int offerIdToExclude, List<Integer> collidingItemIDs) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < collidingItemIDs.size(); i++) {
            placeholders.append("?");
            if (i < collidingItemIDs.size() - 1) placeholders.append(",");
        }
        String sql =
                "SELECT id as offer_id " +
                "FROM offers " +
                "WHERE status = 'PENDING' AND id <> ? AND itemReq IN (" + placeholders + ") " +
                "UNION " +
                "SELECT offer as offer_id " +
                "FROM offered " +
                "WHERE offer <> ? AND item IN (" + placeholders + ")";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, offerIdToExclude);
            pstmt.setInt(2 + collidingItemIDs.size(), offerIdToExclude);
            int n = 2; //starts from the second ? in the sql string (indexes start at 1, not 0)
            for (int id : collidingItemIDs) {
                pstmt.setInt(n, id);
                n++;
            }
            n++; // for the offer <> ? after the UNION statement
            for (int id : collidingItemIDs) { // 2 times for the placeholders on the second part after UNION
                pstmt.setInt(n, id);
                n++;
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("offer_id"));
                }
            }
        }
        return ids;
    }

    public static ResultSet selectNotificationsByUser(Statement stmt, String username) throws SQLException{
        String sql = String.format("SELECT * FROM notifications WHERE user = \"%s\" AND is_read = 0", username);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectBartersByUser(Statement stmt, String username) throws SQLException{
        String sql = String.format(
                "SELECT * " +
                "FROM barters b JOIN offers o on b.offer = o.id " +
                "WHERE offerer = \"%s\" OR receiver = \"%s\"", username, username);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectBarterByID(Statement stmt, int id) throws SQLException {
        String sql = String.format("SELECT * FROM barters WHERE barters.id = \"%d\"", id);
        return stmt.executeQuery(sql);
    }

    public static ResultSet selectItemsOfferedByOfferId(Statement stmt, int id) throws SQLException {
        String sql = String.format("SELECT item FROM offered WHERE offered.offer = \"%d\"", id);
        return stmt.executeQuery(sql);
    }
}
