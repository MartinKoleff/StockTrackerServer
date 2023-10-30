package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.TimezoneDto;
import com.koleff.stockserver.stocks.dto.mapper.TimezoneDtoMapper;
import com.koleff.stockserver.stocks.exceptions.TimezoneNotFoundException;
import com.koleff.stockserver.stocks.exceptions.TimezonesNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.TimezoneRepositoryImpl;
import com.koleff.stockserver.stocks.service.TimezoneService;
import com.koleff.stockserver.stocks.utils.jsonUtil.TimezoneJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimezoneServiceImpl implements TimezoneService {
    private final TimezoneRepositoryImpl timezoneRepositoryImpl;
    private final TimezoneDtoMapper timezoneDtoMapper;
    private final TimezoneJsonUtil timezoneJsonUtil;

    @Autowired
    public TimezoneServiceImpl(TimezoneRepositoryImpl timezoneRepositoryImpl,
                               TimezoneDtoMapper timezoneDtoMapper,
                               TimezoneJsonUtil timezoneJsonUtil) {
        this.timezoneRepositoryImpl = timezoneRepositoryImpl;
        this.timezoneDtoMapper = timezoneDtoMapper;
        this.timezoneJsonUtil = timezoneJsonUtil;
    }

    /**
     * Get timezone from DB via id
     */
    @Override
    public TimezoneDto getTimezone(Long id) {
        return timezoneRepositoryImpl.findById(id)
                .stream()
                .map(timezoneDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new TimezoneNotFoundException(
                                String.format("Timezone with id %d not found.",
                                        id
                                )
                        )
                );
    }

    /**
     * Get timezone from DB via stockTag associated with it
     */
    @Override
    public TimezoneDto getTimezone(String stockTag) {
        return timezoneRepositoryImpl.findByStockTag(stockTag)
                .stream()
                .map(timezoneDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new TimezoneNotFoundException(
                                String.format("Timezone associated with stock tag %s not found.",
                                        stockTag
                                )
                        )
                );
    }

    /**
     * Get timezone id from DB via timezone string
     */
    @Override
    public Long getTimezoneId(String timezone) {
        return timezoneRepositoryImpl.findTimezoneByTimezoneString(timezone)
                .stream()
                .findFirst()
                .orElseThrow(
                        () -> new TimezoneNotFoundException(
                                String.format("Timezone with timezone string %s not found.",
                                        timezone
                                )
                        )
                ).getId();
    }

    /**
     * Get all timezones from DB
     */
    @Override
    public List<TimezoneDto> getTimezones() {
        return timezoneRepositoryImpl.findAll()
                .stream()
                .map(timezoneDtoMapper)
                .toList();
    }


    /**
     * Get timezone column from DB
     */
    @Override
    public List<String> getTimezoneStrings() {
        return timezoneRepositoryImpl.getTimezoneStrings()
                .orElseThrow(
                        () -> new TimezonesNotFoundException("Timezone not found. Please load them.")
                )
                .stream()
                .toList();
    }

    /**
     * Save one entry to DB
     */
    @Override
    public void saveTimezone(Timezone timezone) {
        timezoneRepositoryImpl.save(timezone);
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveTimezones(List<Timezone> data) {
        timezoneRepositoryImpl.saveAll(data);
    }

    /**
     * Delete entry from DB via id
     */
    @Override
    public void deleteById(Long id) {
        timezoneRepositoryImpl.deleteById(id);
    }

    /**
     * Delete entry from DB via currencyCode
     */
    @Override
    public void deleteByTimezone(String timezone) {
        timezoneRepositoryImpl.deleteByTimezone(timezone);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        timezoneRepositoryImpl.deleteAll();
    }

    /**
     * Load one entry from JSON
     */
    @Override
    public Timezone loadTimezone(String timezone) {
        String json = timezoneJsonUtil.loadJson("timezones.json");

        DataWrapper<Timezone> data = timezoneJsonUtil.convertJson(json);

        Timezone selectedTimezone = data.getData()
                .stream()
                .filter(currency -> currency.getTimezone().equals(timezone))
                .findFirst()
                .orElseThrow(
                        () -> new TimezoneNotFoundException("Timezone not found in the JSON with all currencies.")
                );

        return selectedTimezone;
    }

    /**
     * Load data from JSON
     */
    @Override
    public List<Timezone> loadAllTimezones() {
        String json = timezoneJsonUtil.loadJson("timezones.json");

        DataWrapper<Timezone> data = timezoneJsonUtil.convertJson(json);

        return data.getData();
    }
}
