package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.UserDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAOJDBC extends UserDAO {

    Connection connection = PersistenceManager.getInstance().getConnection();

    @Override
    public User retrieveUserByUsername(String username) {
        User user = null;
        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = SelectQueries.selectUserByUsername(stmt, username);

            if (!rs.first()) { // rs empty
                throw new DAOException("No user found with username: " + username);
            }
            if (rs.next()) {
                throw new DAOException("Data integrity error: duplicate username found for '" + username + "'.");
            }
            rs.first();
            double rating = rs.getDouble("rating");
            String email = rs.getString("email");
            String password = rs.getString("password");
            user = new User(username, password, rating, email);
        } catch (SQLException e) {
            throw new DAOException("Database error while retrieving user: " + username, e);
        }
        return user;
    }

        @Override
    public void createUser(User user) {
        //TODO
    }

    @Override
    public void deleteUser(String username) {

    }

}
