package com.innov.bhaashe;

import com.innov.bhaashe.data.Clause;
import com.innov.bhaashe.utils.Dictionary;
import com.innov.bhaashe.utils.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class DefaultTranslator {

    private Dictionary dictionary;

    private WordSorter wordSorter;

    private Map<String, String> personTenseVerbMapping;

    private static DefaultTranslator instance;

    private DefaultTranslator() throws IOException, URISyntaxException {
        personTenseVerbMapping = FileUtils.getInstance().getMappingFromFile("ptv.txt", "=");
        dictionary = Dictionary.getInstance();
        wordSorter = new WordSorter();
    }

    public static DefaultTranslator getTranslator(){
        if(instance == null){
            try {
                instance = new DefaultTranslator();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private String translate(Clause clause){
        StringBuilder output = new StringBuilder();
        String sortedInput = clause.getRearrangedText();
        if(sortedInput != null && !sortedInput.isEmpty()){
            String[] words = sortedInput.split(" ");
            for(String word : words){
                if(word.isEmpty()) continue;
                String equiv = dictionary.get(word);
                if(equiv != null && !equiv.isEmpty()){
                    if(word.equals(clause.getVerb())){
                        equiv = addTenseToVerb(equiv, word, clause);
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

    private String addTenseToVerb(String equiv, String word, Clause input) {
        equiv += personTenseVerbMapping.get(input.getPerson()+"_"+input.getTense());
        return equiv;
    }

    public String process(Clause clause) {
        String lastChar = "";
        String translatedSentence = translate(clause);
        clause.setTranslatedText(translatedSentence);
        //System.out.println(clause.toString() + " | = | " + clause.getRearrangedText() + " | = | " + translatedSentence + lastChar);
        return translatedSentence;
    }

}
