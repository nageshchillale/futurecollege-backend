package com.futurecollege.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class CutoffEntry {

    @JsonProperty("institute")
    private String institute;

    @JsonProperty("choice_code")
    private String choiceCode;

    @JsonProperty("course")
    private String course;

    @JsonProperty("cutoffs")
    private Map<String, Cutoff> cutoffs;

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getChoiceCode() {
        return choiceCode;
    }

    public void setChoiceCode(String choiceCode) {
        this.choiceCode = choiceCode;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Map<String, Cutoff> getCutoffs() {
        return cutoffs;
    }

    public void setCutoffs(Map<String, Cutoff> cutoffs) {
        this.cutoffs = cutoffs;
    }


}