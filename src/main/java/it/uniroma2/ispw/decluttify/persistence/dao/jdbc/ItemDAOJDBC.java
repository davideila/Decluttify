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
    public List<Item> retrieveAllAvailableItems() {

        List<Item>  itemList = new ArrayList<>();

        try (Statement stmtItem = connection.createStatement();
             Statement stmtUser = connection.createStatement();
             Statement stmtImages = connection.createStatement()) {

             ResultSet rsItem = SelectQueries.selectAllAvailableItems(stmtItem);
             if (!rsItem.first()){ // rsItem empty
                 throw new DAOException("No available items found");
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
                String status = rsItem.getString("status");

                // Second statement for owner data
                String username;
                double rating;
                String email;
                ResultSet rsUser = SelectQueries.selectUserByUsername(stmtUser, owner);

                if (!rsUser.first()) { // rs empty
                        throw new DAOException("Data integrity error: no owner found for item with ID:" + id);
                }
                rsUser.first();
                username = rsUser.getString("username");
                rating = rsUser.getDouble("rating");
                email = rsUser.getString("email");

                // Third statement for images data
                List<String> images = new ArrayList<>();
                ResultSet rsImages = SelectQueries.selectImagesByItem(stmtImages, id);
                if (!rsImages.first()) { // rsItem empty
                    throw new DAOException("Data integrity error: no images found for item with ID: " + id);
                }
                rsImages.first();
                do{
                    images.add(rsImages.getString("image"));
                }while(rsImages.next());

                User user = new User(username, null, rating, email);
                Item item = new Item(id, user, name, description, creationDate, category.toUpperCase(), condition.toUpperCase(), numOffers, images);
                itemList.add(item);

            } while(rsItem.next());
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
    public void createItem(Item item) {

    }

    @Override
    public void deleteItemById(int itemId) {

    }

    @Override
    public void updateItemOfferCounter(int id, int num) {
        int rowsAffected;
        try {
            rowsAffected = UpdateQueries.updateItemNumOffer(
                    this.connection,
                    id,
                    num
            );
            if (rowsAffected != 1) {
                throw new DAOException("Update failed: Item with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database error while updating offer counter for item " + id, e);
        }
    }

}
