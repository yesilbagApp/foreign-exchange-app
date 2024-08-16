package com.openpayd.foreign.exchange.service;

import com.openpayd.foreign.exchange.controller.request.ConversionRequest;
import com.openpayd.foreign.exchange.controller.response.ConversionResponse;

public interface ConversionService {
  ConversionResponse convertCurrency(ConversionRequest conversionRequest);
}
