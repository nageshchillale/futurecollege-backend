package com.futurecollege.backend.controller; // Adjust your package name

import com.futurecollege.backend.model.CollegeCutoffData; // Import your data model
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/cutoffs") // Base URL for these endpoints will be /api/cutoffs
@CrossOrigin(origins = "http://localhost:3000") // !!! IMPORTANT: Allows your React app to fetch data !!!
public class CutoffDataController {

    private final ObjectMapper objectMapper = new ObjectMapper(); // Used to parse JSON

    /**
     * Handles GET requests to /api/cutoffs/{year}
     * Example: /api/cutoffs/2024-25 will load cutoff_2024-25.json
     *
     * @param year The year string (e.g., "2023-24", "2024-25") from the URL path.
     * @return A list of CollegeCutoffData objects as JSON, or 404 Not Found if file is missing.
     */
    @GetMapping("/{year}")
    public ResponseEntity<List<CollegeCutoffData>> getCutoffsByYear(@PathVariable String year) {
        // Construct the file path within src/main/resources/cutoffData/
        String filename = String.format("cutoffData/cutoff_%s.json", year);

        List<CollegeCutoffData> cutoffs = readJsonFromFile(filename);

        if (cutoffs.isEmpty()) {
            // If the list is empty, it means the file wasn't found or was empty
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cutoffs, HttpStatus.OK); // Return the data with a 200 OK status
    }

    /**
     * Helper method to read a JSON file from the classpath (resources folder).
     * @param filePath The path to the JSON file relative to src/main/resources (e.g., "cutoffData/cutoff_2024-25.json").
     * @return A List of CollegeCutoffData parsed from the JSON file, or an empty list if an error occurs.
     */
    private List<CollegeCutoffData> readJsonFromFile(String filePath) {
        try {
            // ClassPathResource looks for files in the 'resources' directory
            ClassPathResource resource = new ClassPathResource(filePath);

            // Check if the file exists before trying to read it
            if (!resource.exists()) {
                System.err.println("JSON file not found at: " + filePath);
                return Collections.emptyList();
            }

            // Get an InputStream to read the file content
            InputStream inputStream = resource.getInputStream();

            // Use ObjectMapper to read the JSON array into a List of CollegeCutoffData objects
            return objectMapper.readValue(inputStream, new TypeReference<List<CollegeCutoffData>>() {});
        } catch (IOException e) {
            System.err.println("Error reading or parsing JSON file: " + filePath + " - " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
