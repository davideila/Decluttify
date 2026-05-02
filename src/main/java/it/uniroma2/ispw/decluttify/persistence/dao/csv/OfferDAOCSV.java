package it.uniroma2.ispw.decluttify.persistence.dao.csv;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.Item;
import it.uniroma2.ispw.decluttify.model.Offer;
import it.uniroma2.ispw.decluttify.model.OfferStatus;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.OfferDAO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OfferDAOCSV extends OfferDAO {
    private final String OFFER_FILE_PATH = PersistenceManager.getInstance().getCSVPathPrefix() + "offers.csv";
    private final String OFFERED_FILE_PATH = PersistenceManager.getInstance().getCSVPathPrefix() + "offered.csv";

    @Override
    public Offer retrieveOfferById(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(OFFER_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] offerData = line.split(";");
                if (Integer.parseInt(offerData[0]) == id) {
                    return new Offer(
                            Integer.parseInt(offerData[0]),
                            new User(offerData[1], null, 0, null),
                            new User(offerData[2], null, 0, null),
                            this.retrieveOfferedItems(Integer.parseInt(offerData[0])),
                            new Item(Integer.parseInt(offerData[3])),
                            Boolean.parseBoolean(offerData[4]),
                            Boolean.parseBoolean(offerData[5]),
                            OfferStatus.valueOf(offerData[6].toUpperCase())
                    );
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: offer file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching offer data with ID: " + id, e);
        }
        return null;
    }

    private List<Item> retrieveOfferedItems(int offerId) {
        List<Item> offeredItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(OFFERED_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] offeredData = line.split(";");
                if (Integer.parseInt(offeredData[1]) == offerId) {
                    offeredItems.add(new Item(Integer.parseInt(offeredData[0])));
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: items file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching offered items for offer with ID: " + offerId, e);
        }
        return offeredItems;
    }

    @Override
    public void acceptOffer(Offer offer, List<Offer> collidingOffers) {
        String tempOfferFilePath = OFFER_FILE_PATH + "_tmp";
        File tempOfferFile = new File(tempOfferFilePath);
        File originalOfferFile = new File(OFFER_FILE_PATH);
        String ITEM_FILE_PATH = "src/main/resources/it/uniroma2/ispw/decluttify/persistence/items.csv";
        String tempItemFilePath = ITEM_FILE_PATH + "_tmp";
        File tempItemFile = new File(tempItemFilePath);
        File originalItemFile = new File(ITEM_FILE_PATH);

        List<Integer> collidingOffersIds = new ArrayList<>();
        for (Offer collidingOff : collidingOffers) {
            collidingOffersIds.add(collidingOff.getId());
        }
        boolean success = false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempOfferFile));
             BufferedReader br = new BufferedReader(new FileReader(originalOfferFile));
             BufferedWriter bw2 = new BufferedWriter(new FileWriter(tempItemFile));
             BufferedReader br2 = new BufferedReader(new FileReader(originalItemFile))) {
            String line;
            line = br.readLine();
            bw.write(line + "\r\n");
            String[] tmpRow;
            while ((line = br.readLine()) != null) {
                tmpRow = line.split(";");
                if (tmpRow[0].equals(String.valueOf(offer.getId()))) {
                    tmpRow[6] = "ACCEPTED";
                    bw.write(String.join(";",tmpRow) + "\r\n");
                } else if (collidingOffersIds.contains(Integer.parseInt(tmpRow[0]))) { //Invalidate collision
                    tmpRow[6] = "REJECTED";
                    bw.write( String.join(";",tmpRow)+ "\r\n");
                }else{
                    bw.write(line + "\r\n");
                }
            }

            //Update status of accepted offer items = TRADED
            //And update offerCounters of invalidated collisions item
            line = br2.readLine();
            bw2.write(line + "\r\n");
            List<Integer> acceptedItemsIds = new ArrayList<>();
            List<Integer> rejectedItemsIds = new ArrayList<>();
            for (Item itemOffered : offer.getItemOffered()) {
                acceptedItemsIds.add(itemOffered.getId());
            }
            acceptedItemsIds.add(offer.getItemRequested().getId());

            for (Offer collidingOff : collidingOffers) {
                for(Item itemOffered : collidingOff.getItemOffered()) {
                    rejectedItemsIds.add(itemOffered.getId());
                }
                rejectedItemsIds.add(collidingOff.getItemRequested().getId());
            }

            while ((line = br2.readLine()) != null) {
                tmpRow = line.split(";");
                if (acceptedItemsIds.contains(Integer.parseInt(tmpRow[0]))){
                    tmpRow[9] = "TRADED";
                    bw2.write(String.join(";",tmpRow) + "\r\n");
                    success = true;
                } else if (rejectedItemsIds.contains(Integer.parseInt(tmpRow[0]))) {
                    tmpRow[7] = Integer.toString(Integer.parseInt(tmpRow[7]) - 1); //offerCounter --
                    bw2.write( String.join(";",tmpRow)+ "\r\n");
                    success = true;
                }else{
                    bw2.write(line + "\r\n");
                }
            }
        } catch (IOException | NumberFormatException e) {
            if (tempOfferFile.exists()) tempOfferFile.delete();
            if (tempItemFile.exists()) tempItemFile.delete();
            throw new DAOException("Transaction failed: could not update CSV files for offer acceptance.", e);
        }
        if (success) {
            boolean offerRenameOk = originalOfferFile.delete() && tempOfferFile.renameTo(originalOfferFile);
            boolean itemRenameOk = originalItemFile.delete() && tempItemFile.renameTo(originalItemFile);
            if (!offerRenameOk || !itemRenameOk) {
                throw new DAOException("Error: cannot update one or both CSV files (delete/rename failed)");
            }
        }
    }

    @Override
    public List<Offer> retrieveCollidingOffers(Offer offerToExclude) {
        List<Offer> collidingOffers = new ArrayList<>();

        List<Integer> itemIds = new ArrayList<>();
        itemIds.add(offerToExclude.getItemRequested().getId());
        for (Item item : offerToExclude.getItemOffered()) {
            itemIds.add(item.getId());
        }

        for (Integer itemId : itemIds) {
            for (Offer o : this.retrieveOffersByItemReq(itemId)) {
                addIfValid(collidingOffers, o, offerToExclude.getId());
            }

            for (Offer o : this.retrieveOffersByItemOffered(itemId)) {
                addIfValid(collidingOffers, o, offerToExclude.getId());
            }
        }
        return collidingOffers;
    }

    // Utility method for retrieveCollidingOffers (to not include duplicates)
    private void addIfValid(List<Offer> list, Offer newOffer, int excludedId) {
        if (newOffer == null || newOffer.getId() == excludedId || newOffer.getStatus() == OfferStatus.ACCEPTED) {
            return;
        }

        boolean alreadyAdded = false;
        for (Offer existing : list) {
            if (existing.getId() == newOffer.getId()) {
                alreadyAdded = true;
                break;
            }
        }
        if (!alreadyAdded) {
            list.add(newOffer);
        }
    }

    private List<Offer> retrieveOffersByItemOffered(int itemId) {
        List<Offer> offers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(OFFERED_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] offeredData = line.split(";");
                if (Integer.parseInt(offeredData[0]) == itemId) {
                    offers.add(this.retrieveOfferById(Integer.parseInt(offeredData[1])));
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: offers file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching offers involving item with ID: " + itemId, e);
        }
        return offers;
    }

    private List<Offer> retrieveOffersByItemReq(int itemId) {
        List<Offer> offers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(OFFER_FILE_PATH))){
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] offerData = line.split(";");
                if (Integer.parseInt(offerData[3]) == itemId) {
                    offers.add(this.retrieveOfferById(Integer.parseInt(offerData[0])));
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: offers file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching offers involving item with ID: " + itemId, e);
        }
        return offers;
    }


    @Override
    public synchronized void createOffer(Offer offer) {
        File offerFile = new File(OFFER_FILE_PATH);
        File offeredFile = new File(OFFERED_FILE_PATH);
        if (!offerFile.exists() || !offeredFile.exists()) throw new DAOException("Error: offers file does not exist.");
        if ((offerFile.length() == 0 || offeredFile.length() == 0)) throw new DAOException("No header found in offers file.");
        try (RandomAccessFile raf = new RandomAccessFile(offerFile, "rw"); RandomAccessFile raf2 = new RandomAccessFile(offeredFile, "rw")) {
            long fileLength = offerFile.length();
            long pointer = fileLength - 1; //last char of the file
            while (pointer >= 0) {
                raf.seek(pointer);
                int b = raf.read();
                if (b == '\n' && pointer != fileLength - 1) { // '\n' = 10
                    break;
                }
                pointer--;
            }

            int id = 0;
            String line = raf.readLine();
            if (line != null && !line.isEmpty()) {
                try {
                    id = Integer.parseInt(line.split(";")[0].trim());
                } catch (NumberFormatException e) {
                    //no offers, only header
                    id = 0;
                }
            }
            id++;

            StringBuilder newRow = new StringBuilder();
            if (raf.length() > 0) {
                raf.seek(raf.length() - 1);
                if (raf.read() != '\n') {
                    raf.writeBytes("\n");
                }
            }
            newRow.append(id + ";" + offer.getOfferer().getUsername() + ";" + offer.getReceiver().getUsername() + ";" + offer.getItemRequested().getId() + ";" + offer.isEscrowOn() + ";" + offer.isShippingOn() + ";" + offer.getStatus().name() + "\r\n");
            raf.write(newRow.toString().getBytes());

            if (raf2.length() > 0) {
                raf2.seek(raf2.length() - 1);
                if (raf2.read() != '\n') {
                    raf2.writeBytes("\n");
                }
            }
            for (Item itemOffered: offer.getItemOffered()) {
                newRow.setLength(0);
                newRow.append(itemOffered.getId()).append(";").append(id).append("\r\n");
                raf2.write(newRow.toString().getBytes());
            }
        } catch (IOException e) {
            throw new DAOException("Error: Impossible to save new offer in CSV.", e);
        }
    }

    @Override
    public void deleteOffer(int id) {

    }


    @Override
    public void rejectOffer(Offer offer) {

    }

    @Override
    public List<Offer> retrieveOffersByReceiver(String receiver) {
        List<Offer> offers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(OFFER_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] offerData = line.split(";");
                if ((offerData[2]).equals(receiver) && offerData[6].equalsIgnoreCase("PENDING")) {
                    offers.add(new Offer(
                            Integer.parseInt(offerData[0]),
                            new User(offerData[1], null, 0, null),
                            new User(offerData[2], null, 0, null),
                            this.retrieveOfferedItems(Integer.parseInt(offerData[0])),
                            new Item(Integer.parseInt(offerData[3])),
                            Boolean.parseBoolean(offerData[4]),
                            Boolean.parseBoolean(offerData[5]),
                            OfferStatus.valueOf(offerData[6].toUpperCase())
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: offers file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching offers with receiver: " + receiver, e);
        }
        return offers;
    }

    @Override
    public List<Offer> retrieveOffersBySender(String sender) {
        List<Offer> offers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(OFFER_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] offerData = line.split(";");
                if ((offerData[1]).equals(sender) && offerData[6].equalsIgnoreCase("PENDING")) {
                    offers.add(new Offer(
                            Integer.parseInt(offerData[0]),
                            new User(offerData[1], null, 0, null),
                            new User(offerData[2], null, 0, null),
                            this.retrieveOfferedItems(Integer.parseInt(offerData[0])),
                            new Item(Integer.parseInt(offerData[3])),
                            Boolean.parseBoolean(offerData[4]),
                            Boolean.parseBoolean(offerData[5]),
                            OfferStatus.valueOf(offerData[6].toUpperCase())
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: offers file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching offers with sender: " + sender, e);
        }
        return offers;
    }
}
