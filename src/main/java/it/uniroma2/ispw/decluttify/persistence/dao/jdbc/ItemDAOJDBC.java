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

    Connection connection = PersistenceManager.getInstance().getConnection();

    @Override
    public Item retrieveItemById(int id) {
        Item item = null;

        try (Statement stmtItem = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             Statement stmtUser = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             Statement stmtImages = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            // first statement --> get item data with id
            ResultSet rsItem = SelectQueries.selectItemById(stmtItem, id);

            if (!rsItem.first()) { // rs empty
                throw new DAOException("No item found with ID: "+ id);
            }
            if (rsItem.next()) {
                    throw new DAOException("Data integrity error: multiple items found for ID " + id);
            }
            rsItem.first();

            String owner = rsItem.getString("owner");
            String name = rsItem.getString("name");
            String description = rsItem.getString("description");
            LocalDate creationDate = rsItem.getDate("creationDate").toLocalDate();
            String category = rsItem.getString("category");
            String condition = rsItem.getString("condition");
            String location = rsItem.getString("location");
            int numOffers = rsItem.getInt("numOffers");
            String status = rsItem.getString("status");

            // Second statement --> get item owner user info
            String username;
            double rating;
            String email;

            ResultSet rsUser = SelectQueries.selectUserByUsername(stmtUser, owner);
            if (!rsUser.first()) { // rs empty
                throw new DAOException("Data integrity error: Owner '" + owner + "' not found for item " + id);
            }
            rsUser.first();

            username = rsUser.getString("username");
            rating = rsUser.getDouble("rating");
            email = rsUser.getString("email");
            User user = new User(username, null, rating, email);

            // Third statement --> get the images of the item
            ArrayList<String> images = new ArrayList<>();
            ResultSet rsImages = SelectQueries.selectImagesByItem(stmtImages, id);

            if (!rsImages.first()) { // rs empty
                throw new DAOException("Data integrity error: no image found for item with ID: " + id);
            }
            rsImages.first();
            do{
                images.add(rsImages.getString("image"));
            }while(rsImages.next());

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
        Item item;
        ArrayList<Item> itemList = new ArrayList<>();

        try (Statement stmtItem = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             Statement stmtUser = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             Statement stmtImages = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){

            for (Integer id : itemIDs) {
                //First statement get Item attributes
                ResultSet rsItem = SelectQueries.selectItemById(stmtItem, id);
                if (!rsItem.first()) { // rs empty
                    throw new DAOException("No item found with ID: "+ id);
                }
                if (rsItem.next()) {
                    throw new DAOException("Data integrity error: multiple items found for ID " + id);
                }

                rsItem.first();
                String owner = rsItem.getString("owner");
                String name = rsItem.getString("name");
                String description = rsItem.getString("description");
                LocalDate creationDate = rsItem.getDate("creationDate").toLocalDate();
                String category = rsItem.getString("category");
                String condition = rsItem.getString("condition");
                String location = rsItem.getString("location");
                int numOffers = rsItem.getInt("numOffers");
                String status = rsItem.getString("status");

                // Second statement --> get owner user info
                String username;
                double rating;
                String email;
                ResultSet rsUser = SelectQueries.selectUserByUsername(stmtUser, owner);
                if (!rsUser.first()) { // rs empty
                    throw new DAOException("Data integrity error: Owner '" + owner + "' not found for item " + id);
                }
                rsUser.first();
                username = rsUser.getString("username");
                rating = rsUser.getDouble("rating");
                email = rsUser.getString("email");
                User user = new User(username, null, rating, email);

                // Third statement --> get the images of the selected item
                ArrayList<String> images = new ArrayList<>();
                ResultSet rsImages = SelectQueries.selectImagesByItem(stmtImages, id);
                if (!rsImages.first()) { // rs empty
                    throw new DAOException("Data integrity error: no image found for item with ID: " + id);
                }
                rsImages.first();
                do {
                    images.add(rsImages.getString("image"));
                } while (rsImages.next());

                item = new Item(id, user, name, description, creationDate, category.toUpperCase(), condition.toUpperCase(), numOffers, images, location, status);
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

        try (Statement stmtItem = connection.createStatement();
             Statement stmtUser = connection.createStatement();
             Statement stmtImages = connection.createStatement()) {

            ResultSet rsItem = SelectQueries.selectAllAvailableItems(stmtItem);

            // Usiamo il classico while (rsItem.next()) invece di .first() e do-while
            while (rsItem.next()) {
                int id = rsItem.getInt("id");
                String owner = rsItem.getString("owner");
                String name = rsItem.getString("name");
                String description = rsItem.getString("description");
                LocalDate creationDate = rsItem.getDate("creationDate").toLocalDate();
                String category = rsItem.getString("category");
                String condition = rsItem.getString("condition");
                int numOffers = rsItem.getInt("numOffers");
                String status = rsItem.getString("status");

                // --- Dati Owner ---
                ResultSet rsUser = SelectQueries.selectUserByUsername(stmtUser, owner);
                if (!rsUser.next()) { // Avanza al primo record; se false, non esiste l'utente
                    throw new DAOException("Data integrity error: no owner found for item with ID: " + id);
                }
                String username = rsUser.getString("username");
                double rating = rsUser.getDouble("rating");
                String email = rsUser.getString("email");

                // --- Dati Immagini ---
                List<String> images = new ArrayList<>();
                ResultSet rsImages = SelectQueries.selectImagesByItem(stmtImages, id);

                while (rsImages.next()) {
                    images.add(rsImages.getString("image"));
                }

                // Se l'item DEVE avere per forza almeno un'immagine:
                if (images.isEmpty()) {
                    throw new DAOException("Data integrity error: no images found for item with ID: " + id);
                }

                User user = new User(username, null, rating, email);
                Item item = new Item(id, user, name, description, creationDate,
                        category.toUpperCase(), condition.toUpperCase(), numOffers, images);
                itemList.add(item);
            }

            // Se non è stato trovato alcun item disponibile
            if (itemList.isEmpty()) {
                throw new DAOException("No available items found");
            }

        } catch (SQLException e) {
            throw new DAOException("Error fetching available items", e);
        }

        return itemList;
    }

    public List<Item> retrieveItemsByOwner(String username) {

        ArrayList<Item> items = new ArrayList<>();

        try(Statement stmtItem = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Statement stmtUser = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Statement stmtImages = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){

            ResultSet rsItem = SelectQueries.selectItemByUser(stmtItem, username);

            if (!rsItem.first()){ // rs empty
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

                //second statement for images
                ArrayList<String> images = new ArrayList<>();
                ResultSet rsImage = SelectQueries.selectImagesByItem(stmtImages, id);
                if (!rsImage.first()) { // rs empty
                    throw new DAOException("Data integrity error: no images found for item with ID: " + id);
                }
                rsImage.first();
                do{
                    images.add(rsImage.getString("image"));
                }while(rsImage.next());

                //third statement for owner data
                double rating;
                String email;
                ResultSet rsUser = SelectQueries.selectUserByUsername(stmtUser, owner);
                if (!rsUser.first()) { // rs empty
                    throw new DAOException("Data integrity error: no owner found for item with ID: " + id);
                }
                rsUser.first();
                rating = rsUser.getDouble("rating");
                email = rsUser.getString("email");

                User user = new User(username, null, rating, email);
                Item item = new Item(id, user, name, description, creationDate, category.toUpperCase(), condition.toUpperCase(), numOffers, images, location, status);
                items.add(item);

            } while(rsItem.next());

            rsItem.close();
        } catch (SQLException e) {
            throw new DAOException("Error fetching items for owner: " + username, e);
        }
        return items;
    }

    @Override
    public void incrementItemsOfferCounters(List<Integer> iDs) {
        if (PersistenceManager.getInstance().isDemoMode()){
            return;
        }
        if (iDs == null || iDs.isEmpty()) {
            return;
        }

        int totalRowsAffected = 0;
        try {
            connection.setAutoCommit(false);
            for (Integer i : iDs) {
                int rowsAffected = UpdateQueries.updateItemNumOffer(
                        this.connection,
                        i,
                        1
                );
                totalRowsAffected += rowsAffected;
            }
            if (totalRowsAffected != iDs.size()) {
                connection.rollback();
                throw new DAOException("Update failed for items with IDs: " + iDs);
            } else {
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DAOException("Database error while updating offer counter for item " + iDs, e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
