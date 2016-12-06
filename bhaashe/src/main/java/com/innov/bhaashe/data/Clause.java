package com.innov.bhaashe.data;

import com.innov.bhaashe.DefaultTranslator;
import com.innov.bhaashe.PhraseTranslator;
import com.innov.bhaashe.utils.WordCollections;

import java.util.Arrays;

/**
 * Created by Sagar on 11/26/2016.
 */
public class Clause {

    private String text;
    private String subject = "";
    private String directObject = "";
    private String indirectObject = "";
    private String verb;
    private String tense;
    private String person;
    private String remainingPhrase = "";
    private boolean isQuestion;
    private String rearrangedText = "";
    private String translatedText = "";

    private int subjectIndex = -1;
    private int verbIndex = -1;
    private int directObjectIndex = -1;
    private int indirectObjectIndex = -1;
    private String[] splits;

    private DefaultTranslator defaultTranslator = DefaultTranslator.getTranslator();
    private PhraseTranslator phraseTranslator = PhraseTranslator.getInstance();

    public Clause(String text) {
        this.text = text.toLowerCase();
        analyze();
    }

    public void analyze(){
        splits = text.split(" ");
        isQuestionSentence();
        detectSubject();
        detectVerb();
        detectObjects();
        detectTense();
        detectPerson();
        detectRemainingPhrase();
        rearrangeText();
        translate();
    }

    private void translate() {
        /*Candidate candidate = phraseTranslator.process(this);
        if(candidate != null){
            translatedText = candidate.getTranslation();
            return;
        }*/

        defaultTranslator.process(this);
    }

    private void rearrangeText() {
        rearrangedText = subject;
        rearrangedText += " " + sortText(indirectObject);
        rearrangedText += " " + sortText(directObject);
        rearrangedText += " " + sortText(remainingPhrase);
        if(isQuestion){
            rearrangedText += " " + splits[0];
        }
        rearrangedText += " " + verb;
    }

    private String sortText(String remainingText) {
        String[] remSplits = remainingText.split(" ");
        String sortedText = "";
        int limit = remSplits.length - 1;
        for(int i= limit;i>=0;i--){
            if(remSplits[i].equals("of") || remSplits[i].equals("on") || remSplits[i].equals("with")
                    || remSplits[i].equals("to") || remSplits[i].equals("for") || remSplits[i].equals("in")
                    || remSplits[i].equals("at")){
                for(int j=i+1; j<=limit; j++){
                    sortedText += remSplits[j]+" ";
                }
                sortedText += remSplits[i]+" ";
                limit = i-1;
            }
        }

        if(limit >= 0){
            if(sortedText.isEmpty()){
                return remainingText;
            }

            for(int i=limit; i>=0; i--){
                sortedText += remSplits[i]+" ";
            }
        }
        return sortedText;
    }

    private void detectRemainingPhrase() {
        int lastIndex = verbIndex;
        if(directObjectIndex > lastIndex) lastIndex = directObjectIndex;
        if(indirectObjectIndex > lastIndex) lastIndex = indirectObjectIndex;

        for(int i=lastIndex + 1; i<splits.length; i++){
            remainingPhrase += splits[i]+" ";
        }

    }

    private void detectPerson(){
        if(subject.equals("i") || subject.equals("we")){
            person = "first";
        }

        if(subject.equals("you")){
            person = "second";
        }

        if(subject.equals("he") || subject.equals("she") || subject.equals("they")){
            person = "third";
        }
    }

    private void detectTense() {
        String tenseWord = "";
        if(isQuestion){
            tenseWord = splits[subjectIndex - 1];
        }else {
            tenseWord = splits[subjectIndex + 1];
        }

        if(tenseWord.equals("is") || tenseWord.equals("are") || tenseWord.equals("am") || tenseWord.equals(" have been") || tenseWord.equals("has been")){
            tense = "present";
        }

        if(tenseWord.equals("was") || tenseWord.equals("were")){
            tense = "past";
        }

        if(tenseWord.equals("will") || tenseWord.equals("would") || tenseWord.equals("shall")){
            tense = "future";
        }
    }

    private void detectObjects() {
        if(verbIndex == splits.length -1){
            return;
        }

        for(String word : WordCollections.indirectObjects){
            if(word.equals(splits[verbIndex+1])){
                indirectObjectIndex = verbIndex+1;
                indirectObject = splits[verbIndex+1]+" ";
                for(int i= verbIndex+2; i<splits.length; i++){
                    directObjectIndex = i;
                    directObject += splits[i]+" ";
                }
                return;
            }
        }

        for(int i=verbIndex+1; i<splits.length; i++){
            if("to".equals(splits[i]) || "for".equals(splits[i]) || "with".equals(splits[i])){
                for(int j = verbIndex+1; j<i; j++){
                    directObjectIndex = j;
                    directObject += splits[j]+" ";
                }

                for(int j=i; j<splits.length; j++){
                    indirectObjectIndex = j;
                    indirectObject += splits[j]+" ";
                }
            }
        }

    }

