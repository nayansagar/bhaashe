package com.innov.bhaashe;

import com.innov.bhaashe.utils.FileUtils;
import com.innov.bhaashe.utils.WordCollections;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sagar on 11/21/2016.
 */
public class SentenceAnalyzer {

    static class AnalysisContext{

        private String sentence;
        private String subject;
        private String directObject = "";
        private String indirectObject = "";
        private String verb;
        private boolean isQuestion;

        private int subjectIndex = -1;
        private int verbIndex = -1;
        private String[] splits;

        public AnalysisContext(String sentence) {
            this.sentence = sentence.toLowerCase();
        }

        private void analyze(){
            splits = sentence.split(" ");
            isQuestionSentence();
            detectSubject();
            detectVerb();
            detectObjects();
        }

        private void detectObjects() {
            if(verbIndex == splits.length -1){
                return;
            }

            for(String word : WordCollections.indirectObjects){
                if(word.equals(splits[verbIndex+1])){
                    indirectObject = splits[verbIndex+1];
                    for(int i= verbIndex+2; i<splits.length; i++){
                        directObject += splits[i]+" ";
                    }
                    return;
                }
            }

            for(int i=verbIndex+1; i<splits.length; i++){
                if("to".equals(splits[i]) || "for".equals(splits[i]) || "with".equals(splits[i])){
                    for(int j = verbIndex+1; j<i; j++){
                        directObject += splits[j]+" ";
                    }

                    for(int j=i; j<splits.length; j++){
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
                        verb = splits[subjectIndex + 3];
                        verbIndex = subjectIndex+3;
                        return;
                    }
                }
                if(splits.length > subjectIndex+2){
                    String next = splits[subjectIndex + 1];
                    if( "am".equals(next) || "is".equals(next) || "are".equals(next)
                            || "was".equals(next) || "were".equals(next)
                            || "will".equals(next) || "would".equals(next) || "should".equals(next) || "could".equals(next)
                            || "won't".equals(next) || "wouldn't".equals(next) || "shouldn't".equals(next)
                            || "couldn't".equals(next) || "can't".equals(next)){
                        verb = splits[subjectIndex + 2];
                        verbIndex = subjectIndex+2;
                        return;
                    }
                }
                verbIndex = subjectIndex+1;
                verb = splits[subjectIndex + 1];
            }
        }

        private void detectSubject() {
            if(isQuestion){
                for(String word : WordCollections.politeQuestionWords) {
                    if (sentence.startsWith(word)) {
                        subject = sentence.split(" ")[1];
                        subjectIndex = 1;
                        return;
                    }
                }
                for(int i = 1; i<splits.length; i++){
                    if(Arrays.asList(WordCollections.tenseWords).contains(splits[i])){
                        if( (splits.length > i + 2) && ( "a".equals(splits[i+1]) || "an".equals(splits[i+1])) ||
                                "the".equals(splits[i+1])){
                            subject = splits[i+1]+" "+splits[i+2];
                            subjectIndex = i+2;
                            return;
                        }else{
                            subject = splits[i+1];
                            subjectIndex = i+1;
                            return;
                        }
                    }
                }
            }

            if("the".equalsIgnoreCase(splits[0]) || "a".equalsIgnoreCase(splits[0]) || "an".equalsIgnoreCase(splits[0])){
                subject = splits[0] + " " + splits[1];
                return;
            }
            subject = splits[0]; //sentences like "here you are", "there you go" not handled
            subjectIndex = 0;
        }

        private void isQuestionSentence() {
            for(String qWord : WordCollections.directQuestionWords){
                if(sentence.startsWith(qWord)){
                    isQuestion = true;
                    return;
                }
            }
            for(String qWord : WordCollections.politeQuestionWords){
                if(sentence.startsWith(qWord)){
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
    }

    public static void main(String[] args) throws FileNotFoundException {
        SentenceAnalyzer sentenceAnalyzer = new SentenceAnalyzer();

        List<String> inputs = FileUtils.getInstance().readLinesFromFile("input.txt");
        for(String sentence : inputs){
            AnalysisContext analysisContext = new AnalysisContext(sentence);
            analysisContext.analyze();
            System.out.println("----------------------"+sentence+"----------------------");
            System.out.println("SUBJECT : "+analysisContext.getSubject());
            System.out.println("VERB : "+analysisContext.getVerb());
            System.out.println("DIRECT OBJECT : "+analysisContext.getDirectObject());
            System.out.println("INDIRECT OBJECT : "+analysisContext.getIndirectObject());
        }
    }

}
