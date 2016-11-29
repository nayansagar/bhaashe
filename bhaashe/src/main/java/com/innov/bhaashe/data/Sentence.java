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

    private boolean isSimpleSentence = true;

    private String[] splits;

    public Sentence(String sentence) {
        this.sentence = sentence.toLowerCase();
        splits = this.sentence.split(" ");
        analyze();
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public boolean isSimpleSentence() {
        return isSimpleSentence;
    }

    private void analyze(){
        int separator = -1;

        for(int i=1; i<splits.length; i++){
            if(isSeparatorWord(splits[i]) && isSubjectWord(splits[i+1])){
                isSimpleSentence = false;
                String left = getSentencePart(separator + 1, i - 1);
                String right = getSentencePart(i+1, splits.length - 1);
                Sentence leftSentence = new Sentence(left);
                Sentence rightSentence = new Sentence(right);
                clauses.addAll(leftSentence.getClauses());
                clauses.addAll(rightSentence.getClauses());
                separator = i;
                break;
            }
        }

        if(isSimpleSentence){
            clauses.add(new Clause(sentence));
        }
    }

    private String getSentencePart(int start, int end) {
        String sentence = "";
        for(int i=start; i<=end; i++){
            sentence += splits[i] + " ";
        }
        return sentence;
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
