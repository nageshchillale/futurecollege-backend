package com.futurecollege.backend.service;

import com.futurecollege.backend.dto.CollegePredictionResultDTO;
import com.futurecollege.backend.model.Cutoff;
import com.futurecollege.backend.model.CutoffEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CutoffService {

    private List<CutoffEntry> cutoffEntries;
    private final ObjectMapper objectMapper;

    public CutoffService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void loadCollegeData() {
        try {
            InputStream inputStream = new ClassPathResource("data.json").getInputStream();
            cutoffEntries = Arrays.asList(objectMapper.readValue(inputStream, CutoffEntry[].class));
            System.out.println("Loaded " + cutoffEntries.size() + " cutoff entries from data.json.");
        } catch (IOException e) {
            System.err.println("Failed to load college data from data.json: " + e.getMessage());
            cutoffEntries = new ArrayList<>();
        }
    }

    public List<CutoffEntry> getAll() {
        return new ArrayList<>(cutoffEntries);
    }

    public List<CollegePredictionResultDTO> getPredictedColleges(
            String userCategory,
            String userBranch,
            Double userPercentage,
            String userGender,
            double tolerance) {

        // ======= ✅ VALIDATION BLOCK START =======
        if (cutoffEntries == null || cutoffEntries.isEmpty()) {
            throw new IllegalStateException("Cutoff data is not loaded.");
        }

        if (userPercentage == null || userPercentage < 0 || userPercentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100.");
        }

        List<String> validCategories = Arrays.asList(
                "OPEN", "OBC", "SC", "ST", "EWS", "SEBC", "NTB", "NTC", "NTD", "NTA",
                "DEFR-OBC", "PWDR-OBC"
        );
        if (!validCategories.contains(userCategory.toUpperCase())) {
            throw new IllegalArgumentException("Invalid category: " + userCategory);
        }

        List<String> validGenders = Arrays.asList("G", "L");
        if (!validGenders.contains(userGender.toUpperCase())) {
            throw new IllegalArgumentException("Invalid gender: " + userGender);
        }

        boolean branchExists = cutoffEntries.stream()
                .anyMatch(entry -> entry.getCourse().equalsIgnoreCase(userBranch));
        if (!branchExists) {
            throw new IllegalArgumentException("Invalid branch: " + userBranch);
        }
        // ======= ✅ VALIDATION BLOCK END =======

        String normalizedUserCategory = userCategory.toUpperCase();
        String normalizedUserGender = userGender.toUpperCase();
        String normalizedUserBranch = userBranch.toLowerCase();

        List<CollegePredictionResultDTO> matchingCollegesDTOs = new ArrayList<>();

        List<CutoffEntry> branchFilteredEntries = cutoffEntries.stream()
                .filter(entry -> entry.getCourse().toLowerCase().contains(normalizedUserBranch))
                .collect(Collectors.toList());

        for (CutoffEntry entry : branchFilteredEntries) {
            Map<String, Cutoff> cutoffs = entry.getCutoffs();
            String cutoffKey = getCutoffKey(normalizedUserCategory, normalizedUserGender);

            if (cutoffs.containsKey(cutoffKey)) {
                Cutoff collegeCutoff = cutoffs.get(cutoffKey);
                if (collegeCutoff != null && collegeCutoff.getPercent() != null) {
                    double collegeCutoffPercent = collegeCutoff.getPercent();

                    // --- PREDICTION LOGIC ---
                    if (collegeCutoffPercent <= (userPercentage + tolerance)) {
                        matchingCollegesDTOs.add(new CollegePredictionResultDTO(
                                entry.getInstitute(),
                                entry.getChoiceCode(),
                                entry.getCourse(),
                                collegeCutoffPercent
                        ));
                    }
                }
            }
        }

        // Sort colleges by cutoff percent (lowest first)
        matchingCollegesDTOs.sort(Comparator.comparingDouble(CollegePredictionResultDTO::getCutoffPercent).reversed());


        return matchingCollegesDTOs;
    }

    private String getCutoffKey(String category, String gender) {
        if ("OPEN".equals(category)) {
            return gender + category;
        }

        List<String> genderSpecificCategories = Arrays.asList(
                "OBC", "SC", "ST", "NTB", "NTC", "NTD", "SEBC", "NTA"
        );
        if (genderSpecificCategories.contains(category)) {
            return gender + category;
        }

        List<String> genderNeutralCategories = Arrays.asList(
                "EWS", "PWDR-OBC", "DEFR-OBC"
        );
        if (genderNeutralCategories.contains(category)) {
            return category;
        }

        return category;
    }

    public List<String> getAllBranches() {
        return cutoffEntries.stream()
                .map(CutoffEntry::getCourse)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
