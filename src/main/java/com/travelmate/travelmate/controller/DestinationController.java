package com.travelmate.travelmate.controller;

import com.travelmate.travelmate.entity.Destination;
import com.travelmate.travelmate.service.DestinationService;
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
@RequestMapping("/api/destinations")
@CrossOrigin(origins = "*")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    // 1. GET /api/destinations - Get all destinations
    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        List<Destination> destinations = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinations);
    }

    // 2. GET /api/destinations/{id} - Get destination by ID
    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        Destination destination = destinationService.getDestinationById(id);
        if (destination == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(destination);
    }

    // 3. POST /api/destinations - Create a new destination
    @PostMapping
    public ResponseEntity<Destination> createDestination(@Valid @RequestBody Destination destination) {
        Destination createdDestination = destinationService.createDestination(destination);
        return new ResponseEntity<>(createdDestination, HttpStatus.CREATED);
    }

    // 4. PUT /api/destinations/{id} - Update an existing destination
    @PutMapping("/{id}")
    public ResponseEntity<Destination> updateDestination(
            @PathVariable Long id,
            @Valid @RequestBody Destination destinationDetails) {
        Destination updatedDestination = destinationService.updateDestination(id, destinationDetails);
        if (updatedDestination == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedDestination);
    }

    // 5. DELETE /api/destinations/{id} - Delete a destination
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        boolean deleted = destinationService.deleteDestination(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
