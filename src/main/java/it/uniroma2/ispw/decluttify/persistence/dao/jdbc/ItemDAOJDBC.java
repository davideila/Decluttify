package it.uniroma2.ispw.decluttify.persistence.dao.jdbc;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.ItemDAO;
import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class ItemDAOJDBC extends ItemDAO {

    @Override
    public Item retrieveItemById(int id) {
        Item item = null;
        Connection connection = PersistenceManager.getInstance().getConnection();

        try {
            // Get Item data
            String owner;
            String name;
            String description;
            LocalDate creationDate;
            String category;
            String condition;
            String location;
            int numOffers;
            String status;

            try (PreparedStatement pstmtItem = connection.prepareStatement(
                    SelectQueries.SELECT_ITEM_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {

                pstmtItem.setInt(1, id);

                try (ResultSet rsItem = pstmtItem.executeQuery()) {
                    if (!rsItem.first()) {
                        throw new DAOException("No item found with ID: " + id);
                    }
                    if (rsItem.next()) {
                        throw new DAOException("Data integrity error: multiple items found for ID " + id);
                    }
                    rsItem.first();

                    owner = rsItem.getString("owner");
                    name = rsItem.getString("name");
                    description = rsItem.getString("description");
                    creationDate = rsItem.getDate("creationDate").toLocalDate();
                    category = rsItem.getString("category");
                    condition = rsItem.getString("condition");
                    location = rsItem.getString("location");
                    numOffers = rsItem.getInt("numOffers");
                    status = rsItem.getString("status");
                }
            }

            // Get user owner data
            User user;
            try (PreparedStatement pstmtUser = connection.prepareStatement(
                    SelectQueries.SELECT_USER_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {

                pstmtUser.setString(1, owner);

                try (ResultSet rsUser = pstmtUser.executeQuery()) {
                    if (!rsUser.first()) {
                        throw new DAOException("Data integrity error: Owner '" + owner + "' not found for item " + id);
                    }
                    rsUser.first();

                    String username = rsUser.getString("username");
                    double rating = rsUser.getDouble("rating");
                    String email = rsUser.getString("email");
                    user = new User(username, null, rating, email);
                }
            }

            // Get images data
            List<String> images = new ArrayList<>();
            try (PreparedStatement pstmtImages = connection.prepareStatement(
                    SelectQueries.SELECT_IMAGES_BY_ITEM,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {

                pstmtImages.setInt(1, id);

                try (ResultSet rsImages = pstmtImages.executeQuery()) {
                    if (!rsImages.first()) {
                        throw new DAOException("Data integrity error: no image found for item with ID: " + id);
                    }
                    rsImages.first();
                    do {
                        images.add(rsImages.getString("image"));
                    } while (rsImages.next());
                }
            }

            item = new Item(id, user, name, description, creationDate, category.toUpperCase(), condition.toUpperCase(), numOffers, images, location, status);

        } catch (SQLException e) {
            throw new DAOException("Database error while retrieving item by ID: " + id, e);
        }

        if (item == null) {
            throw new DAOException("No item found with ID: " + id);
        }

        return item;
    }

    @Override
    public List<Item> retrieveItemsByIds(List<Integer> itemIDs) {
        List<Item> itemList = new ArrayList<>();
        Connection connection = PersistenceManager.getInstance().getConnection();

        try {
            for (Integer id : itemIDs) {
                // Item data
                String owner;
                String name;
                String description;
                LocalDate creationDate;
                String category;
                String condition;
                String location;
                int numOffers;
                String status;

                try (PreparedStatement pstmtItem = connection.prepareStatement(
                        SelectQueries.SELECT_ITEM_BY_ID,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {

                    pstmtItem.setInt(1, id);

                    try (ResultSet rsItem = pstmtItem.executeQuery()) {
                        if (!rsItem.first()) {
                            throw new DAOException("No item found with ID: " + id);
                        }
                        if (rsItem.next()) {
                            throw new DAOException("Data integrity error: multiple items found for ID " + id);
                        }

                        rsItem.first();
                        owner = rsItem.getString("owner");
                        name = rsItem.getString("name");
                        description = rsItem.getString("description");
                        creationDate = rsItem.getDate("creationDate").toLocalDate();
                        category = rsItem.getString("category");
                        condition = rsItem.getString("condition");
                        location = rsItem.getString("location");
                        numOffers = rsItem.getInt("numOffers");
                        status = rsItem.getString("status");
                    }
                }

                // User owner data
                User user;
                try (PreparedStatement pstmtUser = connection.prepareStatement(
                        SelectQueries.SELECT_USER_BY_USERNAME,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {

                    pstmtUser.setString(1, owner);

                    try (ResultSet rsUser = pstmtUser.executeQuery()) {
                        if (!rsUser.first()) {
                            throw new DAOException("Data integrity error: Owner '" + owner + "' not found for item " + id);
                        }
                        rsUser.first();
                        String username = rsUser.getString("username");
                        double rating = rsUser.getDouble("rating");
                        String email = rsUser.getString("email");
                        user = new User(username, null, rating, email);
                    }
                }

                // Images data
                List<String> images = new ArrayList<>();
                try (PreparedStatement pstmtImages = connection.prepareStatement(
                        SelectQueries.SELECT_IMAGES_BY_ITEM,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {

                    pstmtImages.setInt(1, id);

                    try (ResultSet rsImages = pstmtImages.executeQuery()) {
                        if (!rsImages.first()) {
                            throw new DAOException("Data integrity error: no image found for item with ID: " + id);
                        }
                        rsImages.first();
                        do {
                            images.add(rsImages.getString("image"));
                        } while (rsImages.next());
                    }
                }

                Item item = new Item(id, user, name, description, creationDate,
                        category.toUpperCase(), condition.toUpperCase(), numOffers, images, location, status);
                itemList.add(item);
            }
        } catch (SQLException e) {
            throw new DAOException("Database error while retrieving items", e);
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
                int id = rsItem.getInt("id");
                String owner = rsItem.getString("owner");
                String name = rsItem.getString("name");
                String description = rsItem.getString("description");
                LocalDate creationDate = rsItem.getDate("creationDate").toLocalDate();
                String category = rsItem.getString("category");
                String condition = rsItem.getString("condition");
                int numOffers = rsItem.getInt("numOffers");

                // --- Dati Owner ---
                User user;
                try (PreparedStatement pstmtUser = connection.prepareStatement(SelectQueries.SELECT_USER_BY_USERNAME)) {
                    pstmtUser.setString(1, owner);
                    try (ResultSet rsUser = pstmtUser.executeQuery()) {
                        if (!rsUser.next()) {
                            throw new DAOException("Data integrity error: no owner found for item with ID: " + id);
                        }
                        String username = rsUser.getString("username");
                        double rating = rsUser.getDouble("rating");
                        String email = rsUser.getString("email");
                        user = new User(username, null, rating, email);
                    }
                }

                // --- Dati Immagini ---
                List<String> images = new ArrayList<>();
                try (PreparedStatement pstmtImages = connection.prepareStatement(SelectQueries.SELECT_IMAGES_BY_ITEM)) {
                    pstmtImages.setInt(1, id);
                    try (ResultSet rsImages = pstmtImages.executeQuery()) {
                        while (rsImages.next()) {
                            images.add(rsImages.getString("image"));
                        }
                    }
                }

                if (images.isEmpty()) {
                    throw new DAOException("Data integrity error: no images found for item with ID: " + id);
                }

                Item item = new Item(id, user, name, description, creationDate,
                        category.toUpperCase(), condition.toUpperCase(), numOffers, images);
                itemList.add(item);
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

        try (PreparedStatement pstmtItem = connection.prepareStatement(
                SelectQueries.SELECT_ITEM_BY_USER,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {

            pstmtItem.setString(1, username);

            try (ResultSet rsItem = pstmtItem.executeQuery()) {
                if (!rsItem.first()) {
                    throw new DAOException("No items found for owner: " + username);
                }

                rsItem.first();
                do {
                    int id = rsItem.getInt("id");
                    String owner = rsItem.getString("owner");
                    String name = rsItem.getString("name");
                    String description = rsItem.getString("description");
                    LocalDate creationDate = rsItem.getDate("creationDate").toLocalDate();
                    String category = rsItem.getString("category");
                    String condition = rsItem.getString("condition");
                    int numOffers = rsItem.getInt("numOffers");
                    String location = rsItem.getString("location");
                    String status = rsItem.getString("status");

                    // Images data
                    List<String> images = new ArrayList<>();
                    try (PreparedStatement pstmtImages = connection.prepareStatement(
                            SelectQueries.SELECT_IMAGES_BY_ITEM,
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY)) {

                        pstmtImages.setInt(1, id);

                        try (ResultSet rsImage = pstmtImages.executeQuery()) {
                            if (!rsImage.first()) {
                                throw new DAOException("Data integrity error: no images found for item with ID: " + id);
                            }
                            rsImage.first();
                            do {
                                images.add(rsImage.getString("image"));
                            } while (rsImage.next());
                        }
                    }

                    // Owner data
                    User user;
                    try (PreparedStatement pstmtUser = connection.prepareStatement(
                            SelectQueries.SELECT_USER_BY_USERNAME,
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY)) {

                        pstmtUser.setString(1, owner);

                        try (ResultSet rsUser = pstmtUser.executeQuery()) {
                            if (!rsUser.first()) {
                                throw new DAOException("Data integrity error: no owner found for item with ID: " + id);
                            }
                            rsUser.first();
                            double rating = rsUser.getDouble("rating");
                            String email = rsUser.getString("email");
                            user = new User(username, null, rating, email);
                        }
                    }

                    Item item = new Item(id, user, name, description, creationDate,
                            category.toUpperCase(), condition.toUpperCase(), numOffers, images, location, status);
                    items.add(item);

                } while (rsItem.next());
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

}
