package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.CurrencyDto;
import com.koleff.stockserver.stocks.dto.mapper.CurrencyDtoMapper;
import com.koleff.stockserver.stocks.exceptions.CurrencyNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.CurrencyRepositoryImpl;
import com.koleff.stockserver.stocks.service.CurrencyService;
import com.koleff.stockserver.stocks.utils.jsonUtil.CurrencyJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepositoryImpl currencyRepositoryImpl;
    private final CurrencyDtoMapper currencyDtoMapper;
    private final CurrencyJsonUtil currencyJsonUtil;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepositoryImpl currencyRepositoryImpl,
                               CurrencyDtoMapper currencyDtoMapper,
                               CurrencyJsonUtil currencyJsonUtil) {
        this.currencyRepositoryImpl = currencyRepositoryImpl;
        this.currencyDtoMapper = currencyDtoMapper;
        this.currencyJsonUtil = currencyJsonUtil;
    }

    /**
     * Get currency from DB via id
     */
    @Override
    public CurrencyDto getCurrency(Long id) {
        return currencyRepositoryImpl.findById(id)
                .stream()
                .map(currencyDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new CurrencyNotFoundException(
                                String.format("Currency with id %d not found.",
                                        id
                                )
                        )
                );
    }

    /**
     * Get currency from DB via stockTag associated with it
     */
    @Override
    public CurrencyDto getCurrency(String stockTag) {
        return currencyRepositoryImpl.findByStockExchanges_Stocks_Tag(stockTag)
                .stream()
                .map(currencyDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new CurrencyNotFoundException(
                                String.format("Currency associated with stock tag %s not found.",
                                        stockTag
                                )
                        )
                );
    }

    /**
     * Get currency id from DB via currencyCode
     */
    @Override
    public Long getCurrencyId(String currencyCode) {
        return currencyRepositoryImpl.findByCode(currencyCode)
                .stream()
                .findFirst()
                .orElseThrow(
                        () -> new CurrencyNotFoundException(
                                String.format("Currency with code %s not found.",
                                        currencyCode
                                )
                        )
                ).getId();
    }


    /**
     * Get all currencies from DB
     */
    @Override
    public List<CurrencyDto> getCurrencies() {
        return currencyRepositoryImpl.findAll()
                .stream()
                .map(currencyDtoMapper)
                .toList();
    }

    /**
     * Get currency codes column from DB
     */
    @Override
    public List<String> getCodeColumn() {
        return currencyRepositoryImpl.findAll()
                .stream()
                .map(Currency::getCode)
                .toList();
    }

    /**
     * Save one entry to DB
     */
    @Override
    public void saveCurrency(Currency currency) {
        currencyRepositoryImpl.save(currency);
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveCurrencies(List<Currency> data) {
        currencyRepositoryImpl.saveAll(data);
    }

    /**
     * Delete entry from DB via id
     */
    @Override
    public void deleteById(Long id) {
        currencyRepositoryImpl.deleteById(id);
    }

    /**
     * Delete entry from DB via currencyCode
     */
    @Override
    public void deleteByCode(String currencyCode) {
        currencyRepositoryImpl.deleteByCode(currencyCode);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        currencyRepositoryImpl.deleteAll();
    }

    @Override
    public void truncateTable() {
        currencyRepositoryImpl.truncate();
    }

    /**
     * Load one entry from JSON
     */
    @Override
    public Currency loadCurrency(String currencyCode) {
        String json = currencyJsonUtil.loadJson("currencies.json");

        DataWrapper<Currency> data = currencyJsonUtil.convertJson(json);

        Currency selectedCurrency = data.getData()
                .stream()
                .filter(currency -> currency.getCode().equals(currencyCode))
                .findFirst()
                .orElseThrow(
                        () -> new CurrencyNotFoundException("Currency not found in the JSON with all currencies.")
                );

        return selectedCurrency;
    }

    /**
     * Load data from JSON
     */
    @Override
    public List<Currency> loadAllCurrencies() {
        String json = currencyJsonUtil.loadJson("currencies.json");

        DataWrapper<Currency> data = currencyJsonUtil.convertJson(json);

        return data.getData();
    }
}
