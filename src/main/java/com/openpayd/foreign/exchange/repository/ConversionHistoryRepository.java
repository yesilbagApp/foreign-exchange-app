package com.openpayd.foreign.exchange.repository;

import com.openpayd.foreign.exchange.domain.ConversionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long> {
    @Query(
            "SELECT ch FROM ConversionHistory ch WHERE (:from IS NULL OR ch.fromCurrency = :from) "
                    + "AND (:to IS NULL OR ch.toCurrency = :to) "
                    + "AND (:startDate IS NULL OR ch.transactionDateTime >= :startDate) "
                    + "AND (:endDate IS NULL OR ch.transactionDateTime <= :endDate)")
    Page<ConversionHistory> findByCriteria(
            @Param("from") String from,
            @Param("to") String to,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
