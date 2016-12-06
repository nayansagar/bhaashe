package com.innov.bhaashe;

import com.innov.bhaashe.data.Clause;
import com.innov.bhaashe.data.Sentence;
import com.innov.bhaashe.utils.FileUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sagar on 11/21/2016.
 */
public class SentenceAnalyzer {

    private static final String[] sentences = {
    "she slowly moved out of madras harbour"
    };

    public static void main(String[] args) throws FileNotFoundException {

        List<String> inputs = FileUtils.getInstance().readLinesFromFile("input.txt");
        //List<String> inputs = Arrays.asList(sentences);
        for(String input : inputs){
            Sentence sentence = new Sentence(input);
            System.out.println("######################### "+sentence.toString()+" #########################");
            for(Clause clause : sentence.getClauses()){
                System.out.println("----------------------" + clause.toString() + "----------------------");
                System.out.println("IS_QUESTION : "+ clause.isQuestion());
                System.out.println("SUBJECT : "+ clause.getSubject());
                System.out.println("VERB : "+ clause.getVerb());
                System.out.println("DIRECT OBJECT : "+ clause.getDirectObject());
                System.out.println("INDIRECT OBJECT : "+ clause.getIndirectObject());
                System.out.println("REMAINING PHRASE : "+ clause.getRemainingPhrase());
                System.out.println("TENSE : "+ clause.getTense());
                System.out.println("PERSON : "+ clause.getPerson());
                System.out.println("REARRANGED : "+ clause.getRearrangedText());
                System.out.println("TRANSLATED : "+clause.getTranslatedText());
            }

        }
    }

}
