package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.dto.mapper.IntraDayDtoMapper;
import com.koleff.stockserver.stocks.exceptions.IntraDayNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.IntraDayRepositoryImpl;
import com.koleff.stockserver.stocks.service.IntraDayService;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntraDayServiceImpl implements IntraDayService {
    private final IntraDayRepositoryImpl intraDayRepositoryImpl;
    private final StockServiceImpl stockServiceImpl;

    private final IntraDayDtoMapper intraDayDtoMapper;
    private final JsonUtil<DataWrapper<IntraDay>> jsonUtil;

    @Autowired
    public IntraDayServiceImpl(IntraDayRepositoryImpl intraDayRepositoryImpl,
                               IntraDayDtoMapper intraDayDtoMapper,
                               StockServiceImpl stockServiceImpl,
                               JsonUtil<DataWrapper<IntraDay>> jsonUtil) {
        this.intraDayRepositoryImpl = intraDayRepositoryImpl;
        this.intraDayDtoMapper = intraDayDtoMapper;
        this.stockServiceImpl = stockServiceImpl;
        this.jsonUtil = jsonUtil;
    }

    /**
     * Get intraDay from DB via stockTag
     */
    @Override
    public List<IntraDayDto> getIntraDay(String stockTag) {
        return intraDayRepositoryImpl.findIntraDayByStockTag(stockTag)
                .orElseThrow(
                        () -> new IntraDayNotFoundException(
                                String.format("Intra day for stock tag %s not found.",
                                        stockTag
                                )
                        )
                )
                .stream()
                .map(intraDayDtoMapper)
                .toList();
    }

    /**
     * Get intraDay from DB via id
     */
    @Override
    public List<IntraDayDto> getIntraDay(Long id) {
        return intraDayRepositoryImpl.findAllById(id)
                .orElseThrow(
                        () -> new IntraDayNotFoundException(
                                String.format("Intra day with id %d not found.",
                                        id
                                )
                        )
                )
                .stream()
                .map(intraDayDtoMapper)
                .toList();
    }


    /**
     * Get all intraDays from DB
     * List for all stocks and inside each list -> list of their intraDay
     */
    @Override
    public List<List<IntraDayDto>> getAllIntraDays() {
        List<List<IntraDayDto>> data = new ArrayList<>();

        List<String> stockTags = stockServiceImpl.getStockTags();
        stockTags.forEach(
                stockTag -> {
                    List<IntraDayDto> entry = intraDayRepositoryImpl.findIntraDayByStockTag(stockTag)
                            .orElseThrow(
                                    () -> new IntraDayNotFoundException(
                                            String.format("Intra day for stock tag %s not found.",
                                                    stockTag
                                            )
                                    )
                            )
                            .stream()
                            .map(intraDayDtoMapper)
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
    public void saveIntraDay(List<IntraDay> data) {
        stockServiceImpl.getStockTags()
                .forEach(stockTag -> {

                    //Configure stock_id
                    data.forEach(entry ->
                            entry.setStockId(
                                    stockServiceImpl.getStockId(stockTag)
                            )
                    );

                    //Save data entities to DB
                    intraDayRepositoryImpl.saveAll(data);
                });
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveAllIntraDays(List<List<IntraDay>> data) {
        stockServiceImpl.getStockTags()
                .forEach(stockTag -> {

                    //Configure stock_id
                    data.forEach(intraDay ->
                            intraDay.forEach(entry ->
                                    entry.setStockId(
                                            stockServiceImpl.getStockId(stockTag)
                                    )
                            ));

                    //Save data entities to DB
                    data.forEach(intraDayRepositoryImpl::saveAll);
                });
    }

    /**
     * Delete entry from DB via id
     */
    @Override
    public void deleteById(Long id) {
        intraDayRepositoryImpl.deleteById(id);
    }

    /**
     * Delete entry from DB via stockTag
     */
    @Override
    public void deleteByStockTag(String stockTag) {
        intraDayRepositoryImpl.deleteByStockTag(stockTag);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        intraDayRepositoryImpl.deleteAll();
    }


    /**
     * Load one entry from JSON
     */
    @Override
    public List<IntraDay> loadIntraDay(String stockTag) {
        //Configure json based on current stock
        String filePath = String.format("intraday%s.json", stockTag);

        //Load data from json
        String json = jsonUtil.loadJson(filePath);
        DataWrapper<IntraDay> data = jsonUtil.convertJson(json);

        return data.getData();
    }


    /**
     * Load data from JSON
     */
    //TODO: just load from V2 file...
    @Override
    public List<List<IntraDay>> loadAllIntraDays() {
        List<List<IntraDay>> data = new ArrayList<>();

        List<String> stockTags = stockServiceImpl.loadStockTags();
        stockTags.forEach(
                stockTag -> new Thread(() -> {
                    try {
                        List<IntraDay> entry = loadIntraDay(stockTag);

                        configureJoin(entry, stockTag);

                        data.add(entry);
                    } catch (NullPointerException e) {
                        System.out.println(stockTag);
                    }
                }).start()
        );

        return data;
    }

    /**
     * Used in PublicApiClient
     */
    private void configureJoin(List<IntraDay> data, String stockTag) {
        //Configure stock_id
        data.forEach(entity ->
                entity.setStockId(
                        stockServiceImpl.getStockId(stockTag)
                )
        );
    }
}

