package com.travelmate.travelmate.controller;

import com.travelmate.travelmate.entity.Hotel;
import com.travelmate.travelmate.service.HotelService;
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
class HotelControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    private Hotel sampleHotel;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();

        sampleHotel = new Hotel(
                1L,
                "Taj Fort Aguada Resort",
                "Sinquerim, Goa",
                "Luxury beachfront heritage resort overlooking the Arabian Sea",
                "https://example.com/taj-aguada.jpg"
        );
    }

    @Test
    void testGetAllHotels() throws Exception {
        List<Hotel> hotels = Arrays.asList(
                sampleHotel,
                new Hotel(2L, "The Oberoi", "New Delhi, India", "Luxury hotel in Delhi", "https://example.com/oberoi.jpg")
        );

        when(hotelService.getAllHotels()).thenReturn(hotels);

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Taj Fort Aguada Resort"))
                .andExpect(jsonPath("$[1].name").value("The Oberoi"));
    }

    @Test
    void testGetHotelById_Found() throws Exception {
        when(hotelService.getHotelById(1L)).thenReturn(sampleHotel);

        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Taj Fort Aguada Resort"))
                .andExpect(jsonPath("$.location").value("Sinquerim, Goa"));
    }

    @Test
    void testGetHotelById_NotFound() throws Exception {
        when(hotelService.getHotelById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/hotels/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateHotel() throws Exception {
        String jsonPayload = "{\"name\":\"Rambagh Palace\",\"location\":\"Jaipur, Rajasthan\",\"description\":\"Former royal residence\",\"imageUrl\":\"https://example.com/rambagh.jpg\"}";
        Hotel created = new Hotel(3L, "Rambagh Palace", "Jaipur, Rajasthan", "Former royal residence", "https://example.com/rambagh.jpg");

        when(hotelService.createHotel(any(Hotel.class))).thenReturn(created);

        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Rambagh Palace"));
    }

    @Test
    void testUpdateHotel_Success() throws Exception {
        String updateJson = "{\"name\":\"Updated Taj Resort\",\"location\":\"Sinquerim Beach, Goa\",\"description\":\"Updated description\",\"imageUrl\":\"https://example.com/taj-updated.jpg\"}";
        Hotel updated = new Hotel(1L, "Updated Taj Resort", "Sinquerim Beach, Goa", "Updated description", "https://example.com/taj-updated.jpg");

        when(hotelService.updateHotel(eq(1L), any(Hotel.class))).thenReturn(updated);

        mockMvc.perform(put("/api/hotels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Taj Resort"));
    }

    @Test
    void testUpdateHotel_NotFound() throws Exception {
        String updateJson = "{\"name\":\"Non-existent\",\"location\":\"Unknown\",\"description\":\"None\",\"imageUrl\":\"https://example.com/none.jpg\"}";

        when(hotelService.updateHotel(eq(99L), any(Hotel.class))).thenReturn(null);

        mockMvc.perform(put("/api/hotels/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteHotel_Success() throws Exception {
        when(hotelService.deleteHotel(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/hotels/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteHotel_NotFound() throws Exception {
        when(hotelService.deleteHotel(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/hotels/99"))
                .andExpect(status().isNotFound());
    }
}
