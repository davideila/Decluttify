package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Barter;
import it.uniroma2.ispw.decluttify.model.BarterStatus;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.BarterDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BarterDAOJDBC extends BarterDAO {

    Connection connection = PersistenceManager.getInstance().getConnection();

    @Override
    public List<Barter> retrieveBartersByUsername(String username) {
        List<Barter> barters = new ArrayList<>();
        try(Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
            ResultSet rs = SelectQueries.selectBartersByUser(stmt, username);
            while(rs.next()){
                barters.add(new Barter(
                        rs.getInt("id"),
                        rs.getInt("offer"),
                        rs.getString("startDate"),
                        rs.getString("completionDate"),
                        BarterStatus.valueOf(rs.getString("status")),
                        rs.getBoolean("offererConfirmed"),
                        rs.getBoolean("receiverConfirmed")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error retrieving barters for user: " + username, e);
        }
        return barters;
    }

    @Override
    public Barter retrieveBarterByID(int id) {
        Barter barter = null;
        try(Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
            ResultSet rs = SelectQueries.selectBarterByID(stmt, id);
            while(rs.next()){
                barter = new Barter(
                        rs.getInt("id"),
                        rs.getInt("offer"),
                        rs.getString("startDate"),
                        rs.getString("completionDate"),
                        BarterStatus.valueOf(rs.getString("status")),
                        rs.getBoolean("offererConfirmed"),
                        rs.getBoolean("receiverConfirmed")
                );
            }
        } catch (SQLException e) {
            throw new DAOException("Error retrieving barter with ID: " + id, e);
        }
        return barter;
    }

    @Override
    public void createBarter(Barter barter) {
        try {
            int geratedId = InsertQueries.insertBarter(
                    this.connection,
                    barter.getOffer().getId(),
                    barter.getStartDate());
            barter.setId(geratedId);
        } catch (SQLException e) {
            throw new DAOException("Database error while creating barter.", e);
        }
    }

    @Override
    public void updateBarter(Barter barter) {
        int rowsAffected;
        try {
            rowsAffected = UpdateQueries.updateBarter(
                    this.connection,
                    barter.getId(),
                    barter.getCompletionDate(),
                    barter.getStatus().toString().toUpperCase(),
                    barter.isOffererConfirmed(),
                    barter.isReceiverConfirmed()
            );
            if (rowsAffected == 0) {
                throw new DAOException("Update failed: barter with ID " + barter.getId() + " not found.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database error while updating barter status.", e);
        }
    }
}
