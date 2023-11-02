package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.dto.CurrencyDto;

import java.util.List;

public interface CurrencyService {

    CurrencyDto getCurrency(Long id);
    CurrencyDto getCurrency(String stockTag);
    Long getCurrencyId(String currencyCode);
    List<CurrencyDto> getCurrencies();
    List<String> getCodeColumn();
    void saveCurrency(Currency currency);
    void saveCurrencies(List<Currency> data);
    void deleteById(Long id);
    void deleteByCode(String currencyCode);
    void deleteAll();
    Currency loadCurrency(String currencyCode);
    List<Currency> loadAllCurrencies();
}
