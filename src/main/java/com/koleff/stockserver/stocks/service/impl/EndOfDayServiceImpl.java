package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.dto.mapper.EndOfDayDtoMapper;
import com.koleff.stockserver.stocks.exceptions.EndOfDayNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.EndOfDayRepositoryImpl;
import com.koleff.stockserver.stocks.service.EndOfDayService;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EndOfDayServiceImpl implements EndOfDayService {

    private final static Logger logger = LogManager.getLogger(IntraDayServiceImpl.class);

    @Value("${koleff.versionAnnotation}") //Configuring version annotation for Json loading / exporting
    private String versionAnnotation;
    private final EndOfDayRepositoryImpl endOfDayRepositoryImpl;
    private final StockServiceImpl stockServiceImpl;
    private final EndOfDayDtoMapper endOfDayDtoMapper;
    private final JsonUtil<DataWrapper<EndOfDay>> jsonUtil;

    @Autowired
    public EndOfDayServiceImpl(EndOfDayRepositoryImpl endOfDayRepositoryImpl,
                               StockServiceImpl stockServiceImpl,
                               EndOfDayDtoMapper endOfDayDtoMapper,
                               JsonUtil<DataWrapper<EndOfDay>> jsonUtil) {
        this.endOfDayRepositoryImpl = endOfDayRepositoryImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.endOfDayDtoMapper = endOfDayDtoMapper;
        this.jsonUtil = jsonUtil;
    }

    /**
     * Get end of day from DB via stockTag
     */
    @Override
    public List<EndOfDayDto> getEndOfDay(String stockTag) {
        return endOfDayRepositoryImpl.findEndOfDayByStockTag(stockTag)
                .orElseThrow(
                        () -> new EndOfDayNotFoundException(
                                String.format("End of day for stock tag %s not found.",
                                        stockTag
                                )
                        )
                )
                .stream()
                .map(endOfDayDtoMapper)
                .toList();
    }

    /**
     * Get end of day from DB via id
     */
    @Override
    public List<EndOfDayDto> getEndOfDay(Long id) {
        return endOfDayRepositoryImpl.findAllById(id)
                .orElseThrow(
                        () -> new EndOfDayNotFoundException(
                                String.format("End of day with id %d not found.",
                                        id
                                )
                        )
                )
                .stream()
                .map(endOfDayDtoMapper)
                .toList();
    }

    /**
     * Get all end of days from DB
     * List for all stocks and inside each list -> list of their end of days
     */
    @Override
    public List<List<EndOfDayDto>> getAllEndOfDays() {
        List<List<EndOfDayDto>> data = new ArrayList<>();

        List<String> stockTags = stockServiceImpl.getStockTags();
        stockTags.forEach(
                stockTag -> {
                    List<EndOfDayDto> entry = endOfDayRepositoryImpl.findEndOfDayByStockTag(stockTag)
                            .orElseThrow(
                                    () -> new EndOfDayNotFoundException(
                                            String.format("End of day for stock tag %s not found.",
                                                    stockTag
                                            )
                                    )
                            )
                            .stream()
                            .map(endOfDayDtoMapper)
                            .toList();
                    data.add(entry);
                }
        );

        return data;
    }

    /**
     * Save one entry to DB
     */
    @Override
    public void saveEndOfDay(List<EndOfDay> data) {
        stockServiceImpl.getStockTags()
                .forEach(stockTag -> {

                    //Configure stock_id
                    data.forEach(entry ->
                            entry.setStockId(
                                    stockServiceImpl.getStockId(stockTag)
                            )
                    );

                    //Save data entities to DB
                    endOfDayRepositoryImpl.saveAll(data);
                });
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveAllEndOfDays(List<List<EndOfDay>> data) {
        stockServiceImpl.getStockTags()
                .forEach(stockTag -> {

                    //Configure stock_id
                    data.forEach(endOfDay ->
                            endOfDay.forEach(entry ->
                                    entry.setStockId(
                                            stockServiceImpl.getStockId(stockTag)
                                    )
                            ));

                    //Save data entities to DB
                    data.forEach(endOfDayRepositoryImpl::saveAll);
                });
    }

    /**
     * Delete entry from DB via id
     */
    @Override
    public void deleteById(Long id) {
        endOfDayRepositoryImpl.deleteById(id);
    }

    /**
     * Delete entry from DB via stockTag
     */
    @Override
    public void deleteByStockTag(String stockTag) {
        endOfDayRepositoryImpl.deleteByStockTag(stockTag);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        endOfDayRepositoryImpl.deleteAll();
    }

    /**
     * Load one entry from JSON
     */
    @Override
    public List<EndOfDay> loadEndOfDay(String stockTag) {
        //Configure json based on current stock
        String filePath = String.format("eod%s%s.json", stockTag, versionAnnotation);

        //Load data from json
        String json = jsonUtil.loadJson(filePath);
        DataWrapper<EndOfDay> data = jsonUtil.convertJson(json);

        return data.getData();
    }

    /**
     * Load data from JSON
     */
    @Override
    public List<List<EndOfDay>> loadAllEndOfDays() {
        List<List<EndOfDay>> data = new ArrayList<>();

        List<String> stockTags = stockServiceImpl.loadStockTags();
        stockTags.forEach(
                stockTag -> {
                    try {
                        List<EndOfDay> entry = loadEndOfDay(stockTag);

                        data.add(entry);
                    } catch (NullPointerException e) {
                        logger.error(String.format("JSON file for stock %s is corrupted!\n", stockTag));
                    }
                }
        );

        return data;
    }
}
