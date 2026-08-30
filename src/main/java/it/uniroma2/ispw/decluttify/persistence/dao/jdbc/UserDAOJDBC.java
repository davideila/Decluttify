package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.UserDAO;
import java.sql.*;

public class UserDAOJDBC extends UserDAO {

    @Override
    public User retrieveUserByUsername(String username) {
        User user = null;
        Connection conn = PersistenceManager.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(
                SelectQueries.SELECT_USER_BY_USERNAME,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.first()) {
                    return null;
                }
                if (rs.next()) {
                    throw new DAOException("Data integrity error: duplicate username found for '" + username + "'.");
                }
                rs.first();
                double rating = rs.getDouble("rating");
                String email = rs.getString("email");
                String password = rs.getString("password");
                user = new User(username, password, rating, email);
            }

        } catch (SQLException e) {
            throw new DAOException("Database error while retrieving user: " + username, e);
        }
        return user;
    }

}
