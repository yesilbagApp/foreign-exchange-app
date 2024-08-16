package com.openpayd.foreign.exchange.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.foreign.exchange.controller.request.ExchangeRateRequest;
import com.openpayd.foreign.exchange.controller.response.ExchangeRateResponse;
import com.openpayd.foreign.exchange.exception.ExchangeRateException;
import com.openpayd.foreign.exchange.service.impl.ExchangeRateServiceImpl;
import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

  @Mock private HttpClientWrapper httpClientWrapper;

  @Spy private ObjectMapper objectMapper;

  @InjectMocks private ExchangeRateServiceImpl exchangeRateService;

  @Test
  void testGetExchangeRates_SuccessfulResponse() throws Exception {

    ExchangeRateRequest request = new ExchangeRateRequest("USD", "EUR");
    // Mock HTTP response
    HttpResponse<String> mockResponse = mock(HttpResponse.class);
    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body())
        .thenReturn("{\"success\":true,\"source\":\"USD\",\"quotes\":{\"USDEUR\":\"0.85\"}}");

    when(httpClientWrapper.send(any(HttpRequest.class))).thenReturn(mockResponse);

    // Mock ObjectMapper response
    var mockJsonNode = objectMapper.readTree(mockResponse.body());
    when(objectMapper.readTree(anyString())).thenReturn(mockJsonNode);

    // Execute method
    ExchangeRateResponse response = exchangeRateService.getExchangeRates(request);

    // Verify results
    assertNotNull(response);
    assertEquals("USD", response.currency());
    assertEquals(new BigDecimal("0.85"), response.rate());
  }

  @Test
  void testGetExchangeRates_ApiFailure() throws Exception {
    // Arrange
    ExchangeRateRequest request = new ExchangeRateRequest("USD", "EUR");
    HttpResponse<String> response = mock(HttpResponse.class);

    when(httpClientWrapper.send(any(HttpRequest.class))).thenReturn(response);
    when(response.statusCode()).thenReturn(500);

    // Act & Assert
    ExchangeRateException thrownException =
        assertThrows(
            ExchangeRateException.class, () -> exchangeRateService.getExchangeRates(request));
    assertTrue(thrownException.getMessage().contains("API call failed with status code: 500"));
  }

  @Test
  void testGetExchangeRates_ApiSuccess_ButNotSuccessful() throws Exception {
    // Arrange
    ExchangeRateRequest request = new ExchangeRateRequest("USD", "EUR");
    String jsonResponseBody = "{\"success\":false}";
    HttpResponse<String> response = mock(HttpResponse.class);
    JsonNode jsonResponseNode = mock(JsonNode.class);

    when(httpClientWrapper.send(any(HttpRequest.class))).thenReturn(response);
    when(response.statusCode()).thenReturn(200);
    when(response.body()).thenReturn(jsonResponseBody);
    when(objectMapper.readTree(jsonResponseBody)).thenReturn(jsonResponseNode);
    when(jsonResponseNode.get("success")).thenReturn(mock(JsonNode.class));
    when(jsonResponseNode.get("success").asBoolean()).thenReturn(false);

    // Act & Assert
    ExchangeRateException thrownException =
        assertThrows(
            ExchangeRateException.class, () -> exchangeRateService.getExchangeRates(request));
    assertTrue(thrownException.getMessage().contains("API response indicates failure"));
  }
}
