package com.openpayd.foreign.exchange.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openpayd.foreign.exchange.controller.request.ExchangeRateRequest;
import com.openpayd.foreign.exchange.controller.response.ExchangeRateResponse;
import com.openpayd.foreign.exchange.exception.ExchangeRateException;
import com.openpayd.foreign.exchange.service.ExchangeRateService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ExchangeRateService exchangeRateService;

  @Test
  public void testGetExchangeRate_Success() throws Exception {
    // Setup mock response
    ExchangeRateResponse mockResponse = new ExchangeRateResponse("EUR", BigDecimal.valueOf(1.1));

    when(exchangeRateService.getExchangeRates(any(ExchangeRateRequest.class)))
        .thenReturn(mockResponse);

    // Perform POST request and verify the response
    mockMvc
        .perform(
            post("/api/v1/exchange-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"from\":\"USD\",\"to\":\"EUR\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currency").value("EUR"))
        .andExpect(jsonPath("$.rate").value(1.1));
  }

  @Test
  public void testGetExchangeRate_Failure() throws Exception {
    // Setup mock response
    when(exchangeRateService.getExchangeRates(any(ExchangeRateRequest.class)))
        .thenThrow(new ExchangeRateException("Conversion failed"));

    // Perform POST request and verify the response
    mockMvc
        .perform(
            post("/api/v1/exchange-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"from\":\"USD\",\"to\":\"EUR\"}"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Conversion failed"));
  }
}
