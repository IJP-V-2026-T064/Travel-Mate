package com.travelmate.travelmate.service;

import com.travelmate.travelmate.entity.Destination;
import com.travelmate.travelmate.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    // 1. Get all destinations
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    // 2. Get destination by ID
    public Destination getDestinationById(Long id) {
        Optional<Destination> optionalDestination = destinationRepository.findById(id);
        return optionalDestination.orElse(null);
    }

    // 3. Create destination
    public Destination createDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    // 4. Update destination
    public Destination updateDestination(Long id, Destination destinationDetails) {
        Optional<Destination> optionalDestination = destinationRepository.findById(id);
        if (optionalDestination.isPresent()) {
            Destination existingDestination = optionalDestination.get();
            existingDestination.setName(destinationDetails.getName());
            existingDestination.setLocation(destinationDetails.getLocation());
            existingDestination.setDescription(destinationDetails.getDescription());
            existingDestination.setImageUrl(destinationDetails.getImageUrl());
            return destinationRepository.save(existingDestination);
        }
        return null;
    }

    // 5. Delete destination
    public boolean deleteDestination(Long id) {
        if (destinationRepository.existsById(id)) {
            destinationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
