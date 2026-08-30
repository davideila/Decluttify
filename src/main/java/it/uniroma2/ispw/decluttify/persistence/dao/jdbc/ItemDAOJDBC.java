package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.ItemDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOJDBC extends ItemDAO {

    @Override
    public Item retrieveItemById(int id) {
        Connection connection = PersistenceManager.getInstance().getConnection();

        try (PreparedStatement pstmtItem = connection.prepareStatement(SelectQueries.SELECT_ITEM_BY_ID)) {
            pstmtItem.setInt(1, id);

            try (ResultSet rsItem = pstmtItem.executeQuery()) {
                if (!rsItem.next()) {
                    throw new DAOException("No item found with ID: " + id);
                }

                Item item = fetchItemData(connection, rsItem);

                if (rsItem.next()) {
                    throw new DAOException("Data integrity error: multiple items found for ID " + id);
                }

                return item;
            }
        } catch (SQLException e) {
            throw new DAOException("Database error while retrieving item by ID: " + id, e);
        }
    }

    @Override
    public List<Item> retrieveItemsByIds(List<Integer> itemIDs) {
        List<Item> itemList = new ArrayList<>();
        if (itemIDs == null || itemIDs.isEmpty()) {
            return itemList;
        }

        for (Integer id : itemIDs) {
            itemList.add(retrieveItemById(id));
        }
        return itemList;
    }

    @Override
    public List<Item> retrieveAllAvailableItems() throws DAOException {
        List<Item> itemList = new ArrayList<>();
        Connection connection = PersistenceManager.getInstance().getConnection();

        try (PreparedStatement pstmtItem = connection.prepareStatement(SelectQueries.SELECT_ALL_AVAILABLE_ITEMS);
             ResultSet rsItem = pstmtItem.executeQuery()) {

            while (rsItem.next()) {
                itemList.add(fetchItemData(connection, rsItem));
            }

            if (itemList.isEmpty()) {
                throw new DAOException("No available items found");
            }

        } catch (SQLException e) {
            throw new DAOException("Error fetching available items", e);
        }

        return itemList;
    }

    @Override
    public List<Item> retrieveItemsByOwner(String username) {
        List<Item> items = new ArrayList<>();
        Connection connection = PersistenceManager.getInstance().getConnection();

        try (PreparedStatement pstmtItem = connection.prepareStatement(SelectQueries.SELECT_ITEM_BY_USER)) {
            pstmtItem.setString(1, username);

            try (ResultSet rsItem = pstmtItem.executeQuery()) {
                while (rsItem.next()) {
                    items.add(fetchItemData(connection, rsItem));
                }

                if (items.isEmpty()) {
                    throw new DAOException("No items found for owner: " + username);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching items for owner: " + username, e);
        }

        return items;
    }

    @Override
    public void incrementItemsOfferCounters(List<Integer> iDs) {
        if (PersistenceManager.getInstance().isDemoMode() || iDs == null || iDs.isEmpty()) {
            return;
        }

        try (Connection connection = PersistenceManager.getInstance().getConnection()) {
            if (connection == null) {
                throw new DAOException("Database connection is null.");
            }

            connection.setAutoCommit(false);
            int totalRowsAffected = 0;

            for (Integer i : iDs) {
                totalRowsAffected += UpdateQueries.updateItemNumOffer(connection, i, 1);
            }

            if (totalRowsAffected != iDs.size()) {
                connection.rollback();
                throw new DAOException("Update failed for items with IDs: " + iDs);
            }

            connection.commit();

        } catch (SQLException e) {
            throw new DAOException("Database error while updating offer counter for item " + iDs, e);
        }
    }

    // #################################################################################
    // Private helper methods to eliminate duplication as per sonarqube indication


    private Item fetchItemData(Connection connection, ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String ownerName = rs.getString("owner");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate creationDate = rs.getDate("creationDate").toLocalDate();
        String category = rs.getString("category");
        String condition = rs.getString("condition");
        int numOffers = rs.getInt("numOffers");
        String location = rs.getString("location");
        String status = rs.getString("status");

        User owner = fetchUserByUsername(connection, ownerName);
        List<String> images = fetchImagesByItemId(connection, id);

        return new Item(id, owner, name, description, creationDate,
                category.toUpperCase(), condition.toUpperCase(), numOffers, images, location, status);
    }

    private User fetchUserByUsername(Connection connection, String username) throws SQLException {
        try (PreparedStatement pstmtUser = connection.prepareStatement(SelectQueries.SELECT_USER_BY_USERNAME)) {
            pstmtUser.setString(1, username);
            try (ResultSet rsUser = pstmtUser.executeQuery()) {
                if (!rsUser.next()) {
                    throw new DAOException("Data integrity error: Owner '" + username + "' not found.");
                }
                double rating = rsUser.getDouble("rating");
                String email = rsUser.getString("email");
                return new User(username, null, rating, email);
            }
        }
    }

    private List<String> fetchImagesByItemId(Connection connection, int itemId) throws SQLException {
        List<String> images = new ArrayList<>();
        try (PreparedStatement pstmtImages = connection.prepareStatement(SelectQueries.SELECT_IMAGES_BY_ITEM)) {
            pstmtImages.setInt(1, itemId);
            try (ResultSet rsImages = pstmtImages.executeQuery()) {
                while (rsImages.next()) {
                    images.add(rsImages.getString("image"));
                }
            }
        }
        if (images.isEmpty()) {
            throw new DAOException("Data integrity error: no images found for item with ID: " + itemId);
        }
        return images;
    }

}
