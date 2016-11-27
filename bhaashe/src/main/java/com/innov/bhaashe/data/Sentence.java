package com.innov.bhaashe.data;

import com.innov.bhaashe.utils.WordCollections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagar on 11/26/2016.
 */
public class Sentence {

    private String sentence;

    private List<Clause> clauses = new ArrayList<Clause>();

    private String sentenceType;

    private String[] splits;

    public Sentence(String sentence) {
        this.sentence = sentence.toLowerCase();
        splits = sentence.split(" ");
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public String getSentenceType() {
        return sentenceType;
    }

    public void analyze(){
        int separator = -1;
        for(int i=1; i<splits.length; i++){
            if(isSeparatorWord(splits[i]) && isSubjectWord(splits[i+1])){
                createClause(separator + 1, i-1);
                createClause(i+1, splits.length - 1);
                separator = i;
            }
        }
    }

    private void createClause(int start, int end) {
        String sentence = "";
        for(int i=start; i<=end; i++){
            sentence += splits[i] + " ";
        }
        clauses.add(new Clause(sentence));
    }

    private boolean isSubjectWord(String split) {
        for(String word : WordCollections.subjectWords){
            if(word.equals(split)){
                return true;
            }
        }
        return false;
    }

    private boolean isSeparatorWord(String split) {
        for(String word : WordCollections.sentenceSeparators){
            if(word.equals(split)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return sentence;
    }
}
