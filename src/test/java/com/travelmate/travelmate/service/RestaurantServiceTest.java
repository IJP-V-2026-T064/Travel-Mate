package com.travelmate.travelmate.service;

import com.travelmate.travelmate.entity.Restaurant;
import com.travelmate.travelmate.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant sampleRestaurant;

    @BeforeEach
    void setUp() {
        sampleRestaurant = new Restaurant(
                1L,
                "Fisherman's Wharf",
                "Cavelossim, Goa",
                "Riverside restaurant offering authentic Goan seafood and river views.",
                "https://example.com/fishermans-wharf.jpg"
        );
    }

    @Test
    void testGetAllRestaurants() {
        List<Restaurant> list = Arrays.asList(
                sampleRestaurant,
                new Restaurant(2L, "Bukhara", "New Delhi, India", "Famous North Indian tandoori restaurant.", "https://example.com/bukhara.jpg")
        );
        when(restaurantRepository.findAll()).thenReturn(list);

        List<Restaurant> result = restaurantService.getAllRestaurants();

        assertEquals(2, result.size());
        assertEquals("Fisherman's Wharf", result.get(0).getName());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testGetRestaurantById_Found() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(sampleRestaurant));

        Restaurant result = restaurantService.getRestaurantById(1L);

        assertNotNull(result);
        assertEquals("Fisherman's Wharf", result.getName());
        assertEquals("Cavelossim, Goa", result.getLocation());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRestaurantById_NotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        Restaurant result = restaurantService.getRestaurantById(99L);

        assertNull(result);
        verify(restaurantRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateRestaurant() {
        Restaurant toCreate = new Restaurant("Karavalli", "Bengaluru, Karnataka", "Coastal Indian cuisine in a traditional setting.", "https://example.com/karavalli.jpg");
        Restaurant saved = new Restaurant(3L, "Karavalli", "Bengaluru, Karnataka", "Coastal Indian cuisine in a traditional setting.", "https://example.com/karavalli.jpg");

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(saved);

        Restaurant result = restaurantService.createRestaurant(toCreate);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Karavalli", result.getName());
        verify(restaurantRepository, times(1)).save(toCreate);
    }

    @Test
    void testUpdateRestaurant_Success() {
        Restaurant updateInfo = new Restaurant("Updated Fisherman's Wharf", "Panaji, Goa", "Expanded seating with river views.", "https://example.com/wharf-updated.jpg");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(sampleRestaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Restaurant result = restaurantService.updateRestaurant(1L, updateInfo);

        assertNotNull(result);
        assertEquals("Updated Fisherman's Wharf", result.getName());
        assertEquals("Panaji, Goa", result.getLocation());
        assertEquals("Expanded seating with river views.", result.getDescription());
        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).save(sampleRestaurant);
    }

    @Test
    void testUpdateRestaurant_NotFound() {
        Restaurant updateInfo = new Restaurant("Updated Name", "Location", "Desc", "url");
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        Restaurant result = restaurantService.updateRestaurant(999L, updateInfo);

        assertNull(result);
        verify(restaurantRepository, times(1)).findById(999L);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testDeleteRestaurant_Success() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        doNothing().when(restaurantRepository).deleteById(1L);

        boolean result = restaurantService.deleteRestaurant(1L);

        assertTrue(result);
        verify(restaurantRepository, times(1)).existsById(1L);
        verify(restaurantRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRestaurant_NotFound() {
        when(restaurantRepository.existsById(999L)).thenReturn(false);

        boolean result = restaurantService.deleteRestaurant(999L);

        assertFalse(result);
        verify(restaurantRepository, times(1)).existsById(999L);
        verify(restaurantRepository, never()).deleteById(anyLong());
    }
}
