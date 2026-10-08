package com.travelmate.travelmate.service;

import com.travelmate.travelmate.entity.Attraction;
import com.travelmate.travelmate.repository.AttractionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;

    public AttractionService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    // 1. Get all attractions
    public List<Attraction> getAllAttractions() {
        return attractionRepository.findAll();
    }

    // 2. Get attraction by ID
    public Attraction getAttractionById(Long id) {
        Optional<Attraction> optionalAttraction = attractionRepository.findById(id);
        return optionalAttraction.orElse(null);
    }

    // 3. Create attraction
    public Attraction createAttraction(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    // 4. Update attraction
    public Attraction updateAttraction(Long id, Attraction attractionDetails) {
        Optional<Attraction> optionalAttraction = attractionRepository.findById(id);
        if (optionalAttraction.isPresent()) {
            Attraction existingAttraction = optionalAttraction.get();
            existingAttraction.setName(attractionDetails.getName());
            existingAttraction.setLocation(attractionDetails.getLocation());
            existingAttraction.setDescription(attractionDetails.getDescription());
            existingAttraction.setImageUrl(attractionDetails.getImageUrl());
            return attractionRepository.save(existingAttraction);
        }
        return null;
    }

    // 5. Delete attraction
    public boolean deleteAttraction(Long id) {
        if (attractionRepository.existsById(id)) {
            attractionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
