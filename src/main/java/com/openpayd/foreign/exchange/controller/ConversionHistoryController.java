package com.openpayd.foreign.exchange.controller;

import com.openpayd.foreign.exchange.controller.request.ConversionHistoryRequest;
import com.openpayd.foreign.exchange.controller.response.PageResponse;
import com.openpayd.foreign.exchange.domain.ConversionHistory;
import com.openpayd.foreign.exchange.service.ConversionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "ConversionHistory", description = "API for conversion history")
public class ConversionHistoryController {

  private final ConversionHistoryService conversionHistoryService;

  public ConversionHistoryController(ConversionHistoryService conversionHistoryService) {
    this.conversionHistoryService = conversionHistoryService;
  }

  @Operation(
      summary = "Get Conversion History",
      description =
          "Retrieve a paginated list of conversion histories based on the specified criteria.")
  @GetMapping("/history")
  public PageResponse<ConversionHistory> getConversionHistory(
      ConversionHistoryRequest request, Pageable pageable) {
    return conversionHistoryService.getConversionHistory(request, pageable);
  }
}
