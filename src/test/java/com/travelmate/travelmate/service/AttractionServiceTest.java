package com.travelmate.travelmate.service;

import com.travelmate.travelmate.entity.Attraction;
import com.travelmate.travelmate.repository.AttractionRepository;
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
class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private AttractionService attractionService;

    private Attraction sampleAttraction;

    @BeforeEach
    void setUp() {
        sampleAttraction = new Attraction(
                1L,
                "Aguada Fort",
                "Candolim, Goa",
                "A well-preserved seventeenth-century Portuguese fort and lighthouse overlooking the Arabian Sea.",
                "https://example.com/aguada.jpg"
        );
    }

    @Test
    void testGetAllAttractions() {
        List<Attraction> list = Arrays.asList(
                sampleAttraction,
                new Attraction(2L, "Dudhsagar Falls", "Sonaulim, Goa", "Four-tiered waterfall on the Mandovi River.", "https://example.com/dudhsagar.jpg")
        );
        when(attractionRepository.findAll()).thenReturn(list);

        List<Attraction> result = attractionService.getAllAttractions();

        assertEquals(2, result.size());
        assertEquals("Aguada Fort", result.get(0).getName());
        verify(attractionRepository, times(1)).findAll();
    }

    @Test
    void testGetAttractionById_Found() {
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(sampleAttraction));

        Attraction result = attractionService.getAttractionById(1L);

        assertNotNull(result);
        assertEquals("Aguada Fort", result.getName());
        assertEquals("Candolim, Goa", result.getLocation());
        verify(attractionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAttractionById_NotFound() {
        when(attractionRepository.findById(99L)).thenReturn(Optional.empty());

        Attraction result = attractionService.getAttractionById(99L);

        assertNull(result);
        verify(attractionRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateAttraction() {
        Attraction toCreate = new Attraction("Hawa Mahal", "Jaipur, Rajasthan", "Palace of Winds made of pink sandstone.", "https://example.com/hawamahal.jpg");
        Attraction saved = new Attraction(3L, "Hawa Mahal", "Jaipur, Rajasthan", "Palace of Winds made of pink sandstone.", "https://example.com/hawamahal.jpg");

        when(attractionRepository.save(any(Attraction.class))).thenReturn(saved);

        Attraction result = attractionService.createAttraction(toCreate);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Hawa Mahal", result.getName());
        verify(attractionRepository, times(1)).save(toCreate);
    }

    @Test
    void testUpdateAttraction_Success() {
        Attraction updateInfo = new Attraction("Updated Aguada Fort", "Sinquerim Beach, Goa", "Historic fort with sunset views.", "https://example.com/aguada-updated.jpg");

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(sampleAttraction));
        when(attractionRepository.save(any(Attraction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Attraction result = attractionService.updateAttraction(1L, updateInfo);

        assertNotNull(result);
        assertEquals("Updated Aguada Fort", result.getName());
        assertEquals("Sinquerim Beach, Goa", result.getLocation());
        assertEquals("Historic fort with sunset views.", result.getDescription());
        verify(attractionRepository, times(1)).findById(1L);
        verify(attractionRepository, times(1)).save(sampleAttraction);
    }

    @Test
    void testUpdateAttraction_NotFound() {
        Attraction updateInfo = new Attraction("Updated Name", "Location", "Desc", "url");
        when(attractionRepository.findById(999L)).thenReturn(Optional.empty());

        Attraction result = attractionService.updateAttraction(999L, updateInfo);

        assertNull(result);
        verify(attractionRepository, times(1)).findById(999L);
        verify(attractionRepository, never()).save(any());
    }

    @Test
    void testDeleteAttraction_Success() {
        when(attractionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(attractionRepository).deleteById(1L);

        boolean result = attractionService.deleteAttraction(1L);

        assertTrue(result);
        verify(attractionRepository, times(1)).existsById(1L);
        verify(attractionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAttraction_NotFound() {
        when(attractionRepository.existsById(999L)).thenReturn(false);

        boolean result = attractionService.deleteAttraction(999L);

        assertFalse(result);
        verify(attractionRepository, times(1)).existsById(999L);
        verify(attractionRepository, never()).deleteById(anyLong());
    }
}
