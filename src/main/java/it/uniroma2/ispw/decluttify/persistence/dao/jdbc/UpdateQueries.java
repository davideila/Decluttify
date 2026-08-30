package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import java.sql.*;

public class UpdateQueries {

    private UpdateQueries(){}

    public static int updateItemNumOffer(Connection conn, int itemId, int op) throws SQLException {
        int rowsAffected;
        String sql ="UPDATE items SET numOffers = numOffers + ? WHERE id = ? AND status like 'AVAILABLE'";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, op);
            pstmt.setInt(2, itemId);
            rowsAffected = pstmt.executeUpdate();
        }
        return rowsAffected;
    }

    public static int updateNotification(Connection conn, int id) throws SQLException {
        int rowsAffected;
        String sql = "UPDATE notifications SET is_read = true WHERE id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            rowsAffected = pstmt.executeUpdate();
        }
        return rowsAffected;
    }

}
