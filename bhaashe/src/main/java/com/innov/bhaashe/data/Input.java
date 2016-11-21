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
        for(int i=0; i<words.length; i++){
            String word = words[i];
            if(word.endsWith("ing")){
                if(text.contains("are you "+word) || text.contains("are we "+word) || text.contains("am i "+word)
                        || text.contains("are they "+word) || text.contains("is it "+word) ||text.contains("did it "+word)
                        || text.contains("does it "+word) || text.contains("is "+word) || text.contains("been "+word)  || text.contains("was "+word)
                        || text.contains("were "+word) || text.contains("you are "+word)
                        || text.contains("we are "+word) || text.contains("i am "+word) || text.contains("i was "+word)
                        || text.contains("they are "+word) || text.contains("it is "+word)){
                    verb = word.substring(0, word.indexOf("ing"));
                    return;
                }
            }

            if(word.endsWith("ed")){
                if(text.contains("would have "+word) || text.contains("should have "+word) || text.contains("might have "+word)
                        || text.contains("must have "+word) || text.contains("could have "+word) || text.contains("has "+word)
                        || text.contains("i "+word) || text.contains("he "+word) || text.contains("she "+word)
                        || text.contains("they "+word) || text.contains("it "+word)){
                    verb = word.substring(0, word.indexOf("ed"));
                    return;
                }
            }
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

    public String getVerb() {
        return verb;
    }
}
