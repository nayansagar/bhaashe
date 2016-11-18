package com.innov.bhaashe.data;

import java.util.List;

public class Candidate {

    private String originalClosestPhrase;
    private String originalTranslation;
    private String closestPhrase;
    private String translation;
    private List<Integer> varyingIndices;

    public Candidate(String closestPhrase, List<Integer> varyingIndices, String translation) {
        this.closestPhrase = closestPhrase;
        this.varyingIndices = varyingIndices;
        this.originalClosestPhrase = closestPhrase;
        this.translation = translation;
        this.originalTranslation = translation;
    }

    public void setClosestPhrase(String closestPhrase) {
        this.closestPhrase = closestPhrase;
    }

    public void setVaryingIndices(List<Integer> varyingIndices) {
        this.varyingIndices = varyingIndices;
    }

    public String getClosestPhrase() {
        return closestPhrase;
    }

    public List<Integer> getVaryingIndices() {
        return varyingIndices;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getOriginalClosestPhrase() {
        return originalClosestPhrase;
    }

    public String getOriginalTranslation() {
        return originalTranslation;
    }
}
