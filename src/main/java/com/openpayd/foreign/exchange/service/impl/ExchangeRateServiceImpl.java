package com.openpayd.foreign.exchange.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.foreign.exchange.controller.request.ExchangeRateRequest;
import com.openpayd.foreign.exchange.controller.response.ExchangeRateResponse;
import com.openpayd.foreign.exchange.exception.ExchangeRateException;
import com.openpayd.foreign.exchange.service.ExchangeRateService;
import com.openpayd.foreign.exchange.service.HttpClientWrapper;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

  private static final Logger LOGGER = Logger.getLogger(ExchangeRateServiceImpl.class.getName());
  private static final String API_URL =
      "https://apilayer.net/api/live?access_key=%s&currencies=%s&source=%s&format=1";
  private final HttpClientWrapper httpClientWrapper;
  private final ObjectMapper objectMapper;

  @Value("${fixer.api.key}")
  private String apiKey;

  public ExchangeRateServiceImpl(HttpClientWrapper httpClientWrapper, ObjectMapper objectMapper) {
    this.httpClientWrapper = httpClientWrapper;
    this.objectMapper = objectMapper;
  }

  @Override
  public ExchangeRateResponse getExchangeRates(ExchangeRateRequest request) {
    String url = buildUrl(request);

    try {
      var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

      var response = httpClientWrapper.send(httpRequest);
      return handleResponse(response, request);
    } catch (Exception exception) {
      LOGGER.log(Level.SEVERE, "Error occurred while calling API", exception);
      throw new ExchangeRateException(exception.getMessage());
    }
  }

  private String buildUrl(ExchangeRateRequest request) {
    return String.format(API_URL, apiKey, request.to(), request.from());
  }

  private ExchangeRateResponse handleResponse(
      HttpResponse<String> response, ExchangeRateRequest request) throws Exception {
    if (response.statusCode() == 200) {
      var jsonResponse = objectMapper.readTree(response.body());

      if (jsonResponse.get("success").asBoolean()) {
        var source = jsonResponse.path("source").asText();
        var rate = extractRate(jsonResponse, request);

        return new ExchangeRateResponse(source, rate);
      } else {
        throw new ExchangeRateException("API response indicates failure: " + response.body());
      }
    } else {
      throw new ExchangeRateException("API call failed with status code: " + response.statusCode());
    }
  }

  private BigDecimal extractRate(JsonNode jsonResponse, ExchangeRateRequest request) {
    var quotesNode = jsonResponse.path("quotes");
    var rate = quotesNode.path(String.format("%s%s", request.from(), request.to())).asText();
    return new BigDecimal(rate);
  }
}
