package com.innov.bhaashe;

import com.innov.bhaashe.data.Clause;
import com.innov.bhaashe.data.Sentence;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sagar on 11/21/2016.
 */
public class SentenceAnalyzer {

    private static final String[] sentences = {
    "The human brain never stops working until you stand up to speak in public",
    "I always wanted to be somebody, but I should have been more specific",
    "Curiosity killed the cat",
    "I stopped believing in Santa Claus when my mother took me to see him in a department store, and he asked for my autograph"
    };

    public static void main(String[] args) throws FileNotFoundException {

        //List<String> inputs = FileUtils.getInstance().readLinesFromFile("input.txt");
        List<String> inputs = Arrays.asList(sentences);
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
            }

        }
    }

}
