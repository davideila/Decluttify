package it.uniroma2.ispw.decluttify.persistence;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.utils.ConfigReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PersistenceManager {

    private static PersistenceManager instance;
    private Connection connection;
    private String persistenceType;
    private boolean testEnvironment = false;

    private PersistenceManager(){
        //persistence.type can be mysql or csv, read from config.properties, if it is sql create the connection to DB
        this.setConnection();
    }

    //Singleton
    public static PersistenceManager getInstance() {
        if (instance == null) {
            try {
                instance = new PersistenceManager();
            } catch (Exception e) {
                throw new DAOException("Critical error: Could not initialize PersistenceManager.", e);
            }
        }
        return instance;
    }

    public void setConnection() {
        ConfigReader configReader = ConfigReader.getInstance();
        this.persistenceType = configReader.getPersistenceType();
        try{
            if ("mysql".equalsIgnoreCase(persistenceType)) {
                Class.forName(configReader.getDBDriver());
                if(testEnvironment){
                    this.connection = DriverManager.getConnection(
                            configReader.getTestDBURL(),
                            configReader.getTestDBUser(),
                            configReader.getTestDBPassword()
                    );
                }
                else {
                    this.connection = DriverManager.getConnection(
                            configReader.getDBURL(),
                            configReader.getDBUser(),
                            configReader.getDBPassword()
                    );
                }
            }
        } catch (ClassNotFoundException e) {
            throw new DAOException("Database driver not found: " + configReader.getDBDriver(), e);
        } catch (SQLException e) {
            throw new DAOException("Failed to establish database connection.", e);
        }
    }

    public Connection getConnection() {
        if (connection == null && "mysql".equalsIgnoreCase(persistenceType)) {
            throw new DAOException("Database connection is not available.");
        }
        return this.connection;
    }

    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error while closing the database connection: " + e.getMessage());
        }
    }

    public void setupTestEnvironment() {
        this.testEnvironment = true;
        closeConnection();
        this.setConnection();
    }

    public String getCSVPathPrefix() {
        return testEnvironment ? "src/test/resources/csv/" : "src/main/resources/it/uniroma2/ispw/decluttify/persistence/";
    }

}