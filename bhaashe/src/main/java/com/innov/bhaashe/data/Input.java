package com.innov.bhaashe.data;

public class Input {

    private static final String[] questionWords = {"why", "what", "when", "how", "who", "where", "would", "could", "should", "will", "shall"};

    private String text;

    private String person;

    private String tense;

    private boolean question;

    private String verb;

    public Input(String text) {
        this.text = text;
        setPerson();
        setTense();
        setQuestion();
        guessVerb();
    }

    private void guessVerb() {
        String[] words = text.split(" ");
        for(String word : words){
            if(word.endsWith("ed") || word.endsWith("ing"))
        }
    }

    private void setQuestion() {
        for(String word : questionWords){
            if(text.startsWith(word)){
                question = true;
                break;
            }
        }
        question = false;
    }

    private void setPerson(){
        if(text.contains("i") || text.contains("we")){
            person = "first";
        }

        if(text.contains("you")){
            person = "second";
        }

        if(text.contains("he") || text.contains("she") || text.contains("they")){
            person = "third";
        }
    }

    private void setTense(){
        if(text.contains("is") || text.contains("are") || text.contains("am") || text.contains(" have been") || text.contains("has been")){
            tense = "present";
        }

        if(text.contains("was") || text.contains("were")){
            tense = "past";
        }

        if(text.contains("will") || text.contains("would") || text.contains("shall")){
            tense = "future";
        }
    }

    public String getText() {
        return text;
    }

    public String getPerson() {
        return person;
    }

    public String getTense() {
        return tense;
    }

    public boolean isQuestion() {
        return question;
    }

}
