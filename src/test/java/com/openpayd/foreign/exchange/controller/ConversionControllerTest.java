package com.openpayd.foreign.exchange.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.foreign.exchange.controller.request.ConversionRequest;
import com.openpayd.foreign.exchange.controller.request.ExchangeRateRequest;
import com.openpayd.foreign.exchange.controller.response.ConversionResponse;
import com.openpayd.foreign.exchange.exception.ExchangeRateException;
import com.openpayd.foreign.exchange.service.ConversionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConversionController.class)
public class ConversionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ConversionService conversionService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  public void testConvertCurrency_Success() throws Exception {
    // Arrange
    ConversionResponse mockResponse =
        new ConversionResponse(
            true, LocalDateTime.now(), "USD", BigDecimal.valueOf(0.91), BigDecimal.valueOf(91.0));
    Mockito.when(conversionService.convertCurrency(any(ConversionRequest.class)))
        .thenReturn(mockResponse);

    ConversionRequest request =
        new ConversionRequest(new ExchangeRateRequest("USD", "EUR"), BigDecimal.valueOf(100));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/v1/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.rate").value(0.91))
        .andExpect(jsonPath("$.result").value(91.0));
  }

  @Test
  public void testConvertCurrency_Exception() throws Exception {
    // Arrange
    Mockito.when(conversionService.convertCurrency(any(ConversionRequest.class)))
        .thenThrow(new ExchangeRateException("Conversion failed"));

    ConversionRequest request =
        new ConversionRequest(new ExchangeRateRequest("USD", "EUR"), BigDecimal.valueOf(100));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/v1/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Conversion failed"));
  }
}
