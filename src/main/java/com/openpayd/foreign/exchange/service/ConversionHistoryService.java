package com.openpayd.foreign.exchange.service;

import com.openpayd.foreign.exchange.controller.request.ConversionHistoryRequest;
import com.openpayd.foreign.exchange.controller.response.PageResponse;
import com.openpayd.foreign.exchange.domain.ConversionHistory;
import org.springframework.data.domain.Pageable;

public interface ConversionHistoryService {
  PageResponse<ConversionHistory> getConversionHistory(ConversionHistoryRequest request, Pageable pageable);

  void saveConversionHistory(ConversionHistory conversionHistory);
}
