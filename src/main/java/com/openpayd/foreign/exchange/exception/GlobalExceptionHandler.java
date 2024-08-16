package com.openpayd.foreign.exchange.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Handle ForeignExchangeException
  @ExceptionHandler(ExchangeRateException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleForeignExchangeException(ExchangeRateException ex) {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now().toString(), httpStatus.value(), httpStatus.name(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  // Handle ConversionHistoryException
  @ExceptionHandler(ConversionHistoryException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleConversionHistoryException(
      ConversionHistoryException ex) {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now().toString(), httpStatus.value(), httpStatus.name(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  // Handle ConversionException
  @ExceptionHandler(ConversionException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleConversionException(ConversionHistoryException ex) {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now().toString(), httpStatus.value(), httpStatus.name(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  // Handle InvalidConversionHistoryRequestException
  @ExceptionHandler(InvalidConversionHistoryRequestException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleInvalidConversionHistoryRequestException(
      InvalidConversionHistoryRequestException ex) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now().toString(), httpStatus.value(), httpStatus.name(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, httpStatus);
  }
}
