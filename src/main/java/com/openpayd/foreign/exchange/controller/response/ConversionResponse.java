package com.openpayd.foreign.exchange.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Response object containing the result of a currency conversion operation.")
public record ConversionResponse(
    @Schema(
            description = "Indicates whether the currency conversion was successful.",
            example = "true")
        boolean success,
    @Schema(
            description = "The date and time when the conversion transaction occurred.",
            example = "2024-08-15T15:45:04")
        LocalDateTime transactionDateTime,
    @Schema(description = "The source currency code (e.g., 'USD').", example = "USD") String source,
    @Schema(description = "The conversion rate applied for the transaction.", example = "1.098847")
        BigDecimal rate,
    @Schema(description = "The result amount after conversion.", example = "6834.83")
        BigDecimal result) {}
