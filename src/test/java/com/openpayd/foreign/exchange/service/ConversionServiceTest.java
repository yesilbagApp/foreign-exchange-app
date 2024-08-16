package com.openpayd.foreign.exchange.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.openpayd.foreign.exchange.controller.request.ConversionRequest;
import com.openpayd.foreign.exchange.controller.request.ExchangeRateRequest;
import com.openpayd.foreign.exchange.controller.response.ConversionResponse;
import com.openpayd.foreign.exchange.controller.response.ExchangeRateResponse;
import com.openpayd.foreign.exchange.domain.ConversionHistory;
import com.openpayd.foreign.exchange.service.impl.ConversionServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ConversionServiceTest {

  @InjectMocks private ConversionServiceImpl conversionService;

  @Mock private ExchangeRateService exchangeRateService;

  @Mock private ConversionHistoryService conversionHistoryService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testConvertCurrency_Success() {
    // Arrange
    ConversionRequest conversionRequest =
        new ConversionRequest(new ExchangeRateRequest("USD", "EUR"), new BigDecimal("100"));

    ExchangeRateResponse exchangeRateResponse =
        new ExchangeRateResponse("USD", new BigDecimal("1.1"));

    BigDecimal expectedResult = new BigDecimal("110.00");
    ConversionHistory expectedHistory =
        ConversionHistory.builder()
            .fromCurrency("USD")
            .toCurrency("EUR")
            .amount(new BigDecimal("100"))
            .rate(new BigDecimal("1.1"))
            .convertedAmount(expectedResult)
            .transactionDateTime(LocalDateTime.now())
            .build();

    when(exchangeRateService.getExchangeRates(any())).thenReturn(exchangeRateResponse);

    // Act
    ConversionResponse response = conversionService.convertCurrency(conversionRequest);

    // Assert
    verify(conversionHistoryService, times(1)).saveConversionHistory(any(ConversionHistory.class));
    assertTrue(response.success());
    assertEquals("USD", response.source());
    assertEquals(0, exchangeRateResponse.rate().compareTo(new BigDecimal("1.1")));
    assertEquals(0, response.rate().compareTo(new BigDecimal("1.1")));
    assertEquals(0, response.result().compareTo(expectedResult));
    // Additional checks for date and amount can be done as needed
  }
}
