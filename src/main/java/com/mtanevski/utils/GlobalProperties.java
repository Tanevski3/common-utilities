package com.mtanevski.utils;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * To be utilized throughout the clients or tests wherever a property is meant to be accessed.
 * This will be the single thread-safe source of retrieving properties.
 * Usage example:
 * <pre>
 * GlobalProperties globalProperties = GlobalProperties.getInstance();
 * String aUrl = globalProperties.get("url");
 * </pre>
 * note: GlobalProperties is a thread-safe singleton - it maintains a single instance within execution
 * You can also use it as a property of a class;
 * <pre>
 *  private GlobalProperties globalProperties = GlobalProperties.getInstance();
 *  </pre>
 */
public class GlobalProperties {

    private static GlobalProperties instance;
    private static CompositeConfiguration configuration;

    private GlobalProperties() {
        configuration = new CompositeConfiguration();
        configuration.addConfiguration(new SystemConfiguration());
        configuration.addConfiguration(new EnvironmentConfiguration());
        try {
            configuration.addConfiguration(new PropertiesConfiguration("testing.properties"));
        } catch (ConfigurationException ignored) {
        }

        try {
            configuration.addConfiguration(new PropertiesConfiguration("testing.properties"));
        } catch (ConfigurationException ignored) {
        }
    }


    public static GlobalProperties getInstance() {
        if (instance == null) {
            synchronized (GlobalProperties.class) {
                if (instance == null) {
                    instance = new GlobalProperties();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        return configuration.getString(key);
    }

    public String get(String key, String defaultValue) {
        return configuration.getString(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return configuration.getBoolean(key);
    }

    public synchronized String syncGet(String key) {
        return configuration.getString(key);
    }

}
