package it.uniroma2.ispw.decluttify.persistence.dao.csv;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.model.User;
import it.uniroma2.ispw.decluttify.persistence.dao.UserDAO;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class UserDAOCSV extends UserDAO {
    private final String FILE_PATH =  "src/main/resources/it/uniroma2/ispw/decluttify/persistence/users.csv";
    //getClass().getResource("/it/uniroma2/ispw/decluttify/persistence/users.csv")

    @Override
    public User retrieveUserByUsername(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(";");
                if (userData[0].equals(username)) {
                    return new User(
                            userData[0],
                            userData[1],
                            Double.parseDouble(userData[2]),
                            userData[3]
                    );
                }
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new DAOException("Data corruption: failed to parse user " + username + " from CSV.", e);
        } catch (FileNotFoundException e) {
            throw new DAOException("Persistence error: user database file not found at " + FILE_PATH, e);
        } catch (IOException e) {
            throw new DAOException("System error while reading users CSV file.", e);
        }

        return null;
    }

    @Override
    public void createUser(User user) {

    }

    @Override
    public void deleteUser(String username) {

    }

}
