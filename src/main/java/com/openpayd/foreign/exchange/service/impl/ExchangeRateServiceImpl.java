package com.openpayd.foreign.exchange.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.foreign.exchange.controller.request.ExchangeRateRequest;
import com.openpayd.foreign.exchange.controller.response.ExchangeRateResponse;
import com.openpayd.foreign.exchange.exception.ExchangeRateException;
import com.openpayd.foreign.exchange.service.ExchangeRateService;
import com.openpayd.foreign.exchange.service.HttpClientWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static final Logger LOGGER = Logger.getLogger(ExchangeRateServiceImpl.class.getName());
    private static final String API_URL = "https://apilayer.net/api/live?access_key=%s&currencies=%s&source=%s&format=1";
    private final HttpClientWrapper httpClientWrapper;
    private final ObjectMapper objectMapper;

    @Value("${currencylayer.api.key}")
    private String apiKey;

    public ExchangeRateServiceImpl(HttpClientWrapper httpClientWrapper, ObjectMapper objectMapper) {
        this.httpClientWrapper = httpClientWrapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public ExchangeRateResponse getExchangeRates(ExchangeRateRequest request) {
        // Build the API URL based on the request details
        String url = buildUrl(request);

        try {
            // Create an HTTP GET request using the constructed URL
            var httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            // Send the HTTP request using the HttpClientWrapper and get the response
            var response = httpClientWrapper.send(httpRequest);

            //Handle the response and return the exchange rate data
            return handleResponse(response, request);
        } catch (Exception exception) {
            // Log the exception and throw a custom ExchangeRateException in case of failure
            LOGGER.log(Level.SEVERE, "Error occurred while calling API", exception);
            throw new ExchangeRateException(exception.getMessage());
        }
    }

    // Helper method to construct the API URL using the provided request data
    private String buildUrl(ExchangeRateRequest request) {
        return String.format(API_URL, apiKey, request.to(), request.from());
    }

    // Helper method to process the API response and extract the exchange rate data
    private ExchangeRateResponse handleResponse(HttpResponse<String> response, ExchangeRateRequest request) throws Exception {
        if (response.statusCode() == 200) {
            // Parse the JSON response
            var jsonResponse = objectMapper.readTree(response.body());

            // Check if the API call was successful
            if (jsonResponse.get("success").asBoolean()) {

                // Extract the source currency and rate from the response
                var source = jsonResponse.path("source").asText();
                var rate = extractRate(jsonResponse, request);

                // Return an ExchangeRateResponse object with the extracted data
                return new ExchangeRateResponse(source, rate);
            } else {
                // Throw an exception if the API response indicates failure
                throw new ExchangeRateException("API response indicates failure: " + response.body());
            }
        } else {
            // Throw an exception if the API call fails with a non-200 status code
            throw new ExchangeRateException("API call failed with status code: " + response.statusCode());
        }
    }

    // Helper method to extract the exchange rate from the JSON response
    private BigDecimal extractRate(JsonNode jsonResponse, ExchangeRateRequest request) {
        // Navigate to the "quotes" node in the JSON response
        var quotesNode = jsonResponse.path("quotes");

        // Extract the exchange rate for the specified currency pair (e.g., "USDEUR")
        var rate = quotesNode.path(String.format("%s%s", request.from(), request.to())).asText();

        // Convert the rate to BigDecimal and return it
        return new BigDecimal(rate);
    }
}
