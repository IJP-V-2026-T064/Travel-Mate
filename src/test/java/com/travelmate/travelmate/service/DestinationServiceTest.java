package com.travelmate.travelmate.service;

import com.travelmate.travelmate.entity.Destination;
import com.travelmate.travelmate.repository.DestinationRepository;
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
class DestinationServiceTest {

    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private DestinationService destinationService;

    private Destination sampleDestination;

    @BeforeEach
    void setUp() {
        sampleDestination = new Destination(
                1L,
                "Goa Beaches",
                "Goa, India",
                "Sun, sand, and scenic coastal views.",
                "https://example.com/goa.jpg"
        );
    }

    @Test
    void testGetAllDestinations() {
        List<Destination> list = Arrays.asList(
                sampleDestination,
                new Destination(2L, "Manali", "Himachal Pradesh, India", "Snow peaks and adventures", "https://example.com/manali.jpg")
        );
        when(destinationRepository.findAll()).thenReturn(list);

        List<Destination> result = destinationService.getAllDestinations();

        assertEquals(2, result.size());
        assertEquals("Goa Beaches", result.get(0).getName());
        verify(destinationRepository, times(1)).findAll();
    }

    @Test
    void testGetDestinationById_Found() {
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(sampleDestination));

        Destination result = destinationService.getDestinationById(1L);

        assertNotNull(result);
        assertEquals("Goa Beaches", result.getName());
        assertEquals("Goa, India", result.getLocation());
        verify(destinationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDestinationById_NotFound() {
        when(destinationRepository.findById(99L)).thenReturn(Optional.empty());

        Destination result = destinationService.getDestinationById(99L);

        assertNull(result);
        verify(destinationRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateDestination() {
        Destination toCreate = new Destination("Jaipur", "Rajasthan, India", "Historic palaces and forts", "https://example.com/jaipur.jpg");
        Destination saved = new Destination(3L, "Jaipur", "Rajasthan, India", "Historic palaces and forts", "https://example.com/jaipur.jpg");

        when(destinationRepository.save(any(Destination.class))).thenReturn(saved);

        Destination result = destinationService.createDestination(toCreate);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Jaipur", result.getName());
        verify(destinationRepository, times(1)).save(toCreate);
    }

    @Test
    void testUpdateDestination_Success() {
        Destination updateInfo = new Destination("Updated Goa", "South Goa, India", "Quiet beaches", "https://example.com/south-goa.jpg");

        when(destinationRepository.findById(1L)).thenReturn(Optional.of(sampleDestination));
        when(destinationRepository.save(any(Destination.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Destination result = destinationService.updateDestination(1L, updateInfo);

        assertNotNull(result);
        assertEquals("Updated Goa", result.getName());
        assertEquals("South Goa, India", result.getLocation());
        assertEquals("Quiet beaches", result.getDescription());
        verify(destinationRepository, times(1)).findById(1L);
        verify(destinationRepository, times(1)).save(sampleDestination);
    }

    @Test
    void testUpdateDestination_NotFound() {
        Destination updateInfo = new Destination("Updated Name", "Location", "Desc", "url");
        when(destinationRepository.findById(999L)).thenReturn(Optional.empty());

        Destination result = destinationService.updateDestination(999L, updateInfo);

        assertNull(result);
        verify(destinationRepository, times(1)).findById(999L);
        verify(destinationRepository, never()).save(any());
    }

    @Test
    void testDeleteDestination_Success() {
        when(destinationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(destinationRepository).deleteById(1L);

        boolean result = destinationService.deleteDestination(1L);

        assertTrue(result);
        verify(destinationRepository, times(1)).existsById(1L);
        verify(destinationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDestination_NotFound() {
        when(destinationRepository.existsById(999L)).thenReturn(false);

        boolean result = destinationService.deleteDestination(999L);

        assertFalse(result);
        verify(destinationRepository, times(1)).existsById(999L);
        verify(destinationRepository, never()).deleteById(anyLong());
    }
}
