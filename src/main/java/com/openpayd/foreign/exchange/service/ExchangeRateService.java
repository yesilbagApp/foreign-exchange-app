package com.openpayd.foreign.exchange.service;

import com.openpayd.foreign.exchange.controller.request.ExchangeRateRequest;
import com.openpayd.foreign.exchange.controller.response.ExchangeRateResponse;

public interface ExchangeRateService {
    ExchangeRateResponse getExchangeRates(ExchangeRateRequest request);
}
