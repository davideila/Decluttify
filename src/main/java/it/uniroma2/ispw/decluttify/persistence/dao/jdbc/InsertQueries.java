package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import java.sql.*;


public class InsertQueries {

    public static int insertOffer(Connection conn, String receiver, String offerer, int itemReq, boolean escrow, boolean shipping) throws SQLException {
        String sql = "INSERT INTO offers (offerer, receiver, itemReq, escrow, shipping, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, receiver);
            pstmt.setString(2, offerer);
            pstmt.setInt(3, itemReq);
            pstmt.setBoolean(4, escrow);
            pstmt.setBoolean(5, shipping);
            pstmt.setString(6, "Pending");

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating offer failed, no ID obtained.");
                }
            }
        }
    }

    public static void insertOffered (Connection conn, int offerId, int itemId) throws SQLException {
        String sql = "INSERT INTO offered (item, offer) VALUES (? , ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.setInt(2, offerId);
            pstmt.executeUpdate();
        }
    }

    public static int insertBarter(Connection conn,  int offerId, String startDate) throws SQLException {
        String sql = "INSERT INTO barters (offer, startDate, status) VALUES (?, ?, ?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            //set parameters for prepared statement
            pstmt.setInt(1, offerId);
            pstmt.setString(2, startDate);
            pstmt.setString(3, "ONGOING");

            // Execute and get the ID of new barter
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating barter failed, no ID obtained.");
                }
            }
        }
    }

    public static int insertNotification(Connection conn, String username, String message, String type) throws SQLException {
        String sql = "INSERT INTO notifications (user, message, type) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, message);
            pstmt.setString(3, type);

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating notification failed, no ID obtained.");
                }
            }
        }
    }
}
