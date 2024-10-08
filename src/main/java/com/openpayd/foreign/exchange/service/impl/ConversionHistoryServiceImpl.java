package com.openpayd.foreign.exchange.service.impl;

import com.openpayd.foreign.exchange.controller.request.ConversionHistoryRequest;
import com.openpayd.foreign.exchange.controller.response.PageResponse;
import com.openpayd.foreign.exchange.domain.ConversionHistory;
import com.openpayd.foreign.exchange.exception.ConversionHistoryException;
import com.openpayd.foreign.exchange.repository.ConversionHistoryRepository;
import com.openpayd.foreign.exchange.service.ConversionHistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConversionHistoryServiceImpl implements ConversionHistoryService {

    private static final Logger LOGGER =
            Logger.getLogger(ConversionHistoryServiceImpl.class.getName());
    private final ConversionHistoryRepository conversionHistoryRepository;

    public ConversionHistoryServiceImpl(ConversionHistoryRepository conversionHistoryRepository) {
        this.conversionHistoryRepository = conversionHistoryRepository;
    }

    @Override
    public PageResponse<ConversionHistory> getConversionHistory(
            ConversionHistoryRequest request, Pageable pageable) {

        try {
            // Retrieve paginated conversion history from the repository based on the search criteria
            var page =
                    conversionHistoryRepository.findByCriteria(
                            request.from(), request.to(), request.startDate(), request.endDate(), pageable);
            // Create and return a PageResponse object with the retrieved page details
            return new PageResponse<>(
                    page.getTotalElements(),
                    page.getTotalPages(),
                    page.getNumber(),
                    page.getSize(),
                    page.isFirst(),
                    page.isLast(),
                    page.getContent());

        } catch (Exception ex) {
            // Log the error and throw a custom ConversionHistoryException in case of failure
            LOGGER.log(Level.SEVERE, "Unexpected error occurred", ex);
            throw new ConversionHistoryException(ex.getMessage());
        }
    }

    @Override
    public void saveConversionHistory(ConversionHistory conversionHistory) {
        // Save the conversion history to the repository
        conversionHistoryRepository.save(conversionHistory);
    }
}
