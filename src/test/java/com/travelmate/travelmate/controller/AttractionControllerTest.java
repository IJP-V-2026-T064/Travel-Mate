package com.travelmate.travelmate.controller;

import com.travelmate.travelmate.entity.Attraction;
import com.travelmate.travelmate.service.AttractionService;
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
class AttractionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AttractionService attractionService;

    @InjectMocks
    private AttractionController attractionController;

    private Attraction sampleAttraction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attractionController).build();

        sampleAttraction = new Attraction(
                1L,
                "Aguada Fort",
                "Candolim, Goa",
                "A Portuguese fort and lighthouse overlooking the Arabian Sea",
                "https://example.com/aguada.jpg"
        );
    }

    @Test
    void testGetAllAttractions() throws Exception {
        List<Attraction> attractions = Arrays.asList(
                sampleAttraction,
                new Attraction(2L, "Dudhsagar Falls", "Sonaulim, Goa", "Spectacular waterfalls", "https://example.com/dudhsagar.jpg")
        );

        when(attractionService.getAllAttractions()).thenReturn(attractions);

        mockMvc.perform(get("/api/attractions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Aguada Fort"))
                .andExpect(jsonPath("$[1].name").value("Dudhsagar Falls"));
    }

    @Test
    void testGetAttractionById_Found() throws Exception {
        when(attractionService.getAttractionById(1L)).thenReturn(sampleAttraction);

        mockMvc.perform(get("/api/attractions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Aguada Fort"))
                .andExpect(jsonPath("$.location").value("Candolim, Goa"));
    }

    @Test
    void testGetAttractionById_NotFound() throws Exception {
        when(attractionService.getAttractionById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/attractions/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAttraction() throws Exception {
        String jsonPayload = "{\"name\":\"Hawa Mahal\",\"location\":\"Jaipur, Rajasthan\",\"description\":\"Palace of Winds\",\"imageUrl\":\"https://example.com/hawamahal.jpg\"}";
        Attraction created = new Attraction(3L, "Hawa Mahal", "Jaipur, Rajasthan", "Palace of Winds", "https://example.com/hawamahal.jpg");

        when(attractionService.createAttraction(any(Attraction.class))).thenReturn(created);

        mockMvc.perform(post("/api/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Hawa Mahal"));
    }

    @Test
    void testUpdateAttraction_Success() throws Exception {
        String updateJson = "{\"name\":\"Updated Aguada Fort\",\"location\":\"Candolim, Goa\",\"description\":\"Updated description\",\"imageUrl\":\"https://example.com/aguada2.jpg\"}";
        Attraction updated = new Attraction(1L, "Updated Aguada Fort", "Candolim, Goa", "Updated description", "https://example.com/aguada2.jpg");

        when(attractionService.updateAttraction(eq(1L), any(Attraction.class))).thenReturn(updated);

        mockMvc.perform(put("/api/attractions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Aguada Fort"));
    }

    @Test
    void testUpdateAttraction_NotFound() throws Exception {
        String updateJson = "{\"name\":\"Non-existent\",\"location\":\"Unknown\",\"description\":\"None\",\"imageUrl\":\"https://example.com/none.jpg\"}";

        when(attractionService.updateAttraction(eq(99L), any(Attraction.class))).thenReturn(null);

        mockMvc.perform(put("/api/attractions/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAttraction_Success() throws Exception {
        when(attractionService.deleteAttraction(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/attractions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAttraction_NotFound() throws Exception {
        when(attractionService.deleteAttraction(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/attractions/99"))
                .andExpect(status().isNotFound());
    }
}
