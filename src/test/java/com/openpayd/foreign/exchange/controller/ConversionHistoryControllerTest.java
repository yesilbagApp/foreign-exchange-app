package com.openpayd.foreign.exchange.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openpayd.foreign.exchange.controller.request.ConversionHistoryRequest;
import com.openpayd.foreign.exchange.controller.response.PageResponse;
import com.openpayd.foreign.exchange.domain.ConversionHistory;
import com.openpayd.foreign.exchange.exception.ConversionHistoryException;
import com.openpayd.foreign.exchange.service.ConversionHistoryService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConversionHistoryController.class)
public class ConversionHistoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ConversionHistoryService conversionHistoryService;


  @Test
  public void testGetConversionHistory_Success() throws Exception {
    // Prepare data
    ConversionHistory history = new ConversionHistory();
    history.setId(1L);
    history.setFromCurrency("USD");
    history.setToCurrency("EUR");
    history.setAmount(BigDecimal.valueOf(100));
    history.setConvertedAmount(BigDecimal.valueOf(110));
    history.setTransactionDateTime(LocalDateTime.now());

    List<ConversionHistory> historyList = List.of(history);
    Page<ConversionHistory> page = new PageImpl<>(historyList);

    // Mock service
    when(conversionHistoryService.getConversionHistory(
            any(ConversionHistoryRequest.class), any(Pageable.class)))
        .thenReturn(
            new PageResponse<>(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.getContent()));

    // Perform the test
    mockMvc
        .perform(
            get("/api/v1/history")
                .param("from", "USD")
                .param("to", "EUR")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.data", hasSize(1)))
        .andExpect(jsonPath("$.data[0].fromCurrency").value("USD"))
        .andExpect(jsonPath("$.data[0].toCurrency").value("EUR"))
        .andExpect(jsonPath("$.data[0].amount").value(100))
        .andExpect(jsonPath("$.data[0].convertedAmount").value(110));
  }

  @Test
  public void testGetConversionHistory_Empty() throws Exception {
    // Mock service
    when(conversionHistoryService.getConversionHistory(
            any(ConversionHistoryRequest.class), any(Pageable.class)))
        .thenReturn(new PageResponse<>(0, 0, 0, 0, false, false, List.of()));

    // Perform the test
    mockMvc
        .perform(
            get("/api/v1/history")
                .param("from", "USD")
                .param("to", "EUR")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(0))
        .andExpect(jsonPath("$.totalPages").value(0))
        .andExpect(jsonPath("$.data", hasSize(0)));
  }

  @Test
  public void testGetConversionHistory_InvalidParameters() throws Exception {
    // Perform the test with invalid parameters
    mockMvc
        .perform(
            get("/api/v1/history")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message")
                .value("At least one of 'from' and 'to' or 'startDate' and 'endDate' must be provided."));
  }

  @Test
  public void testGetConversionHistory_ServiceException() throws Exception {
    // Mock service to throw exception
    when(conversionHistoryService.getConversionHistory(
            any(ConversionHistoryRequest.class), any(Pageable.class)))
        .thenThrow(new ConversionHistoryException("Service error"));

    // Perform the test
    mockMvc
        .perform(
            get("/api/v1/history")
                .param("from", "USD")
                .param("to", "EUR")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Service error"));
  }
}
