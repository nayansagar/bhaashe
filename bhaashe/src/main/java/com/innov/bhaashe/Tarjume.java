package com.innov.bhaashe;

import com.innov.bhaashe.data.Input;
import com.innov.bhaashe.utils.Dictionary;
import com.innov.bhaashe.utils.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class Tarjume {

    private Dictionary dictionary;

    private WordSorter wordSorter;

    private Map<String, String> personTenseVerbMapping;

    public Tarjume() throws IOException, URISyntaxException {
        personTenseVerbMapping = FileUtils.getInstance().getMappingFromFile("ptv.txt", "=");
        dictionary = Dictionary.getInstance();
        wordSorter = new WordSorter();
    }

    private String translate(Input input, String sortedInput){
        StringBuilder output = new StringBuilder();
        if(sortedInput != null && !sortedInput.isEmpty()){
            String[] words = sortedInput.split(" ");
            for(String word : words){
                if(word.isEmpty()) continue;
                String equiv = dictionary.get(word);
                if(equiv != null && !equiv.isEmpty()){
                    if(word.equals(input.getVerb())){
                        equiv = addTenseToVerb(equiv, word, input);
                    }
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

    private String addTenseToVerb(String equiv, String word, Input input) {
        equiv += personTenseVerbMapping.get(input.getPerson()+"_"+input.getTense());
        return equiv;
    }

    public String process(Input input) {
        String lastChar = "";
        String sortedSentence = wordSorter.sort(input, lastChar.equals("?"));
        String translatedSentence = translate(input, sortedSentence);
        System.out.println(input.getText() + " | = | " + sortedSentence + " | = | " + translatedSentence + lastChar);
        return translatedSentence;
    }
}
