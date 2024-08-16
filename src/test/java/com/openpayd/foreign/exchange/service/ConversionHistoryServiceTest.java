package com.openpayd.foreign.exchange.service;

import com.openpayd.foreign.exchange.controller.request.ConversionHistoryRequest;
import com.openpayd.foreign.exchange.controller.response.PageResponse;
import com.openpayd.foreign.exchange.domain.ConversionHistory;
import com.openpayd.foreign.exchange.repository.ConversionHistoryRepository;
import com.openpayd.foreign.exchange.service.impl.ConversionHistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversionHistoryServiceTest {

    @InjectMocks
    private ConversionHistoryServiceImpl conversionHistoryService;

    @Mock
    private ConversionHistoryRepository conversionHistoryRepository;

    @Mock
    private Pageable pageable;

    @Test
    void testGetConversionHistory_SuccessfulRetrieval() {
        // Arrange
        ConversionHistoryRequest request = new ConversionHistoryRequest("USD", "EUR", null, null);
        ConversionHistory history = new ConversionHistory();
        Page<ConversionHistory> page = new PageImpl<>(List.of(history));

        when(conversionHistoryRepository.findByCriteria(
                any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PageResponse<ConversionHistory> response =
                conversionHistoryService.getConversionHistory(request, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.totalElements());
        assertEquals(1, response.totalPages());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertEquals(1, response.data().size());
    }

    @Test
    void testGetConversionHistory_EmptyResult() {
        // Arrange
        ConversionHistoryRequest request = new ConversionHistoryRequest("USD", "EUR", null, null);
        Page<ConversionHistory> page = new PageImpl<>(Collections.emptyList());

        when(conversionHistoryRepository.findByCriteria(
                any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PageResponse<ConversionHistory> response =
                conversionHistoryService.getConversionHistory(request, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.totalElements());
        assertEquals(1, response.totalPages());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertTrue(response.data().isEmpty());
    }

    @Test
    void testSaveConversionHistory_SuccessfulSave() {
        // Arrange
        ConversionHistory history = new ConversionHistory();

        // Act
        conversionHistoryService.saveConversionHistory(history);

        // Assert
        verify(conversionHistoryRepository, times(1)).save(history);
    }

    @Test
    void testGetConversionHistory_ExceptionHandling() {
        // Arrange
        ConversionHistoryRequest request = new ConversionHistoryRequest("USD", "EUR", null, null);
        when(conversionHistoryRepository.findByCriteria(
                any(), any(), any(), any(), any(Pageable.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception =
                assertThrows(
                        RuntimeException.class,
                        () -> {
                            conversionHistoryService.getConversionHistory(request, pageable);
                        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testSaveConversionHistory_ExceptionHandling() {
        // Arrange
        ConversionHistory history = new ConversionHistory();
        doThrow(new RuntimeException("Database error")).when(conversionHistoryRepository).save(any());

        // Act & Assert
        Exception exception =
                assertThrows(
                        RuntimeException.class,
                        () -> {
                            conversionHistoryService.saveConversionHistory(history);
                        });

        assertEquals("Database error", exception.getMessage());
    }
}
