package com.openpayd.foreign.exchange.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
    description =
        "Response object for the currency exchange rate API, including the target currency and the exchange rate.")
public record ExchangeRateResponse(
    @Schema(
            description =
                "The target currency code for which the exchange rate is provided (e.g., 'USD').",
            example = "USD",
            required = true)
        String currency,
    @Schema(
            description = "The exchange rate from the source currency to the target currency.",
            example = "1.097381",
            required = true)
        BigDecimal rate) {}
