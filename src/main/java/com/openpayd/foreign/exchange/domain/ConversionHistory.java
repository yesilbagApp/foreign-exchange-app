package com.openpayd.foreign.exchange.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversion_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entity representing the conversion history of currency transactions.")
public class ConversionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the conversion history record.", example = "1")
    private Long id;

    @Column(name = "from_currency", nullable = false)
    @Schema(description = "The source currency code (e.g., 'USD').", example = "USD")
    private String fromCurrency;

    @Column(name = "to_currency", nullable = false)
    @Schema(description = "The target currency code (e.g., 'EUR').", example = "EUR")
    private String toCurrency;

    @Column(name = "amount", nullable = false)
    @Schema(description = "The amount of currency that was converted.", example = "100.00")
    private BigDecimal amount;

    @Column(name = "rate", nullable = false, precision = 20, scale = 10)
    @Schema(description = "The conversion rate applied to the transaction.", example = "1.098847")
    private BigDecimal rate;

    @Column(name = "converted_amount", nullable = false)
    @Schema(description = "The amount of currency after conversion.", example = "109.88")
    private BigDecimal convertedAmount;

    @Column(name = "transaction_date", nullable = false)
    @Schema(
            description = "The date and time when the conversion transaction occurred.",
            example = "2024-08-15T15:45:04")
    private LocalDateTime transactionDateTime;
}
