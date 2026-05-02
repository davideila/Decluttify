package it.uniroma2.ispw.decluttify.persistence.dao.csv;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.*;
import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.persistence.dao.BarterDAO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BarterDAOCSV extends BarterDAO {
    private final String BARTER_FILE_PATH = PersistenceManager.getInstance().getCSVPathPrefix() + "barters.csv";
    private final String OFFER_FILE_PATH = PersistenceManager.getInstance().getCSVPathPrefix() + "offers.csv";

    @Override
    public List<Barter> retrieveBartersByUsername(String username){
        List<Integer> offersIds = new ArrayList<>();
        List<Barter> barters = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(OFFER_FILE_PATH)); BufferedReader br2 = new BufferedReader(new FileReader(BARTER_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] offerData = line.split(";");
                if (offerData[1].equals(username) || offerData[2].equals(username)){
                    offersIds.add(Integer.parseInt(offerData[0]));
                }
            }
            line = br2.readLine();
            while ((line = br2.readLine()) != null) {
                String[] barterData = line.split(";");
                if(offersIds.contains(Integer.parseInt(barterData[0]))){
                    barters.add(new Barter(
                            Integer.parseInt(barterData[0]),
                            Integer.parseInt(barterData[1]),
                            barterData[2],
                            barterData[3],
                            BarterStatus.valueOf(barterData[4].toUpperCase()),
                            Boolean.parseBoolean(barterData[5]),
                            Boolean.parseBoolean(barterData[6])
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: items file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching barters data for user: " + username, e);
        }
        return barters;
    }

    @Override
    public Barter retrieveBarterByID(int id){
        try (BufferedReader br = new BufferedReader(new FileReader(BARTER_FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] barterData = line.split(";");
                if ((Integer.parseInt(barterData[0])) == id) {
                    return new Barter(
                            Integer.parseInt(barterData[0]),
                            Integer.parseInt(barterData[1]),
                            barterData[2],
                            barterData[3],
                            BarterStatus.valueOf(barterData[4].toUpperCase()),
                            Boolean.parseBoolean(barterData[5]),
                            Boolean.parseBoolean(barterData[6])
                    );
                }
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: barters file not found.", e);
        } catch (IOException | NumberFormatException e) {
            throw new DAOException("Error fetching barter data with ID: " + id, e);
        }
        return null;
    }

    @Override
    public void updateBarter(Barter barter) {
        String tempFilePath = BARTER_FILE_PATH + "_tmp";
        File tempFile = new File(tempFilePath);
        File originalFile = new File(BARTER_FILE_PATH);
        boolean found = false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
             BufferedReader br = new BufferedReader(new FileReader(originalFile))) {
            String line;
            line = br.readLine();
            bw.write(line + "\r\n");
            String[] tmpRow;
            while ((line = br.readLine()) != null) {
                tmpRow = line.split(";");
                if (tmpRow[0].equals(String.valueOf(barter.getId()))) {
                    tmpRow[3] = barter.getCompletionDate();
                    tmpRow[4] = barter.getStatus().toString();
                    tmpRow[5] = String.valueOf(barter.isOffererConfirmed());
                    tmpRow[6] = String.valueOf(barter.isReceiverConfirmed());
                    bw.write(String.join(";",tmpRow) + "\r\n");
                    found = true;
                } else {
                    bw.write(line + "\r\n");
                }
            }
        } catch (IOException e) {
            if (tempFile.exists()) tempFile.delete();
            throw new DAOException("Error writing to barter CSV during update.", e);
        }

        if (!found) {
            if (tempFile.exists()) tempFile.delete();
            throw new DAOException("Update failed: Barter with ID " + barter.getId() + " not found in CSV.");
        }

        // Tmp file is the new original file if everything goes well
        if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
            throw new DAOException("Critical error: could not finalize CSV update for Barter ID " + barter.getId());
        }
    }

    @Override
    public void createBarter(Barter barter) {
        File barterFile = new File(BARTER_FILE_PATH);
        if (!barterFile.exists()) throw new DAOException("Persistence error: Barter CSV file does not exist.");
        if (barterFile.length() == 0) throw new DAOException("No header found in barters file.");
        try (RandomAccessFile raf = new RandomAccessFile(barterFile, "rw")) {
            long fileLength = barterFile.length();
            long pointer = fileLength - 1; //last char of the file
            // get the new id for new barter going from end of file to first newline to get last inserted barter id
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
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    // No IDs in file, only header
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
            newRow.append(id).append(";").append(barter.getOffer().getId()).append(";").append(barter.getStartDate()).append(";").append("null").append(";").append(barter.getStatus().name()).append(";").append("false").append(";").append("false").append("\r\n");
            raf.writeBytes(newRow.toString());
        } catch (IOException e) {
            throw new DAOException("Error: Impossible to save new barter in CSV.", e);
        }
    }
}