    private void detectVerb() {
        if(isQuestion){
            if(splits.length > subjectIndex+1 && "been".equals(splits[subjectIndex+1])){
                if(splits.length > subjectIndex+2){
                    verb = splits[subjectIndex+2];
                    verbIndex = subjectIndex+2;
                    return;
                }else {
                    verb = splits[subjectIndex+1];
                    verbIndex = subjectIndex+1;
                    return;
                }
            }else {
                verb = splits[subjectIndex+1];
                verbIndex = subjectIndex+1;
                return;
            }
        }else{
            if(splits.length > subjectIndex+3){
                String next = splits[subjectIndex + 1];
                String nextToNext = splits[subjectIndex + 2];
                String phrase = next+" "+nextToNext;
                if("have been".equals(phrase) || "has been".equals(phrase) || "had been".equals(phrase)
                        || "would have".equals(phrase) || "could have".equals(phrase) || "should have".equals(phrase)
                        || "must have".equals(phrase) || "can not".equals(phrase) || "could not".equals(phrase)
                        || "would not".equals(phrase) || "should not".equals(phrase) || "has not".equals(phrase)
                        || "had not".equals(phrase) || "have not".equals(phrase)){
                    if(splits[subjectIndex + 3].endsWith("ly") && subjectIndex + 4 < splits.length){
                        verb = splits[subjectIndex + 3] + " " + splits[subjectIndex + 4];
                        verbIndex = subjectIndex+4;
                        return;
                    }else if(subjectIndex + 4 < splits.length && splits[subjectIndex + 4].endsWith("ly")){
                        verb = splits[subjectIndex + 4] + " " + splits[subjectIndex + 3];
                        verbIndex = subjectIndex+4;
                        return;
                    }else{
                        verb = splits[subjectIndex + 3];
                        verbIndex = subjectIndex+3;
                        return;
                    }
                }
            }
            if(splits.length > subjectIndex+2){
                String next = splits[subjectIndex + 1];
                if( "am".equals(next) || "is".equals(next) || "are".equals(next)
                        || "was".equals(next) || "were".equals(next)
                        || "will".equals(next) || "would".equals(next) || "should".equals(next) || "could".equals(next)
                        || "won't".equals(next) || "wouldn't".equals(next) || "shouldn't".equals(next)
                        || "couldn't".equals(next) || "can't".equals(next) || "never".equals(next)){
                    if(splits[subjectIndex + 1].endsWith("ly") && subjectIndex + 2 < splits.length){
                        verb = splits[subjectIndex + 1] + " " + splits[subjectIndex + 2];
                        verbIndex = subjectIndex+2;
                        return;
                    }else if(subjectIndex + 2 < splits.length && splits[subjectIndex + 2].endsWith("ly")){
                        verb = splits[subjectIndex + 2] + " " + splits[subjectIndex + 1];
                        verbIndex = subjectIndex+2;
                        return;
                    }else{
                        verb = splits[subjectIndex + 2];
                        verbIndex = subjectIndex+2;
                        return;
                    }
                }
            }
            if(splits[subjectIndex + 1].endsWith("ly") && subjectIndex + 2 < splits.length){
                verb = splits[subjectIndex + 1] + " " + splits[subjectIndex + 2];
                verbIndex = subjectIndex+2;
                return;
            }else if(subjectIndex + 2 < splits.length && splits[subjectIndex + 2].endsWith("ly")){
                verb = splits[subjectIndex + 2] + " " + splits[subjectIndex + 1];
                verbIndex = subjectIndex+2;
                return;
            }else{
                verb = splits[subjectIndex + 1];
                verbIndex = subjectIndex+1;
                return;
            }
        }
    }

