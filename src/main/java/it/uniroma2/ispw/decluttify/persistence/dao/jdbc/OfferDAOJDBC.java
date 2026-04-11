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

    Connection connection = PersistenceManager.getInstance().getConnection();

    @Override
    public Offer retrieveOfferById(int id) {
        List<Item> itemofflist = new ArrayList<>();
        Offer offer = null;
        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
            ResultSet rs = SelectQueries.selectOfferById(stmt, id);
            if (!rs.first()) { // rs empty
                throw new DAOException("Error fetching offer: no offer found with id: " + id);
            }
            rs.first();
            offer = new Offer(
                rs.getInt("id"),
                new User(rs.getString("offerer"), null, -1, null),
                new User(rs.getString("receiver"), null, -1, null),
                itemofflist,
                new Item(rs.getInt("itemReq")),
                rs.getBoolean("escrow"),
                rs.getBoolean("shipping"),
                OfferStatus.valueOf(rs.getString("status").toUpperCase()));
            rs.close();
        }catch(SQLException e){
            throw new DAOException("Error fetching offer with id: " + id, e);
        }
        return offer;
    }

    @Override
    public void acceptOffer(Offer offer, List<Offer> collidingOffers) {

        try {
            // All the operations have to be atomic = transaction
            this.connection.setAutoCommit(false);
            int rowsAffected;
            rowsAffected = UpdateQueries.updateOfferStatus(connection, offer.getId(), offer.getStatus().name());
            if (rowsAffected == 0) {
                throw new SQLException("Accept failed: Offer ID " + offer.getId() + " not found or status changed.");
            }

            // Update Involved Items
            rowsAffected = UpdateQueries.updateItemStatus(connection, offer.getItemRequested().getId(), offer.getItemRequested().getStatus().name());
            if (rowsAffected == 0) {
                throw new SQLException("Item update failed: Requested Item " + offer.getItemRequested().getId() + " not found or not available.");
            }

            for (Item item : offer.getItemOffered()) {
                rowsAffected = UpdateQueries.updateItemStatus(connection, item.getId(), item.getStatus().name());
                if (rowsAffected == 0) {
                    throw new SQLException("Item update failed: Offered Item " + item.getId() + " not found or not available.");
                }
            }

            //TODO make a method for rejection and reuse that for this part and for rejectOffer of logic controller
            // Invalidate Collision using with the statuses of the items object that were rejected with the method called in model by logic controller
            if (collidingOffers != null && !collidingOffers.isEmpty()) {
                for (Offer coll : collidingOffers) {
                    rowsAffected = UpdateQueries.updateOfferStatus(connection, coll.getId(), coll.getStatus().name());
                    if (rowsAffected == 0) {
                        throw new SQLException("Collision rejection failed: Offer ID " + coll.getId());
                    }
                    rowsAffected = UpdateQueries.updateItemNumOffer(connection, coll.getItemRequested().getId(), -1);
                    if (rowsAffected == 0) {
                        throw new SQLException("Counter update failed for requested item " + coll.getItemRequested().getId());
                    }
                    for (Item item : coll.getItemOffered()) {
                        rowsAffected = UpdateQueries.updateItemNumOffer(connection, item.getId(), -1);
                        if (rowsAffected == 0) {
                            throw new SQLException("Counter update failed for offered item " + item.getId());
                        }
                    }
                }
            }

            this.connection.commit();

        } catch (SQLException e) {
            try {
                if (this.connection != null) this.connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error: Transaction rollback failed.", rollbackEx);
            }
            throw new DAOException("Database error during transaction: Offer acceptance and collision updates failed.", e);
        } finally {
            try {
                if (this.connection != null) this.connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Warning: Could not reset auto-commit.");
            }
        }
    }

    @Override
    public List<Offer> retrieveCollidingOffers(Offer offer) {
        List <Offer> collidingOffers = new ArrayList<>();
        List<Integer> collidingItemsIDs = new ArrayList<>();
        collidingItemsIDs.add(offer.getItemRequested().getId());
        for(Item items: offer.getItemOffered()){
            collidingItemsIDs.add(items.getId());
        }
        try {
            List<Integer> offerIds = SelectQueries.selectCollidingOffers(this.connection, offer.getId(), collidingItemsIDs);
            for (Integer id : offerIds) {
                Offer found = this.retrieveOfferById(id);
                if (found != null) collidingOffers.add(found);
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching colliding offers IDs", e);
        }
        return collidingOffers;
    }

    @Override
    public void createOffer(Offer offer) {
        try {
            // Atomicity (2 operations on 2 different tables)
            this.connection.setAutoCommit(false);

            int generatedId = InsertQueries.insertOffer(
                    this.connection,
                    offer.getOfferer().getUsername(),
                    offer.getReceiver().getUsername(),
                    offer.getItemRequested().getId(),
                    offer.isEscrowOn(),
                    offer.isShippingOn());
            offer.setId(generatedId);

            for (Item item : offer.getItemOffered()) {
                InsertQueries.insertOffered(connection, offer.getId(), item.getId());
            }
            this.connection.commit();

        } catch (SQLException e) {
            try {
                if (this.connection != null) this.connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error: Rollback failed during offer creation.", rollbackEx);
            }
            throw new DAOException("Failed to save offer and its items to database.", e);
        } finally {
            try {
                if (this.connection != null) this.connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Warning: Could not reset auto-commit.");
            }
        }
    }

    @Override
    public void deleteOffer(int id) {

    }

    @Override
    public void rejectOffer(Offer offer) {
        PreparedStatement pstmt = null;
        try {
            //atomicity
            this.connection.setAutoCommit(false);
            int rowsAffected;
            rowsAffected = UpdateQueries.updateOfferStatus(connection, offer.getId(), offer.getStatus().name());
            if (rowsAffected == 0) {
                throw new SQLException("Offer update failed: offer with ID " + offer.getId() + " not found.");
            }
            rowsAffected = UpdateQueries.updateItemNumOffer(connection, offer.getItemRequested().getId(), -1);
            if (rowsAffected == 0) {
                throw new SQLException("Item update failed: Requested Item ID " + offer.getItemRequested().getId() + " not found.");
            }
            for (Item item : offer.getItemOffered()) {
                rowsAffected = UpdateQueries.updateItemNumOffer(connection, item.getId(), -1);
                if (rowsAffected == 0) {
                    throw new SQLException("Item update failed: Offered Item ID " + item.getId() + " not found.");
                }
            }
            this.connection.commit();
        } catch (SQLException e) {
            try {
                if (this.connection != null) this.connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error: rollback failed during offer rejection.", rollbackEx);
            }
            throw new DAOException("Database error during offer rejection. Transaction rolled back.", e);
        } finally {
            try {
                if (this.connection != null) this.connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Warning: Could not reset auto-commit.");
            }
        }
    }

    @Override
    public List<Offer> retrieveOffersByReceiver(String receiver) {
        List<Offer> offerlist = new ArrayList<>();
        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
            ResultSet rs = SelectQueries.selectOffersByReceiver(stmt, receiver);
            rs.first();
            List<Item> itemofflist = new ArrayList<>();
            do{
                offerlist.add(new Offer(
                    rs.getInt("id"),
                    new User(rs.getString("offerer"), null, -1, null),
                    new User(rs.getString("receiver"), null, -1, null),
                    itemofflist,
                    new Item(rs.getInt("itemReq")),
                    rs.getBoolean("escrow"),
                    rs.getBoolean("shipping"),
                    OfferStatus.valueOf(rs.getString("status").toUpperCase())));
            }while(rs.next());
        }catch(SQLException e){
            throw new DAOException("Error fetching offers for receiver: " + receiver, e);
        }
        return offerlist;
    }

    @Override
    public List<Offer> retrieveOffersBySender(String sender) {
        List<Offer> offerlist = new ArrayList<>();
        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
            ResultSet rs = SelectQueries.selectOffersBySender(stmt, sender);
            rs.first();
            List<Item> itemofflist = new ArrayList<>();
            do{
                offerlist.add(new Offer(
                        rs.getInt("id"),
                        new User(rs.getString("offerer"), null, -1, null),
                        new User(rs.getString("receiver"), null, -1, null),
                        itemofflist,
                        new Item(rs.getInt("itemReq")),
                        rs.getBoolean("escrow"),
                        rs.getBoolean("shipping"),
                        OfferStatus.valueOf(rs.getString("status").toUpperCase())));
            }while(rs.next());
        }catch(SQLException e){
            throw new DAOException("Error fetching offers for sender: " + sender, e);
        }
        return offerlist;
    }

}
