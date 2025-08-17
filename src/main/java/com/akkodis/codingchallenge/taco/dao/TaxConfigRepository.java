package com.akkodis.codingchallenge.taco.dao;

import com.akkodis.codingchallenge.taco.model.TaxConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TaxConfigRepository extends JpaRepository<TaxConfig, Long> {
    @Query("SELECT tc FROM TaxConfig tc WHERE tc.effectiveDate <= :currentDate ORDER BY tc.effectiveDate DESC")
    Optional<TaxConfig> findLastTaxConfigForCurrentDate(@Param("currentDate") LocalDate currentDate);

    //Optional<TaxConfig> findTaxConfigByEffectiveDateLessThanEqualOrderByEffectiveDateDesc(LocalDate currentDate); //OMG...
}