package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.dto.mapper.IntraDayDtoMapper;
import com.koleff.stockserver.stocks.exceptions.IntraDayNotFoundException;
import com.koleff.stockserver.stocks.exceptions.IntraDayNotSavedException;
import com.koleff.stockserver.stocks.repository.IntraDayRepository;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntraDayService {
    private final IntraDayRepository intraDayRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;

    private final IntraDayDtoMapper intraDayDtoMapper;
    private final JsonUtil<DataWrapper<IntraDay>> jsonUtil;

    @Autowired
    public IntraDayService(IntraDayRepository intraDayRepository,
                           IntraDayDtoMapper intraDayDtoMapper,
                           StockRepository stockRepository,
                           StockService stockService,
                           JsonUtil<DataWrapper<IntraDay>> jsonUtil) {
        this.intraDayRepository = intraDayRepository;
        this.intraDayDtoMapper = intraDayDtoMapper;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
        this.jsonUtil = jsonUtil;
    }

    public List<IntraDayDto> getIntraDay(String stockTag) {
        return intraDayRepository.findIntraDayByStockTag(stockTag)
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

    public List<IntraDayDto> getIntraDay(Long id) {
        return intraDayRepository.findAllById(id)
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

    //List for all stocks and inside each list -> list of their intraday
    public void saveAllIntraDay(List<List<IntraDay>> data) {
        stockRepository.findAll().stream()
                .map(Stock::getTag)
                .forEach(stockTag -> {

                    //Configure stock_id
                    data.forEach(intraDay ->
                            intraDay.forEach(entry ->
                                    entry.setStockId(
                                            stockRepository.findStockByStockTag(stockTag)
                                                    .orElseThrow(
                                                            () -> new IntraDayNotSavedException(
                                                                    String.format(
                                                                            "Intra day for stock %s could not be saved.",
                                                                            stockTag
                                                                    )
                                                            )
                                                    )
                                                    .getId()
                                    )
                            ));

                    //Save data entities to DB
                    data.forEach(intraDayRepository::saveAll);
                });
    }

    public void saveIntraDay(List<IntraDay> data) {
        stockRepository.findAll().stream()
                .map(Stock::getTag)
                .forEach(stockTag -> {

                    //Configure stock_id
                    data.forEach(entry ->
                            entry.setStockId(
                                    stockRepository.findStockByStockTag(stockTag)
                                            .orElseThrow(
                                                    () -> new IntraDayNotSavedException(
                                                            String.format(
                                                                    "Intra day for stock %s could not be saved.",
                                                                    stockTag
                                                            )
                                                    )
                                            )
                                            .getId()
                            )
                    );

                    //Save data entities to DB
                    intraDayRepository.saveAll(data);
                });
    }

    public List<IntraDay> loadIntraDay(String stockTag) {
        //Configure json based on current stock
        String filePath = String.format("intraday%s.json", stockTag);

        //Load data from json
        jsonUtil.setType(IntraDay.class);
        String json = jsonUtil.loadJson(filePath);
        DataWrapper<IntraDay> data = jsonUtil.convertJson(json);

        return data.getData();
    }

    public List<List<IntraDay>> loadAllIntraDays() {
        List<List<IntraDay>> data = new ArrayList<>();

        List<String> stockTags = stockService.getStockTags();
        stockTags.forEach(
                stockTag -> {
                    List<IntraDay> entry = loadIntraDay(stockTag);
                    data.add(entry);
                }
        );

        return data;
    }

    public List<List<IntraDayDto>> getAllIntraDays() {
        List<List<IntraDayDto>> data = new ArrayList<>();

        List<String> stockTags = stockService.getStockTags();
        stockTags.forEach(
                stockTag -> {
                    List<IntraDayDto> entry = intraDayRepository.findIntraDayByStockTag(stockTag)
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
}

