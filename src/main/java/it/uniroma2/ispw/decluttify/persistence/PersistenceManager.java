package it.uniroma2.ispw.decluttify.persistence;

import it.uniroma2.ispw.decluttify.exception.ConfigurationException;
import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.utils.ConfigReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PersistenceManager {

    private static final String MYSQL_TYPE = "mysql";
    private static final String CSV_TYPE = "csv";
    private static PersistenceManager instance;
    private String persistenceType;
    private boolean testEnvironment = false;
    private boolean demoMode = false;

    private PersistenceManager() {
        String mode = ConfigReader.getInstance().getMode();

        if (mode == null) {
            throw new ConfigurationException("Configuration mode is null. Please check properties file.");
        }

        switch (mode.toUpperCase().trim()) {
            case "TEST":
                this.testEnvironment = true;
                break;
            case "DEMO":
                this.demoMode = true;
                break;
            case "FULL":
                this.testEnvironment = false;
                this.demoMode = false;
                break;
            default:
                throw new ConfigurationException("Unsupported application mode in configuration: " + mode);
        }

        String type = ConfigReader.getInstance().getPersistenceType();

        if (type == null) {
            throw new ConfigurationException("Persistence type is null. Please check properties file.");
        }

        switch (type.toLowerCase().trim()) {
            case MYSQL_TYPE:
                this.persistenceType = MYSQL_TYPE;
                break;

            case CSV_TYPE:
                this.persistenceType = CSV_TYPE;
                break;

            default:
                throw new ConfigurationException("Unsupported persistence type in configuration: " + type);
        }
    }

    // Singleton
    public static PersistenceManager getInstance() {
        if (instance == null) {
            instance = new PersistenceManager();
        }
        return instance;
    }

    public Connection getConnection() {
        ConfigReader configReader = ConfigReader.getInstance();

        if (!(MYSQL_TYPE.equalsIgnoreCase(persistenceType))) {
            throw new ConfigurationException("Persistence type is not set to a value supported for a connection.");
        }

        try {
            Class.forName(configReader.getDBDriver());

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
            throw new ConfigurationException("Database driver not found: " + configReader.getDBDriver(), e);
        } catch (SQLException e) {
            throw new DecluttifyException("Failed to establish database connection.", e);
        }
    }

    public String getCSVPathPrefix() {
        return testEnvironment ? "src/test/resources/csv/" : "src/main/resources/it/uniroma2/ispw/decluttify/persistence/";
    }

    public boolean isDemoMode() {
        return this.demoMode;
    }

    public void setupTestEnvironment() {
        this.testEnvironment = true;
        this.demoMode = false;
    }
}