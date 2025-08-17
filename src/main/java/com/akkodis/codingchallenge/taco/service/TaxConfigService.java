package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.model.TaxConfig;

public interface TaxConfigService {
    TaxConfig findCurrentTaxConfig();
}
