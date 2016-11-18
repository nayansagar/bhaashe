package com.innov.bhaashe;

import com.innov.bhaashe.data.Input;
import com.innov.bhaashe.utils.*;
import com.innov.bhaashe.utils.Dictionary;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created by Sagar on 11/10/2016.
 */
public class Tarjume {

    private Dictionary dictionary;

    private WordSorter wordSorter;

    public Tarjume() throws IOException, URISyntaxException {
        dictionary = Dictionary.getInstance();
        wordSorter = new WordSorter();
    }

    private String translate(String input){
        StringBuilder output = new StringBuilder();
        if(input != null && !input.isEmpty()){
            String[] words = input.split(" ");
            for(String word : words){
                if(word.isEmpty()) continue;
                String equiv = dictionary.get(word);
                if(equiv != null && !equiv.isEmpty()){
                    output.append(equiv);
                }else {
                    dictionary.addToDictionary(word);
                    output.append(word);
                }
                output.append(" ");
            }
        }
        return output.toString();
    }

    public String process(Input input) {
        String lastChar = "";
        String inputText = input.getText();
        if(inputText.endsWith(".") || inputText.endsWith("?") || inputText.endsWith("!")){
            lastChar = inputText.substring(inputText.length() - 1);
            inputText = input.getText().substring(0, input.getText().length() - 1);
        }
        String sortedSentence = wordSorter.sort(inputText, lastChar.equals("?"));
        String translatedSentence = translate(sortedSentence);
        System.out.println(input + " | = | " + sortedSentence + " | = | " + translatedSentence + lastChar);
        return translatedSentence;
    }
}
