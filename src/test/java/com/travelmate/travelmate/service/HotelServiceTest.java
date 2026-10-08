package com.travelmate.travelmate.service;

import com.travelmate.travelmate.entity.Hotel;
import com.travelmate.travelmate.repository.HotelRepository;
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
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel sampleHotel;

    @BeforeEach
    void setUp() {
        sampleHotel = new Hotel(
                1L,
                "Taj Fort Aguada Resort",
                "Sinquerim, Goa",
                "Luxury beachfront heritage resort overlooking the Arabian Sea.",
                "https://example.com/taj-aguada.jpg"
        );
    }

    @Test
    void testGetAllHotels() {
        List<Hotel> list = Arrays.asList(
                sampleHotel,
                new Hotel(2L, "The Oberoi", "New Delhi, India", "Luxury hotel in central Delhi.", "https://example.com/oberoi.jpg")
        );
        when(hotelRepository.findAll()).thenReturn(list);

        List<Hotel> result = hotelService.getAllHotels();

        assertEquals(2, result.size());
        assertEquals("Taj Fort Aguada Resort", result.get(0).getName());
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testGetHotelById_Found() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));

        Hotel result = hotelService.getHotelById(1L);

        assertNotNull(result);
        assertEquals("Taj Fort Aguada Resort", result.getName());
        assertEquals("Sinquerim, Goa", result.getLocation());
        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void testGetHotelById_NotFound() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        Hotel result = hotelService.getHotelById(99L);

        assertNull(result);
        verify(hotelRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateHotel() {
        Hotel toCreate = new Hotel("Rambagh Palace", "Jaipur, Rajasthan", "Former residence of the Maharaja of Jaipur.", "https://example.com/rambagh.jpg");
        Hotel saved = new Hotel(3L, "Rambagh Palace", "Jaipur, Rajasthan", "Former residence of the Maharaja of Jaipur.", "https://example.com/rambagh.jpg");

        when(hotelRepository.save(any(Hotel.class))).thenReturn(saved);

        Hotel result = hotelService.createHotel(toCreate);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Rambagh Palace", result.getName());
        verify(hotelRepository, times(1)).save(toCreate);
    }

    @Test
    void testUpdateHotel_Success() {
        Hotel updateInfo = new Hotel("Updated Taj Resort", "Sinquerim Beach, Goa", "Renovated 5-star beachfront property.", "https://example.com/taj-updated.jpg");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Hotel result = hotelService.updateHotel(1L, updateInfo);

        assertNotNull(result);
        assertEquals("Updated Taj Resort", result.getName());
        assertEquals("Sinquerim Beach, Goa", result.getLocation());
        assertEquals("Renovated 5-star beachfront property.", result.getDescription());
        verify(hotelRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).save(sampleHotel);
    }

    @Test
    void testUpdateHotel_NotFound() {
        Hotel updateInfo = new Hotel("Updated Name", "Location", "Desc", "url");
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        Hotel result = hotelService.updateHotel(999L, updateInfo);

        assertNull(result);
        verify(hotelRepository, times(1)).findById(999L);
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void testDeleteHotel_Success() {
        when(hotelRepository.existsById(1L)).thenReturn(true);
        doNothing().when(hotelRepository).deleteById(1L);

        boolean result = hotelService.deleteHotel(1L);

        assertTrue(result);
        verify(hotelRepository, times(1)).existsById(1L);
        verify(hotelRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteHotel_NotFound() {
        when(hotelRepository.existsById(999L)).thenReturn(false);

        boolean result = hotelService.deleteHotel(999L);

        assertFalse(result);
        verify(hotelRepository, times(1)).existsById(999L);
        verify(hotelRepository, never()).deleteById(anyLong());
    }
}
