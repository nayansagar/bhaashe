package com.innov.bhaashe.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class FileUtils {

    private Map<String, FileWriter> fileWriters = new HashMap<String, FileWriter>();
    private static FileUtils instance = new FileUtils();

    private FileUtils(){}

    public static FileUtils getInstance(){
        if(instance == null){
            synchronized (instance){
                instance = new FileUtils();
            }
        }
        return instance;
    }

    public List<String> readLinesFromFile(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream(fileName));
        List<String> inputs = new ArrayList<String>();
        while (scanner.hasNextLine()){
            inputs.add(scanner.nextLine());
        }
        return inputs;
    }

    public Map<String, String> getMappingFromFile(String fileName, String separator) throws FileNotFoundException {
        List<String> fileLines = readLinesFromFile(fileName);
        Map<String, String> mappings = new HashMap<String, String>();
        for(String line : fileLines){
            if(line.contains(separator)){
                String[] splits = line.split(separator);
                if(splits.length == 1){
                    mappings.put(splits[0], "");
                }else{
                    mappings.put(splits[0], splits[1]);
                }
            }
        }
        return mappings;
    }

    public void writeLineToFile(String fileName, String text){
        try {
            FileWriter fw = getFileWriter(fileName);
            fw.append(text + "=\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private FileWriter getFileWriter(String fileName) throws URISyntaxException, IOException {
        if(fileWriters.get(fileName) != null){
            return fileWriters.get(fileName);
        }

        URL url = this.getClass().getClassLoader().getResource(fileName);
        File f = new File(url.toURI().getPath());
        FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);

        fileWriters.put(fileName, fw);
        return fw;
    }
}
