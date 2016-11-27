package com.innov.bhaashe;

import com.innov.bhaashe.data.Candidate;
import com.innov.bhaashe.data.Input;
import com.innov.bhaashe.utils.Dictionary;
import com.innov.bhaashe.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhraseTranslator {

    Map<String, String> phraseMappings;
    Dictionary dictionary;

    public PhraseTranslator() throws IOException, URISyntaxException {
        phraseMappings = getFileUtils().getMappingFromFile("phrases.txt", "=");
        dictionary = Dictionary.getInstance();
    }

    public Candidate process(Input input){
        List<Candidate> candidateList = getClosestPhrase(input.getText());
        if(candidateList.isEmpty()){
            return null;
        }

        for(Candidate candidate : candidateList){
            replace(input, candidate);
            finish(candidate);
        }

        for(Candidate candidate : candidateList){
            if(candidate.getClosestPhrase().equalsIgnoreCase(input.getText())){
                return candidate;
            }else if(!phraseMappings.get(candidate.getClosestPhrase()).equalsIgnoreCase(candidate.getTranslation())){
                return candidate;
            }
        }

        return null;
    }

    private void finish(Candidate candidate) {

    }

    private void replace(Input input, Candidate candidate) {
        if(candidate.getVaryingIndices() == null || candidate.getVaryingIndices().isEmpty()){
            return;
        }
        for(int index : candidate.getVaryingIndices()){
            String wordToReplace = candidate.getClosestPhrase().split(" ")[index];
            String wordToReplaceInTranslation = getFromDictionary(wordToReplace);
            String replacementWord = input.getText().split(" ")[index];
            String replacementTranslation = getFromDictionary(replacementWord);

            String transformedInput = candidate.getClosestPhrase().replace(wordToReplace, replacementWord);
            String translationInProgress = candidate.getTranslation().replace(wordToReplaceInTranslation, replacementTranslation);

            candidate.setClosestPhrase(transformedInput);
            candidate.setTranslation(translationInProgress);
        }
    }

    private String getFromDictionary(String wordToReplace) {
        String translation = dictionary.get(wordToReplace);
        if(StringUtils.isEmpty(translation)){
            String modifiedWord = wordToReplace;
            if(wordToReplace.endsWith("ing")){
                modifiedWord = StringUtils.removeEnd(wordToReplace, "ing");
            }
            if(wordToReplace.endsWith("ed")){
                modifiedWord = StringUtils.removeEnd(wordToReplace, "ed");
            }

            if(!modifiedWord.equals(wordToReplace)){
                translation = dictionary.get(modifiedWord);
                if(StringUtils.isEmpty(translation)){
                    dictionary.addToDictionary(modifiedWord);
                }
            }else {
                dictionary.addToDictionary(wordToReplace);
            }
        }
        return StringUtils.isEmpty(translation) ? wordToReplace : translation;
    }

    private List<Candidate> getClosestPhrase(String phrase) {
        List<Candidate> candidateList = new ArrayList<Candidate>();
        double differenceIndex = .50d;
        String[] phraseSplits = phrase.split(" ");
        for(String s : phraseMappings.keySet()){
            String[] matchSplits = s.split(" ");
            if(phraseSplits.length == matchSplits.length){

                List<Integer> diffIndices = new ArrayList<Integer>();
                for(int i=0; i<phraseSplits.length; i++){
                    if(!phraseSplits[i].equalsIgnoreCase(matchSplits[i])){
                        diffIndices.add(i);
                    }
                }
                if(diffIndices.size() == 0){
                    candidateList = new ArrayList<Candidate>();
                    candidateList.add(new Candidate(s, null, phraseMappings.get(s)));
                    return candidateList;
                }

                double si = ((double) diffIndices.size())/phraseSplits.length;

                if(si == differenceIndex){
                    candidateList.add(new Candidate(s, diffIndices, phraseMappings.get(s)));
                }

                if(si < differenceIndex){
                    differenceIndex = si;
                    candidateList = new ArrayList<Candidate>();
                    candidateList.add(new Candidate(s, diffIndices, phraseMappings.get(s)));
                }

            }
        }
        return candidateList;
    }

    private FileUtils getFileUtils() {
        return FileUtils.getInstance();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        PhraseTranslator phraseTranslator = new PhraseTranslator();
        List<String> inputs = phraseTranslator.getFileUtils().readLinesFromFile("input.txt");
        for(String text : inputs){
            Candidate candidate = phraseTranslator.process(new Input(text));
            if(candidate == null){
                FileUtils.getInstance().writeLineToFile("phrases.txt", text);
                Tarjume tarjume = new Tarjume();
                tarjume.process(new Input(text));
            }else{
                System.out.println("From PhraseTranslator :: "+ candidate.getTranslation());
            }
        }

    }
}
