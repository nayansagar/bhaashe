package com.innov.bhaashe;

import com.innov.bhaashe.utils.WordCollections;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Sagar on 11/21/2016.
 */
public class SentenceAnalyzer {

    static class AnalysisContext{

        private String sentence;
        private String subject;
        private String directObject;
        private String indirectObject;
        private String verb;
        private boolean isQuestion;

        private int subjectIndex = -1;

        public AnalysisContext(String sentence) {
            this.sentence = sentence.toLowerCase();
        }

        private void analyze(){
            isQuestionSentence();
            detectSubject();
            detectVerb();
            detectObjects();
        }

        private void detectObjects() {

        }

        private void detectVerb() {
            String[] splits = sentence.split(" ");
            if(isQuestion){
                if(splits.length > subjectIndex+1 && "been".equals(splits[subjectIndex+1])){
                    if(splits.length > subjectIndex+2){
                        verb = splits[subjectIndex+2];
                    }else {
                        verb = splits[subjectIndex+1];
                    }
                }else {
                    verb = splits[subjectIndex+1];
                }
            }else{

                if(splits.length > subjectIndex+2){
                    String next = splits[subjectIndex + 1];
                    String nextToNext = splits[subjectIndex + 2];
                    String phrase = next+" "+nextToNext;
                    if("have been".equals(phrase) || "has been".equals(phrase) || "had been".equals(phrase)){}
                }
                if(splits.length > subjectIndex+1){
                    String next = splits[subjectIndex + 1];
                    if( "am".equals(next) || "is".equals(next) || "are".equals(next)
                            || "was".equals(next) || "were".equals(next)
                            || "will".equals(next) || "would".equals(next) || "should".equals(next) || "could".equals(next)
                            || "won't".equals(next) || "wouldn't".equals(next) || "shouldn't".equals(next) || "couldn't".equals(next)){
                        verb = splits[subjectIndex + 2];
                    }
                }
            }
        }

        private void detectSubject() {
            if(isQuestion()){
                for(String word : WordCollections.politeQuestionWords) {
                    if (sentence.startsWith(word)) {
                        subject = sentence.split(" ")[1];
                        subjectIndex = 1;
                        return;
                    }
                }
                String[] splits = sentence.split(" ");
                for(int i = 1; i<splits.length; i++){
                    if(Arrays.asList(WordCollections.tenseWords).contains(splits[i])){
                        if( (splits.length > i + 2) && ( "a".equals(splits[i+1]) || "an".equals(splits[i+1])) ||
                                "the".equals(splits[i+1])){
                            subject = splits[i+1]+" "+splits[i+2];
                            subjectIndex = i+2;
                        }else{
                            subject = splits[i+1];
                            subjectIndex = i+1;
                        }
                    }
                }
            }
            subject = sentence.split(" ")[0]; //sentences like "here you are", "there you go" not handled
            subjectIndex = 0;
        }

        private void isQuestionSentence() {
            for(String qWord : WordCollections.directQuestionWords){
                if(sentence.startsWith(qWord)){
                    isQuestion = true;
                    break;
                }
            }
            for(String qWord : WordCollections.politeQuestionWords){
                if(sentence.startsWith(qWord)){
                    isQuestion = true;
                    break;
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
    }

    private static String[] sentences = {
            "He ran to the shop",
            "Mia gave me a heart attack",
            "I will give the pen to him",
            "what do you think is a good price?",
            "they won",
            "I love chocolate cake with rainbow sprinkles",
            "love for chocolates never dies"
    };

}
