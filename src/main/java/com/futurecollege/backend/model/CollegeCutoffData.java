package com.futurecollege.backend.model; // Adjust your package name

import java.util.Map;
// Import Lombok annotations if you use Lombok (add to pom.xml)
// import lombok.Data;

// @Data // Uncomment if you are using Lombok
public class CollegeCutoffData {
    private String institute;
    private String choice_code;
    private String course;
    private Map<String, CutoffDetails> cutoffs;

    // --- Standard Getters and Setters (uncomment if not using Lombok) ---
    public String getInstitute() { return institute; }
    public void setInstitute(String institute) { this.institute = institute; }
    public String getChoice_code() { return choice_code; }
    public void setChoice_code(String choice_code) { this.choice_code = choice_code; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public Map<String, CutoffDetails> getCutoffs() { return cutoffs; }
    public void setCutoffs(Map<String, CutoffDetails> cutoffs) { this.cutoffs = cutoffs; }
    // --- End Standard Getters and Setters ---
}
