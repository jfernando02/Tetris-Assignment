package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final String CONFIG_FILE_PATH = "src/config/config.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static ConfigData configData;

    static {
        File configFile = new File(CONFIG_FILE_PATH);
        if (configFile.exists()) {
            configData = loadFromFile();
        } else {
            configData = new ConfigData();
            saveConfigData(configData);
        }
    }

    public static ConfigData getConfigData() {
        if (configData == null) {
            configData = new ConfigData();
            saveConfigData(configData);
        }
        return configData;
    }

    public static void saveConfigData(ConfigData configData) {
        ConfigManager.configData = configData;
        try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
            gson.toJson(configData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ConfigData loadFromFile() {
        File configFile = new File(CONFIG_FILE_PATH);
        if (configFile.exists() && configFile.length() > 0) {
            try (FileReader reader = new FileReader(configFile)) {
                return gson.fromJson(reader, ConfigData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; // Return null if file doesn't exist or is empty
    }

    public static void resetConfigData() {
        configData = new ConfigData();
        saveConfigData(configData);
    }
}