    private void detectSubject() {
        if(isQuestion){
            for(String word : WordCollections.politeQuestionWords) {
                if (text.startsWith(word)) {
                    subject = text.split(" ")[1];
                    subjectIndex = 1;
                    return;
                }
            }
            for(int i = 1; i<splits.length; i++){
                if(Arrays.asList(WordCollections.tenseWords).contains(splits[i])){
                    if( (splits.length > i + 2) && ( "a".equals(splits[i+1]) || "an".equals(splits[i+1])) ||
                            "the".equals(splits[i+1])){
                        for(int j=i+1; j+1<splits.length; j++){
                            String nextWord = splits[j] + " " + splits[j+1];
                            if("have been".equals(nextWord) || "has been".equals(nextWord) || "had been".equals(nextWord)
                                    || "would have".equals(nextWord) || "could have".equals(nextWord) || "should have".equals(nextWord)
                                    || "must have".equals(nextWord) || "can not".equals(nextWord) || "could not".equals(nextWord)
                                    || "would not".equals(nextWord) || "should not".equals(nextWord) || "has not".equals(nextWord)
                                    || "had not".equals(nextWord) || "have not".equals(nextWord)){
                                subjectIndex = j;
                                break;
                            }
                            nextWord = splits[j];
                            if("was".equals(nextWord)
                                    || "were".equals(nextWord) || "will".equals(nextWord) || "would".equals(nextWord)
                                    || "should".equals(nextWord) || "could".equals(nextWord) || "won't".equals(nextWord)
                                    || "wouldn't".equals(nextWord) || "shouldn't".equals(nextWord) || "couldn't".equals(nextWord)
                                    || "can't".equals(nextWord) || "never".equals(nextWord)){
                                subjectIndex = j;
                                break;
                            }
                            subject = subject +" "+ splits[j];
                            subjectIndex = j;
                        }
                    }else{
                        subject = splits[i+1];
                        subjectIndex = i+1;
                        return;
                    }
                }
            }
        }

        if("the".equalsIgnoreCase(splits[0]) || "a".equalsIgnoreCase(splits[0]) || "an".equalsIgnoreCase(splits[0])
                || "my".equalsIgnoreCase(splits[0]) || "our".equalsIgnoreCase(splits[0]) || "their".equalsIgnoreCase(splits[0])
                || "his".equalsIgnoreCase(splits[0]) || "her".equalsIgnoreCase(splits[0]) ){
            for(int j=0; j<splits.length; j++){
                String nextWord = splits[j] + (j+1 < splits.length ? (" " + splits[j+1]) : "");
                if("have been".equals(nextWord) || "has been".equals(nextWord) || "had been".equals(nextWord)
                        || "would have".equals(nextWord) || "could have".equals(nextWord) || "should have".equals(nextWord)
                        || "must have".equals(nextWord) || "can not".equals(nextWord) || "could not".equals(nextWord)
                        || "would not".equals(nextWord) || "should not".equals(nextWord) || "has not".equals(nextWord)
                        || "had not".equals(nextWord) || "have not".equals(nextWord)){
                    subjectIndex = j;
                    break;
                }
                nextWord = splits[j];
                if("was".equals(nextWord)
                        || "were".equals(nextWord) || "will".equals(nextWord) || "would".equals(nextWord)
                        || "should".equals(nextWord) || "could".equals(nextWord) || "won't".equals(nextWord)
                        || "wouldn't".equals(nextWord) || "shouldn't".equals(nextWord) || "couldn't".equals(nextWord)
                        || "can't".equals(nextWord) || "never".equals(nextWord)){
                    subjectIndex = j;
                    break;
                }
                subject = subject +" "+ splits[j];
                subjectIndex = j;
            }
            if(subject.trim().equals(text.trim())){
                subject = splits[0]+" "+splits[1];
                subjectIndex = 1;
            }
        }
        if(subject.isEmpty()){
            subject = splits[0]; //sentences like "here you are", "there you go" not handled
            subjectIndex = 0;
        }
    }

    private void isQuestionSentence() {
        for(String qWord : WordCollections.directQuestionWords){
            if(text.startsWith(qWord)){
                isQuestion = true;
                return;
            }
        }
        for(String qWord : WordCollections.politeQuestionWords){
            if(text.startsWith(qWord)){
                isQuestion = true;
                return;
            }
        }
        isQuestion = false;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDirectObject() {
        return directObject;
    }

    public void setDirectObject(String directObject) {
        this.directObject = directObject;
    }

    public String getIndirectObject() {
        return indirectObject;
    }

    public void setIndirectObject(String indirectObject) {
        this.indirectObject = indirectObject;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setIsQuestion(boolean isQuestion) {
        this.isQuestion = isQuestion;
    }

    public String getTense() {
        return tense;
    }

    public String getPerson() {
        return person;
    }

    public String getRemainingPhrase() {
        return remainingPhrase;
    }

    public String getRearrangedText() {
        return rearrangedText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    @Override
    public String toString() {
        return text;
    }
}