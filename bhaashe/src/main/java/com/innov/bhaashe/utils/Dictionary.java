package com.innov.bhaashe.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by 310229986 on 11/15/2016.
 */
public class Dictionary {

    public static final String DICTIONARY_FILE = "dictionary.properties";
    private static Properties properties;
    private static Dictionary instance = new Dictionary();
    private FileUtils fileUtils;

    private Dictionary() {
        try {
            properties = new Properties();
            loadDictionary();
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

    private void loadDictionary() throws IOException {
        properties.load(this.getClass().getClassLoader().getResourceAsStream(DICTIONARY_FILE));
    }

    public String get(String key){
        return properties.getProperty(key.toLowerCase());
    }

    public void addToDictionary(String word) {
        try {
            fileUtils.writeLineToFile(DICTIONARY_FILE, word.toLowerCase());
            synchronized (properties){
                loadDictionary();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
