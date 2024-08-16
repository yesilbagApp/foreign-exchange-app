package com.openpayd.foreign.exchange.controller;

import com.openpayd.foreign.exchange.controller.request.ConversionRequest;
import com.openpayd.foreign.exchange.controller.response.ConversionResponse;
import com.openpayd.foreign.exchange.service.ConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Conversion", description = "API for currency conversion operations")
public class ConversionController {

    private final ConversionService conversionService;

    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Operation(
            summary = "Convert Currency",
            description =
                    "Converts a specified amount from one currency to another based on the request details.")
    @PostMapping("/convert")
    public ConversionResponse convertCurrency(@RequestBody ConversionRequest request) {
        return conversionService.convertCurrency(request);
    }
}
