package com.travelmate.travelmate.controller;

import com.travelmate.travelmate.entity.Restaurant;
import com.travelmate.travelmate.service.RestaurantService;
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
class RestaurantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private Restaurant sampleRestaurant;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();

        sampleRestaurant = new Restaurant(
                1L,
                "Fisherman's Wharf",
                "Cavelossim, Goa",
                "Riverside restaurant offering authentic Goan seafood",
                "https://example.com/fishermans-wharf.jpg"
        );
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        List<Restaurant> restaurants = Arrays.asList(
                sampleRestaurant,
                new Restaurant(2L, "Bukhara", "New Delhi, India", "Famous North Indian restaurant", "https://example.com/bukhara.jpg")
        );

        when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Fisherman's Wharf"))
                .andExpect(jsonPath("$[1].name").value("Bukhara"));
    }

    @Test
    void testGetRestaurantById_Found() throws Exception {
        when(restaurantService.getRestaurantById(1L)).thenReturn(sampleRestaurant);

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Fisherman's Wharf"))
                .andExpect(jsonPath("$.location").value("Cavelossim, Goa"));
    }

    @Test
    void testGetRestaurantById_NotFound() throws Exception {
        when(restaurantService.getRestaurantById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/restaurants/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRestaurant() throws Exception {
        String jsonPayload = "{\"name\":\"Karavalli\",\"location\":\"Bengaluru, Karnataka\",\"description\":\"Coastal Indian cuisine\",\"imageUrl\":\"https://example.com/karavalli.jpg\"}";
        Restaurant created = new Restaurant(3L, "Karavalli", "Bengaluru, Karnataka", "Coastal Indian cuisine", "https://example.com/karavalli.jpg");

        when(restaurantService.createRestaurant(any(Restaurant.class))).thenReturn(created);

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Karavalli"));
    }

    @Test
    void testUpdateRestaurant_Success() throws Exception {
        String updateJson = "{\"name\":\"Updated Fisherman's Wharf\",\"location\":\"Panaji, Goa\",\"description\":\"Updated description\",\"imageUrl\":\"https://example.com/wharf-updated.jpg\"}";
        Restaurant updated = new Restaurant(1L, "Updated Fisherman's Wharf", "Panaji, Goa", "Updated description", "https://example.com/wharf-updated.jpg");

        when(restaurantService.updateRestaurant(eq(1L), any(Restaurant.class))).thenReturn(updated);

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Fisherman's Wharf"));
    }

    @Test
    void testUpdateRestaurant_NotFound() throws Exception {
        String updateJson = "{\"name\":\"Non-existent\",\"location\":\"Unknown\",\"description\":\"None\",\"imageUrl\":\"https://example.com/none.jpg\"}";

        when(restaurantService.updateRestaurant(eq(99L), any(Restaurant.class))).thenReturn(null);

        mockMvc.perform(put("/api/restaurants/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRestaurant_Success() throws Exception {
        when(restaurantService.deleteRestaurant(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRestaurant_NotFound() throws Exception {
        when(restaurantService.deleteRestaurant(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/restaurants/99"))
                .andExpect(status().isNotFound());
    }
}
