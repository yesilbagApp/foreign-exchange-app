package com.openpayd.foreign.exchange.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
    description =
        "Request object for specifying currency exchange details, including source and target currencies.")
public record ExchangeRateRequest(
    @Schema(
            description =
                "The source currency code from which the conversion starts (e.g., 'USD').",
            example = "USD",
            required = true)
        @NotNull(message = "From currency must not be null")
        String from,
    @Schema(
            description = "The target currency code to which the conversion is made (e.g., 'EUR').",
            example = "EUR",
            required = true)
        @NotNull(message = "To currency must not be null")
        String to) {}
