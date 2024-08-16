package com.openpayd.foreign.exchange.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(
    description =
        "Request object for converting currency. Includes details about the source and target currencies, and the amount to convert.")
public record ConversionRequest(
    @Schema(
            description =
                "Details for the currency exchange request including the source and target currencies.",
            required = true)
        @NotNull(message = "Exchange rate request must not be null")
        ExchangeRateRequest exchangeRateRequest,
    @Schema(
            description = "The amount of currency to convert. Must be a positive value.",
            example = "100.00",
            required = true)
        @NotNull(message = "Amount must not be null")
        @Positive(message = "Amount must be positive")
        BigDecimal amount) {}
