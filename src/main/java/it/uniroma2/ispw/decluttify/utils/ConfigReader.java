package it.uniroma2.ispw.decluttify.utils;

import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance = null;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
            else{
                throw new DecluttifyException("property file 'config.properties' not found in the classpath");
            }
        } catch (IOException e) {
            throw new DecluttifyException("Configuration error: Failed to load 'config.properties'.", e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) instance = new ConfigReader();
        return instance;
    }
    public String getPepper() { return properties.getProperty("pepper");}
    public String getViewType() { return properties.getProperty("view.type", "GUI"); }
    public String getPersistenceType() { return properties.getProperty("persistence.type", "mysql"); }
    public String getDBURL() { return properties.getProperty("db.url"); }
    public String getDBUser() { return properties.getProperty("db.user"); }
    public String getDBPassword() { return properties.getProperty("db.password"); }
    public String getDBDriver() { return properties.getProperty("db.driver"); }
}
