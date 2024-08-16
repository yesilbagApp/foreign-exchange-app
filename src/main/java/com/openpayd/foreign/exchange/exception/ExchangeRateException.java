package com.openpayd.foreign.exchange.exception;

public class ExchangeRateException extends RuntimeException {
  public ExchangeRateException(String message) {
    super(message);
  }
}
