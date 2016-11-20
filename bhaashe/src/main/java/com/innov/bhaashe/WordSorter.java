package com.innov.bhaashe;

import com.innov.bhaashe.data.Input;
import org.apache.commons.lang3.StringUtils;

public class WordSorter {

    private static final String[] peopleWords =
            {"i", "me", "my", "mine",
             "you", "your", "yourself",
             "he", "him", "his",
             "she", "her", "hers",
             "it","that","this","they","those"
    };

    private static final String[] questionWords = {"why", "what", "when", "how", "who", "where"};

    public String sort(Input input, boolean isQuestion){
        String sentence = input.getText().toLowerCase();
        sentence = sortPeopleWords(sentence);
        if(isQuestion || startsWithQuestionWord(sentence)){
            sentence = sortQuestionWords(sentence);
        }
        sentence = sortVerb(sentence, input);
        return sentence;
    }

    private String sortVerb(String sentence, Input input) {
        if(StringUtils.isEmpty(input.getVerb())){
            return sentence;
        }
        sentence = sentence.replace(input.getVerb(), "");
        sentence = sentence + " " + input.getVerb();
        return sentence;
    }

    private boolean startsWithQuestionWord(String sentence) {
        for(String s : questionWords){
            if(sentence.startsWith(s)){
                return true;
            }
        }
        return false;
    }

    private String sortPeopleWords(String sentence) {
        String containedPeopleWords = "";
        for(String s : peopleWords){
            String newSentence = containsWholeWord(sentence, s);
            if(!newSentence.equals(sentence)){
                containedPeopleWords += (s + " ");
                sentence = newSentence;
            }
        }
        return containedPeopleWords + " " + sentence;
    }

    private String sortQuestionWords(String sentence) {
        String containedQuestionWords = "";
        for(String s : questionWords){
            String newSentence = containsWholeWord(sentence, s);
            if(!newSentence.equals(sentence)){
                containedQuestionWords += (s + " ");
                sentence = newSentence;
            }
        }
        return sentence + " " + containedQuestionWords;
    }

    private String containsWholeWord(String sentence, String word){
        sentence = sentence.replaceAll("\\b"+word+"\\b", " ");
        return sentence;
    }
}
