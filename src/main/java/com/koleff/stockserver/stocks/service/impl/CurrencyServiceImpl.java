package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.CurrencyDto;
import com.koleff.stockserver.stocks.dto.mapper.CurrencyDtoMapper;
import com.koleff.stockserver.stocks.exceptions.CurrenciesNotFoundException;
import com.koleff.stockserver.stocks.exceptions.CurrencyNotFoundException;
import com.koleff.stockserver.stocks.repository.CurrencyRepository;
import com.koleff.stockserver.stocks.service.CurrencyService;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyDtoMapper currencyDtoMapper;
    private final JsonUtil<DataWrapper<Currency>> jsonUtil;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               CurrencyDtoMapper currencyDtoMapper,
                               JsonUtil<DataWrapper<Currency>> jsonUtil) {
        this.currencyRepository = currencyRepository;
        this.currencyDtoMapper = currencyDtoMapper;
        this.jsonUtil = jsonUtil;
    }

    /**
     * Get currency from DB via id
     */
    @Override
    public CurrencyDto getCurrency(Long id) {
        return currencyRepository.findById(id)
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
        return currencyRepository.findByStockTag(stockTag)
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
        return currencyRepository.findCurrencyByCurrencyCode(currencyCode)
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
        return currencyRepository.findAll()
                .stream()
                .map(currencyDtoMapper)
                .toList();
    }

    /**
     * Get currency codes column from DB
     */
    @Override
    public List<String> getCurrencyCodes() {
        return currencyRepository.getCurrencyCodes()
                .orElseThrow(
                        () -> new CurrenciesNotFoundException("Currencies not found. Please load them.")
                )
                .stream()
                .toList();
    }

    /**
     * Save one entry to DB
     */
    @Override
    public void saveCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveCurrencies(List<Currency> data) {
        currencyRepository.saveAll(data);
    }

    /**
     * Delete entry from DB via id
     */
    @Override
    public void deleteById(Long id) {
        currencyRepository.deleteById(id);
    }

    /**
     * Delete entry from DB via currencyCode
     */
    @Override
    public void deleteByCurrencyCode(String currencyCode) {
        currencyRepository.deleteByCurrencyCode(currencyCode);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        currencyRepository.deleteAll();
    }

    /**
     * Load one entry from JSON
     */
    @Override
    public Currency loadCurrency(String currencyCode) {
        String json = jsonUtil.loadJson("currencies.json");

        DataWrapper<Currency> data = jsonUtil.convertJson(json);

        Currency selectedCurrency = data.getData()
                .stream()
                .filter(currency -> currency.getCode().equals(currencyCode))
                .findFirst()
                .orElseThrow(
                        () -> new CurrencyNotFoundException("Currency not found in the JSON with all currencies.")
                );
        System.out.println(selectedCurrency);

        return selectedCurrency;
    }

    /**
     * Load data from JSON
     */
    @Override
    public List<Currency> loadAllCurrencies() {
        String json = jsonUtil.loadJson("currencies.json");

        DataWrapper<Currency> data = jsonUtil.convertJson(json);
        System.out.println(data);

        return data.getData();
    }
}
