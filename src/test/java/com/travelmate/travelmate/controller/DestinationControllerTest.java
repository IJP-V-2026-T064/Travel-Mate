package com.travelmate.travelmate.controller;

import com.travelmate.travelmate.entity.Destination;
import com.travelmate.travelmate.service.DestinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DestinationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DestinationService destinationService;

    @InjectMocks
    private DestinationController destinationController;

    private Destination sampleDestination;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(destinationController).build();

        sampleDestination = new Destination(
                1L,
                "Goa Beaches",
                "Goa, India",
                "Tropical beaches and water sports",
                "https://example.com/goa.jpg"
        );
    }

    @Test
    void testGetAllDestinations() throws Exception {
        List<Destination> destinations = Arrays.asList(
                sampleDestination,
                new Destination(2L, "Manali", "Himachal Pradesh, India", "Mountain peaks", "https://example.com/manali.jpg")
        );

        when(destinationService.getAllDestinations()).thenReturn(destinations);

        mockMvc.perform(get("/api/destinations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Goa Beaches"))
                .andExpect(jsonPath("$[1].name").value("Manali"));
    }

    @Test
    void testGetDestinationById_Found() throws Exception {
        when(destinationService.getDestinationById(1L)).thenReturn(sampleDestination);

        mockMvc.perform(get("/api/destinations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Goa Beaches"))
                .andExpect(jsonPath("$.location").value("Goa, India"));
    }

    @Test
    void testGetDestinationById_NotFound() throws Exception {
        when(destinationService.getDestinationById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/destinations/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateDestination() throws Exception {
        String jsonPayload = "{\"name\":\"Kerala Backwaters\",\"location\":\"Kerala, India\",\"description\":\"Serene backwaters\",\"imageUrl\":\"https://example.com/kerala.jpg\"}";
        Destination created = new Destination(3L, "Kerala Backwaters", "Kerala, India", "Serene backwaters", "https://example.com/kerala.jpg");

        when(destinationService.createDestination(any(Destination.class))).thenReturn(created);

        mockMvc.perform(post("/api/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Kerala Backwaters"));
    }

    @Test
    void testUpdateDestination_Success() throws Exception {
        String updateJson = "{\"name\":\"Updated Goa\",\"location\":\"Goa, India\",\"description\":\"Updated description\",\"imageUrl\":\"https://example.com/goa2.jpg\"}";
        Destination updated = new Destination(1L, "Updated Goa", "Goa, India", "Updated description", "https://example.com/goa2.jpg");

        when(destinationService.updateDestination(eq(1L), any(Destination.class))).thenReturn(updated);

        mockMvc.perform(put("/api/destinations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Goa"));
    }

    @Test
    void testUpdateDestination_NotFound() throws Exception {
        String updateJson = "{\"name\":\"Non-existent\",\"location\":\"Unknown\",\"description\":\"None\",\"imageUrl\":\"https://example.com/none.jpg\"}";

        when(destinationService.updateDestination(eq(99L), any(Destination.class))).thenReturn(null);

        mockMvc.perform(put("/api/destinations/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDestination_Success() throws Exception {
        when(destinationService.deleteDestination(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/destinations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteDestination_NotFound() throws Exception {
        when(destinationService.deleteDestination(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/destinations/99"))
                .andExpect(status().isNotFound());
    }
}
