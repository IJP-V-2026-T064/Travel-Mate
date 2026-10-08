package com.travelmate.travelmate.controller;

import com.travelmate.travelmate.entity.Attraction;
import com.travelmate.travelmate.service.AttractionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
@CrossOrigin(origins = "*")
public class AttractionController {

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    // 1. GET /api/attractions - Get all attractions
    @GetMapping
    public ResponseEntity<List<Attraction>> getAllAttractions() {
        List<Attraction> attractions = attractionService.getAllAttractions();
        return ResponseEntity.ok(attractions);
    }

    // 2. GET /api/attractions/{id} - Get attraction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Attraction> getAttractionById(@PathVariable Long id) {
        Attraction attraction = attractionService.getAttractionById(id);
        if (attraction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attraction);
    }

    // 3. POST /api/attractions - Create a new attraction
    @PostMapping
    public ResponseEntity<Attraction> createAttraction(@Valid @RequestBody Attraction attraction) {
        Attraction createdAttraction = attractionService.createAttraction(attraction);
        return new ResponseEntity<>(createdAttraction, HttpStatus.CREATED);
    }

    // 4. PUT /api/attractions/{id} - Update an existing attraction
    @PutMapping("/{id}")
    public ResponseEntity<Attraction> updateAttraction(
            @PathVariable Long id,
            @Valid @RequestBody Attraction attractionDetails) {
        Attraction updatedAttraction = attractionService.updateAttraction(id, attractionDetails);
        if (updatedAttraction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAttraction);
    }

    // 5. DELETE /api/attractions/{id} - Delete an attraction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttraction(@PathVariable Long id) {
        boolean deleted = attractionService.deleteAttraction(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
