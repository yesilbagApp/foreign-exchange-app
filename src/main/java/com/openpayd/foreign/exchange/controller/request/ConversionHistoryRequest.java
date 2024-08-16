package com.openpayd.foreign.exchange.controller.request;

import com.openpayd.foreign.exchange.exception.InvalidConversionHistoryRequestException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(
        description =
                "Details for retrieving conversion history based on criteria such as currency codes and transaction dates.")
public record ConversionHistoryRequest(
        @Schema(
                description = "The source currency code (e.g., 'USD'). Must be exactly 3 characters.",
                example = "USD")
        @Size(max = 3, message = "From currency code must be 3 characters")
        String from,
        @Schema(
                description = "The target currency code (e.g., 'EUR'). Must be exactly 3 characters.",
                example = "EUR")
        @Size(max = 3, message = "To currency code must be 3 characters")
        String to,
        @Schema(description = "The start date and time of the conversion transaction.")
        LocalDateTime startDate,
        @Schema(description = "The end date and time of the conversion transaction.")
        LocalDateTime endDate) {

    // Validation logic
    public ConversionHistoryRequest {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new InvalidConversionHistoryRequestException(
                        "Start date must be before or equal to end date.");
            }
        } else if (startDate == null && endDate == null) {
            if (from == null || from.isEmpty() || to == null || to.isEmpty()) {
                throw new InvalidConversionHistoryRequestException(
                        "At least one of 'from' and 'to' or 'startDate' and 'endDate' must be provided.");
            }
        } else {
            throw new InvalidConversionHistoryRequestException(
                    "If one of 'startDate' or 'endDate' is provided, the other must also be provided.");
        }
    }
}
