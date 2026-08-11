package it.uniroma2.ispw.decluttify.persistence;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.utils.ConfigReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PersistenceManager {

    private static PersistenceManager instance;
    private String persistenceType;
    private boolean testEnvironment = false;
    private boolean demoMode = false;

    private PersistenceManager(){
        switch(ConfigReader.getInstance().getMode()){
            case "TEST", "test":
                this.testEnvironment = true;
                break;
            case "DEMO", "demo":
                this.demoMode = true;
                break;
            case "FULL", "full":
                this.testEnvironment = false;
                this.demoMode = false;
                break;
            case null, default:
                throw new DecluttifyException("Cannot read configuration properties... closing app.");
        }

        switch(ConfigReader.getInstance().getPersistenceType()){
            case "mysql":
                this.persistenceType = "mysql";
                break;
            case "csv":
                this.persistenceType = "csv";
                break;
            case null, default:
                throw new DecluttifyException("Cannot read configuration properties... closing app.");

        }
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

    public Connection getConnection() {
        ConfigReader configReader = ConfigReader.getInstance();

        if (!"mysql".equalsIgnoreCase(persistenceType)) {
            return null;
        }

        try {
            Class.forName(configReader.getDBDriver());

            if (demoMode) {
                return null;
            }
            if (testEnvironment) {
                return DriverManager.getConnection(
                        configReader.getTestDBURL(),
                        configReader.getTestDBUser(),
                        configReader.getTestDBPassword()
                );
            } else {
                return DriverManager.getConnection(
                        configReader.getDBURL(),
                        configReader.getDBUser(),
                        configReader.getDBPassword()
                );
            }
        } catch (ClassNotFoundException e) {
            throw new DAOException("Database driver not found: " + configReader.getDBDriver(), e);
        } catch (SQLException e) {
            throw new DAOException("Failed to establish database connection.", e);
        }
    }

    public String getCSVPathPrefix() {
        return testEnvironment ? "src/test/resources/csv/" : "src/main/resources/it/uniroma2/ispw/decluttify/persistence/";
    }

    public boolean isDemoMode() {
        return this.demoMode;
    }
}