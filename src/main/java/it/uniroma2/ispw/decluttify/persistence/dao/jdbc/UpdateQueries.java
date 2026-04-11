package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import java.sql.*;

public class UpdateQueries {
    public static int updateOfferStatus(Connection conn, int id, String status) throws SQLException {
        int rowsAffected;
        String sql = "UPDATE offers SET status = ? WHERE id = ? AND status like 'PENDING'"; //not searching only for offer id to prevent any changes to the status have been made (offerer canceled)
                                                                                        // if more statuses are to be implemented in the future, this method might be changed
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //set parameters for prepared statement
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            // Execute and get the ID of new offer
            rowsAffected = pstmt.executeUpdate();
        }
        return rowsAffected;
    }

    public static int updateItemStatus(Connection conn, int itemId, String status) throws SQLException {
        String sql;
        int rowsAffected;
        switch(status) {
            //switch is added to predict any additional item status implementation in the future
            case "TRADED":
                sql = "UPDATE items SET status = ? WHERE id = ? AND status like 'AVAILABLE'";
                break;
            default:
                sql = "UPDATE items SET status = ? WHERE id = ?";
                break;
        }

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, itemId);
            rowsAffected = pstmt.executeUpdate();
        }
        return rowsAffected;
    }

    public static int updateItemNumOffer(Connection conn, int itemId, int op) throws SQLException {
        int rowsAffected;
        String sql ="UPDATE items SET numOffers = numOffers + ? WHERE id = ?";
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

    public static int updateBarter(Connection connection, int id, String completionDate, String status, boolean offererConfirmed, boolean receiverConfirmed) throws SQLException {
        int rowsAffected;
        String sql = "UPDATE barters SET completionDate = ?, status = ?, offererConfirmed = ?, receiverConfirmed = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, completionDate);
            pstmt.setString(2, status);
            pstmt.setBoolean(3, offererConfirmed);
            pstmt.setBoolean(4, receiverConfirmed);
            pstmt.setInt(5, id);
            rowsAffected = pstmt.executeUpdate();
        }
        return rowsAffected;
    }
}
