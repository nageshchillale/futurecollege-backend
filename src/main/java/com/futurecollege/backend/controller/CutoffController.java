package com.futurecollege.backend.controller;

import com.futurecollege.backend.dto.CollegePredictionResultDTO; // NEW IMPORT
import com.futurecollege.backend.model.CutoffEntry; // Still needed for getAllCutoffs if you keep it
import com.futurecollege.backend.service.CutoffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cutoffs") // Base path for this controller
public class CutoffController {

    private final CutoffService cutoffService;

    public CutoffController(CutoffService cutoffService) {
        this.cutoffService = cutoffService;
    }

    // This endpoint returns ALL CutoffEntry objects, including all cutoffs data.
    // Useful for fetching raw data, but not for simplified predictions.
    @GetMapping
    public List<CutoffEntry> getAllCutoffs() {
        return cutoffService.getAll();
    }

    /**
     * Predicts colleges based on user's criteria. Returns a simplified list of college details.
     *
     * Example URL:
     * http://localhost:8080/api/cutoffs/predict?category=OBC&branch=Computer%20Engineering&percent=93.5&gender=G&tolerance=1.5
     *
     * @param category  The user's reservation category (e.g., "OPEN", "OBC", "SC", "ST", "EWS").
     * @param branch    The desired branch/course (e.g., "Computer Engineering", "Civil"). Case-insensitive and partial match.
     * @param percent   The user's percentage.
     * @param gender    The user's gender ("G" for Gents, "L" for Ladies).
     * @param tolerance Optional: The allowed difference for "near about" comparison (default to 1.0).
     * @return A ResponseEntity containing a list of CollegePredictionResultDTOs or 204 No Content if none found.
     */
    @GetMapping("/predict")
    public ResponseEntity<List<CollegePredictionResultDTO>> predict( // Changed return type to DTO list
                                                                     @RequestParam String category,
                                                                     @RequestParam String branch,
                                                                     @RequestParam Double percent,
                                                                     @RequestParam String gender,
                                                                     @RequestParam(defaultValue = "1.0") double tolerance) {

        // Basic validation for percentage input
        if (percent == null || percent < 0 || percent > 100) {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
        }

        // Call the service method to get predicted colleges (as DTOs)
        List<CollegePredictionResultDTO> predictedColleges = cutoffService.getPredictedColleges(
                category,
                branch,
                percent,
                gender,
                tolerance
        );

        if (predictedColleges.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Return 204 No Content if no matches
        }
        return ResponseEntity.ok(predictedColleges); // Return 200 OK with the list of DTOs
    }

    @GetMapping("/branches")
    public List<String> getAllBranches() {
        return cutoffService.getAllBranches();
    }

}