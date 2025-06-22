package com.futurecollege.backend.model; // Adjust your package name

// Import Lombok annotations if you use Lombok (add to pom.xml)
// import lombok.Data;

// @Data // Uncomment if you are using Lombok
public class CutoffDetails {
    private int rank;
    private double percent;

    // --- Standard Getters and Setters (uncomment if not using Lombok) ---
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    public double getPercent() { return percent; }
    public void setPercent(double percent) { this.percent = percent; }
    // --- End Standard Getters and Setters ---
}
