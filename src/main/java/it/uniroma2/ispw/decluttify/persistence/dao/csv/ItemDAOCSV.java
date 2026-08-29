package it.uniroma2.ispw.decluttify.persistence.dao.csv;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.ItemDAO;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOCSV extends ItemDAO {
    private final String ITEMS_FILE_PATH =  PersistenceManager.getInstance().getCSVPathPrefix() + "items.csv";
    private final String IMAGES_FILE_PATH =  PersistenceManager.getInstance().getCSVPathPrefix() + "images.csv";
    private static final Object ITEMS_FILE_LOCK = new Object();

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
            throw new DAOException("Error fetching items by IDs", e);
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
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: items file not found.", e);
        } catch (IOException | NumberFormatException e) {
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
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: images file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error reading images for item ID: " + itemId, e);
        }
        return imgPaths;
    }

    @Override
    public void incrementItemsOfferCounters(List<Integer> iDs) {
        if (PersistenceManager.getInstance().isDemoMode() || iDs == null || iDs.isEmpty()) {
            return;
        }

        synchronized (ITEMS_FILE_LOCK) {
            File originalFile = new File(ITEMS_FILE_PATH);
            File tempFile = new File(ITEMS_FILE_PATH + "_tmp");
            boolean anyUpdated = false;

            try (BufferedReader br = new BufferedReader(new FileReader(originalFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
                // copy header in tmp file
                String line = br.readLine();
                if (line != null) {
                    bw.write(line);
                    bw.newLine();
                }
                // start reading the info by row
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] tmpRow = line.split(";");
                    int currentLineId = Integer.parseInt(tmpRow[0].trim());
                    // Verify ID match
                    boolean isMatch = false;
                    for (Integer id : iDs) {
                        if (id != null && id == currentLineId) {
                            isMatch = true;
                            break;
                        }
                    }
                    if (isMatch) {
                        int currentCounter = Integer.parseInt(tmpRow[7].trim());
                        tmpRow[7] = String.valueOf(currentCounter + 1);
                        // Rebuild row with ; separator for tmp file and with offer counter increment
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < tmpRow.length; i++) {
                            sb.append(tmpRow[i]);
                            if (i < tmpRow.length - 1) {
                                sb.append(";");
                            }
                        }
                        bw.write(sb.toString());
                        anyUpdated = true;
                    } else {
                        bw.write(line);
                    }
                    bw.newLine();
                }
            } catch (FileNotFoundException e) {
                throw new DAOException("Persistence error: items file not found.", e);
            } catch (IOException | NumberFormatException e) {
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                throw new DAOException("Error: cannot access or read items file", e);
            }
            // Overwrite real file
            if (anyUpdated) {
                if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
                    throw new DAOException("Error: cannot replace original file with temp file");
                }
            } else {
                // If no updates, delete temp file
                tempFile.delete();
            }
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
