package com.openpayd.foreign.exchange.controller;

import com.openpayd.foreign.exchange.controller.request.ExchangeRateRequest;
import com.openpayd.foreign.exchange.controller.response.ExchangeRateResponse;
import com.openpayd.foreign.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Foreign Exchange Rate", description = "API for retrieving currency exchange rates")
public class ExchangeRateController {

  private final ExchangeRateService exchangeRateService;

  public ExchangeRateController(ExchangeRateService exchangeRateService) {
    this.exchangeRateService = exchangeRateService;
  }

  @Operation(
      summary = "Get Exchange Rates",
      description =
          "Retrieves the current exchange rates between specified currencies based on the provided request details. The endpoint fetches the latest exchange rates without performing any conversions.")
  @PostMapping("/exchange-rate")
  public ExchangeRateResponse getExchangeRates(@RequestBody ExchangeRateRequest request) {
    return exchangeRateService.getExchangeRates(request);
  }
}
