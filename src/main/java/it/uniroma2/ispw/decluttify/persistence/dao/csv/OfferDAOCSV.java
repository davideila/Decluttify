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
    public List<Offer> retrievePendingOffersByPartners(String offerer, String receiver) {
        List<Offer> offers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(OFFER_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] offerData = line.split(";");
                if ((offerData[2]).equals(receiver) && offerData[6].equalsIgnoreCase("PENDING") && offerData[1].equals(offerer)) {
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
    public synchronized void createOffer(Offer offer) {
        if (PersistenceManager.getInstance().isDemoMode()){
            return;
        }
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
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error: Impossible to save new offer in CSV.", e);
        }
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
