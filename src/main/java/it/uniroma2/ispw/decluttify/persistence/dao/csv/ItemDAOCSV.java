package it.uniroma2.ispw.decluttify.persistence.dao.csv;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.dao.ItemDAO;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOCSV extends ItemDAO {
    private final String ITEMS_FILE_PATH =  "src/main/resources/it/uniroma2/ispw/decluttify/persistence/items.csv";
    private final String IMAGES_FILE_PATH =  "src/main/resources/it/uniroma2/ispw/decluttify/persistence/images.csv";

    @Override
    public Item retrieveItemById(int itemId) {
        try (BufferedReader br = new BufferedReader(new FileReader(ITEMS_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] itemData = line.split(";");
                if (Integer.parseInt(itemData[0]) == itemId) {
                    return new Item(
                            Integer.parseInt(itemData[0]),
                            new User(itemData[1], null, 0, null),
                            itemData[2],
                            itemData[3],
                            LocalDate.parse(itemData[4]),
                            itemData[5].toUpperCase(),
                            itemData[6].toUpperCase(),
                            Integer.parseInt(itemData[7]),
                            this.retrieveImagesForItem(Integer.parseInt(itemData[0])),
                            itemData[8],
                            itemData[9]
                    );
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: items file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error reading item data for ID: " + itemId, e);
        }
        throw new DAOException("No item found with ID: " + itemId);
    }

    @Override
    public List<Item> retrieveItemsByIds(List<Integer> itemIDs) {
        List<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ITEMS_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] itemData = line.split(";");
                for (int itemId : itemIDs) {
                    if (Integer.parseInt(itemData[0]) == itemId) {
                        items.add(new Item(
                                Integer.parseInt(itemData[0]),
                                new User(itemData[1], null, 0, null),
                                itemData[2],
                                itemData[3],
                                LocalDate.parse(itemData[4]),
                                itemData[5].toUpperCase(),
                                itemData[6].toUpperCase(),
                                Integer.parseInt(itemData[7]),
                                this.retrieveImagesForItem(Integer.parseInt(itemData[0])),
                                itemData[8],
                                itemData[9]
                        ));
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: items file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching items", e);
        }
        return items;
    }

    @Override
    public List<Item> retrieveAllAvailableItems() {
        List<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ITEMS_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] itemData = line.split(";");
                if (itemData[9].equalsIgnoreCase("AVAILABLE")) {
                    items.add(new Item(
                        Integer.parseInt(itemData[0]),
                        new User(itemData[1], null, 0, null),
                        itemData[2],
                        itemData[3],
                        LocalDate.parse(itemData[4]),
                        itemData[5].toUpperCase(),
                        itemData[6].toUpperCase(),
                        Integer.parseInt(itemData[7]),
                        this.retrieveImagesForItem(Integer.parseInt(itemData[0])),
                        itemData[8],
                        itemData[9]
                    ));
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error retrieving available items from CSV.", e);
        }
        return items;
    }

    private List<String> retrieveImagesForItem(int itemId){
        List<String> imgPaths = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(IMAGES_FILE_PATH))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] imgData = line.split(";");
                if (Integer.parseInt(imgData[0]) == itemId) {
                    imgPaths.add(imgData[1]);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Error reading images for item ID: " + itemId, e);
        }
        return imgPaths;
    }

    @Override
    public void createItem(Item item) {
        //TODO
    }

    @Override
    public void deleteItemById(int itemId) {
        //TODO
    }

    @Override
    public void updateItemOfferCounter(int id, int num) {
        String tempFilePath = ITEMS_FILE_PATH + "_tmp";
        File tempFile = new File(tempFilePath);
        File originalFile = new File(ITEMS_FILE_PATH);
        boolean success = false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
             BufferedReader br = new BufferedReader(new FileReader(originalFile))) {
            String line;
            line = br.readLine();
            bw.write(line + "\r\n");
            String[] tmpRow;
            while ((line = br.readLine()) != null) {
                tmpRow = line.split(";");
                if (tmpRow[0].equals(String.valueOf(id))) {
                    tmpRow[7] = String.valueOf(Integer.parseInt(tmpRow[7]) + num);
                    bw.write(String.join(";",tmpRow) + "\r\n");
                    success = true;
                } else {
                    bw.write(line + "\r\n");
                }
            }
        }

        catch (IOException e) {
            if(tempFile.exists()) tempFile.delete();
            throw new DAOException("Error: cannot access to file");
        }
        if (success && (!originalFile.delete() || !tempFile.renameTo(originalFile))) {
            throw new DAOException("Error: cannot delete original file and rename temp file");
        }
    }

    @Override
    public List<Item> retrieveItemsByOwner(String username) {
        List<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ITEMS_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] itemData = line.split(";");
                if (itemData[9].equalsIgnoreCase("AVAILABLE") && itemData[1].equals(username)) {
                    items.add(new Item(
                            Integer.parseInt(itemData[0]),
                            new User(itemData[1], null, 0, null),
                            itemData[2],
                            itemData[3],
                            LocalDate.parse(itemData[4]),
                            itemData[5].toUpperCase(),
                            itemData[6].toUpperCase(),
                            Integer.parseInt(itemData[7]),
                            this.retrieveImagesForItem(Integer.parseInt(itemData[0])),
                            itemData[8],
                            itemData[9]
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: items file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error reading item data for owner: " + username, e);
        }
        return items;
    }
}
