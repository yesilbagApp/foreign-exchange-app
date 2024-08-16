package com.openpayd.foreign.exchange.service.impl;

import com.openpayd.foreign.exchange.controller.request.ConversionRequest;
import com.openpayd.foreign.exchange.controller.response.ConversionResponse;
import com.openpayd.foreign.exchange.domain.ConversionHistory;
import com.openpayd.foreign.exchange.service.ConversionHistoryService;
import com.openpayd.foreign.exchange.service.ConversionService;
import com.openpayd.foreign.exchange.service.ExchangeRateService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class ConversionServiceImpl implements ConversionService {

  private final ExchangeRateService exchangeRateService;
  private final ConversionHistoryService
      conversionHistoryService; // Assuming this service is required

  public ConversionServiceImpl(
      ExchangeRateService exchangeRateService, ConversionHistoryService conversionHistoryService) {
    this.exchangeRateService = exchangeRateService;
    this.conversionHistoryService = conversionHistoryService;
  }

  @Override
  public ConversionResponse convertCurrency(ConversionRequest conversionRequest) {
    // Get exchange rate response from ForeignExchangeRateService
    var exchangeRateResponse =
        exchangeRateService.getExchangeRates(conversionRequest.exchangeRateRequest());

    // Calculate result
    var result = exchangeRateResponse.rate().multiply(conversionRequest.amount());

    // Create ConversionHistory object
    var conversionHistory =
        ConversionHistory.builder()
            .fromCurrency(conversionRequest.exchangeRateRequest().from())
            .toCurrency(conversionRequest.exchangeRateRequest().to())
            .amount(conversionRequest.amount())
            .rate(exchangeRateResponse.rate())
            .convertedAmount(result)
            .transactionDateTime(LocalDateTime.now())
            .build();

    // Save conversion history
    conversionHistoryService.saveConversionHistory(conversionHistory);

    // Create and return ConversionResponse object
    return new ConversionResponse(
        true,
        LocalDateTime.now(),
        conversionRequest.exchangeRateRequest().from(),
        exchangeRateResponse.rate(),
        result);
  }
}
