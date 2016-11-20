package com.innov.bhaashe.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Dictionary {

    public static final String DICTIONARY_FILE = "dictionary.properties";
    private static Map<String, String> properties;
    private static Dictionary instance = new Dictionary();
    private FileUtils fileUtils;

    private Dictionary() {
        try {
            properties = FileUtils.getInstance().getMappingFromFile(DICTIONARY_FILE, "=");
            fileUtils = FileUtils.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Dictionary getInstance(){
        if(instance == null){
            synchronized (instance){
                instance = new Dictionary();
            }
        }
        return instance;
    }

    public String get(String key){
        return properties.get(key.toLowerCase());
    }

    public void addToDictionary(String word) {
        try {
            fileUtils.writeLineToFile(DICTIONARY_FILE, word.toLowerCase());
            synchronized (properties){
                properties = FileUtils.getInstance().getMappingFromFile(DICTIONARY_FILE, "=");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
