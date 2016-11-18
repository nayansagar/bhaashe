package com.innov.bhaashe;

public class WordSorter {

    private static final String[] peopleWords =
            {"i", "me", "my", "mine",
             "you", "your", "yourself",
             "he", "him", "his",
             "she", "her", "hers",
             "it","that","this","they","those"
    };

    private static final String[] questionWords = {"why", "what", "when", "how", "who", "where"};

    public String sort(String sentence, boolean isQuestion){
        sentence = sentence.toLowerCase();
        sentence = sortPeopleWords(sentence);
        if(isQuestion || startsWithQuestionWord(sentence)){
            sentence = sortQuestionWords(sentence);
        }
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
