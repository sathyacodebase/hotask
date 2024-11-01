package com.test.configs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private final String propertyFilePath = "src/test/resources/properties/Configuration.properties";
    private Properties properties;
    private static ConfigFileReader configFileReader;

    public static ConfigFileReader getInstance() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }

    public ConfigFileReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    String getChromeDriverPath() {
        return isMac() ? "/usr/local/bin/chromedriver" : getProperty("chromedriverPath");
    }

    private boolean isMac() {
        return System.getProperty("os.name").contains("Mac");
    }

    public String getFilePath() {
        return getProperty("filePath");
    }

    private String getProperty(String path) {
        return properties.getProperty(path);
    }

    public String getApplicationUrl() {
        String url = properties.getProperty("url");
        if (url != null) {
            return url;
        } else {
            throw new RuntimeException("Application Url not specified in the Configuration.properties file for the Key:url");
        }
    }

    public DriverType getBrowser() {

        String browserName = properties.getProperty("browser");
        if (browserName == null || browserName.equals("chrome")) {
            return DriverType.CHROME;
        } else {
            throw new RuntimeException("Browser Name Key value in Configuration.properties is not matched : " + browserName);
        }
    }

    public EnvironmentType getEnvironment() {
        String environmentName = properties.getProperty("environment");
        if (environmentName == null || environmentName.equalsIgnoreCase("local")) {
            return EnvironmentType.LOCAL;
        } else if (environmentName.equals("remote")) {
            return EnvironmentType.REMOTE;
        } else {
            throw new RuntimeException("Environment Type Key value in Configuration.properties is not matched : " + environmentName);
        }
    }

    public Boolean getBrowserWindowSize() {
        String windowSize = properties.getProperty("windowMaximize");
        if (windowSize != null) {
            return Boolean.valueOf(windowSize);
        }
        return true;
    }
}


