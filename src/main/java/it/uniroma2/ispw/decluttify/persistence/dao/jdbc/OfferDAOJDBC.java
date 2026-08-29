package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.model.Offer;
import it.uniroma2.ispw.decluttify.model.OfferStatus;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.OfferDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferDAOJDBC extends OfferDAO {

    @Override
    public List<Offer> retrievePendingOffersByPartners(String offerer, String receiver) {
        List<Offer> offerlist = new ArrayList<>();

        Connection connection = PersistenceManager.getInstance().getConnection();
        try (Statement stmt = connection.createStatement();
             Statement stmtItems = connection.createStatement()) {

            ResultSet rs = SelectQueries.selectPendingOffersByPartners(stmt, offerer, receiver);
            while (rs.next()) {
                List<Item> itemofflist = new ArrayList<>();
                int offID = rs.getInt("id");
                try (ResultSet rsItems = SelectQueries.selectItemsOfferedByOfferId(stmtItems, offID)) {
                    while (rsItems.next()) {
                        itemofflist.add(new Item(rsItems.getInt("item")));
                    }
                }

                offerlist.add(new Offer(
                        offID,
                        new User(rs.getString("offerer"), null, -1, null),
                        new User(rs.getString("receiver"), null, -1, null),
                        itemofflist,
                        new Item(rs.getInt("itemReq")),
                        rs.getBoolean("escrow"),
                        rs.getBoolean("shipping"),
                        OfferStatus.valueOf(rs.getString("status").toUpperCase())
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching offers between offerer " + offerer + " and receiver " + receiver, e);
        }
        return offerlist;
    }

    @Override
    public void createOffer(Offer offer) {
        if (PersistenceManager.getInstance().isDemoMode()){
            return;
        }

        Connection connection = PersistenceManager.getInstance().getConnection();
        try {
            // Atomicity (2 operations on 2 different tables)
            connection.setAutoCommit(false);

            int generatedId = InsertQueries.insertOffer(
                    connection,
                    offer.getOfferer().getUsername(),
                    offer.getReceiver().getUsername(),
                    offer.getItemRequested().getId(),
                    offer.isEscrowOn(),
                    offer.isShippingOn());
            offer.setId(generatedId);

            for (Item item : offer.getItemOffered()) {
                InsertQueries.insertOffered(connection, offer.getId(), item.getId());
            }
            connection.commit();

        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error: Rollback failed during offer creation.", rollbackEx);
            }
            throw new DAOException("Failed to save offer and its items to database.", e);
        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DAOException("Database error: Could not reset auto-commit.", e);
            }
        }
    }

    @Override
    public List<Offer> retrieveOffersByReceiver(String receiver) throws DAOException {
        List<Offer> offerlist = new ArrayList<>();

        Connection connection = PersistenceManager.getInstance().getConnection();
        try (Statement stmt = connection.createStatement();
             Statement stmtItems = connection.createStatement()) {
            ResultSet rs = SelectQueries.selectOffersByReceiver(stmt, receiver);
            while (rs.next()) {
                List<Item> itemofflist = new ArrayList<>();
                int offID = rs.getInt("id");
                try (ResultSet rsItems = SelectQueries.selectItemsOfferedByOfferId(stmtItems, offID)) {
                    while (rsItems.next()) {
                        itemofflist.add(new Item(rsItems.getInt("item")));
                    }
                }
                offerlist.add(new Offer(
                        offID,
                        new User(rs.getString("offerer"), null, -1, null),
                        new User(rs.getString("receiver"), null, -1, null),
                        itemofflist,
                        new Item(rs.getInt("itemReq")),
                        rs.getBoolean("escrow"),
                        rs.getBoolean("shipping"),
                        OfferStatus.valueOf(rs.getString("status").toUpperCase())
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching offers for receiver " + receiver, e);
        }
        return offerlist;
    }

    @Override
    public List<Offer> retrieveOffersBySender(String sender) throws DAOException {
        List<Offer> offerlist = new ArrayList<>();

        Connection connection = PersistenceManager.getInstance().getConnection();
        try (Statement stmt = connection.createStatement();
             Statement stmtItems = connection.createStatement()) {

            ResultSet rs = SelectQueries.selectOffersBySender(stmt, sender);
            while (rs.next()) {
                List<Item> itemofflist = new ArrayList<>();
                int offID = rs.getInt("id");
                try (ResultSet rsItems = SelectQueries.selectItemsOfferedByOfferId(stmtItems, offID)) {
                    while (rsItems.next()) {
                        itemofflist.add(new Item(rsItems.getInt("item")));
                    }
                }

                offerlist.add(new Offer(
                        offID,
                        new User(rs.getString("offerer"), null, -1, null),
                        new User(rs.getString("receiver"), null, -1, null),
                        itemofflist,
                        new Item(rs.getInt("itemReq")),
                        rs.getBoolean("escrow"),
                        rs.getBoolean("shipping"),
                        OfferStatus.valueOf(rs.getString("status").toUpperCase())
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching offers for sender " + sender, e);
        }
        return offerlist;
    }

}
