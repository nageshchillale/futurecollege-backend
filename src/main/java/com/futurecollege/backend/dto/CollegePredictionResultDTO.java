package com.futurecollege.backend.dto;

import lombok.Data;

@Data
public class CollegePredictionResultDTO {
    private String institute;
    private String choice_code;
    private String course;
    private double cutoffPercent;

    public CollegePredictionResultDTO(String institute, String choice_code, String course, double cutoffPercent) {
        this.institute = institute;
        this.choice_code = choice_code;
        this.course = course;
        this.cutoffPercent = cutoffPercent;
    }
}
