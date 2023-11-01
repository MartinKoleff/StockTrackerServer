package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.dto.mapper.EndOfDayDtoMapper;
import com.koleff.stockserver.stocks.exceptions.EndOfDayNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.EndOfDayRepositoryImpl;
import com.koleff.stockserver.stocks.service.EndOfDayService;
import com.koleff.stockserver.stocks.utils.jsonUtil.EndOfDayJsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final EndOfDayJsonUtil endOfDayJsonUtil;

    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public EndOfDayServiceImpl(EndOfDayRepositoryImpl endOfDayRepositoryImpl,
                               StockServiceImpl stockServiceImpl,
                               EndOfDayDtoMapper endOfDayDtoMapper,
                               EndOfDayJsonUtil endOfDayJsonUtil,
                               JobLauncher jobLauncher,
                               @Qualifier("eodJob") Job job) {
        this.endOfDayRepositoryImpl = endOfDayRepositoryImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.endOfDayDtoMapper = endOfDayDtoMapper;
        this.endOfDayJsonUtil = endOfDayJsonUtil;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    /**
     * Get end of day from DB via stockTag
     */
    @Override
    public List<EndOfDayDto> getEndOfDays(String stockTag) {
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

    @Override
    public List<EndOfDayDto> getEndOfDays(String stockTag, String dateFrom, String dateTo) {
        return endOfDayRepositoryImpl.findEndOfDayByStockTag(stockTag, dateFrom, dateTo)
                .orElseThrow(
                        () -> new EndOfDayNotFoundException(
                                String.format("End of day for stock tag %s between dates %s and %s not found.",
                                        stockTag,
                                        dateFrom,
                                        dateFrom
                                )
                        )
                )
                .stream()
                .map(endOfDayDtoMapper)
                .toList();
    }

    @Override
    public EndOfDayDto getEndOfDay(String stockTag, String date) {
        return endOfDayRepositoryImpl.findEndOfDayByStockTag(stockTag, date)
                .orElseThrow(
                        () -> new EndOfDayNotFoundException(
                                String.format("End of day for stock tag %s for date %s not found.",
                                        stockTag,
                                        date
                                )
                        )
                )
                .stream()
                .map(endOfDayDtoMapper)
                .findFirst()
                .orElseThrow(); //TODO: add exception
    }

    /**
     * Get end of day from DB via id
     */
    @Override
    public List<EndOfDayDto> getEndOfDays(Long id) {
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
        stockTags.parallelStream()
                 .forEach(
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
        endOfDayRepositoryImpl.saveAll(data);
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveAllEndOfDays(List<List<EndOfDay>> data) {
        //Save data entities to DB
        data.parallelStream()
                .forEach(endOfDayRepositoryImpl::saveAll);
    }

    /**
     * Save data using Spring Batch
     */
    @Override
    public void saveViaJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            logger.error("Error saving EOD data via Spring Batch!");
            e.printStackTrace();
        }
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
        String json = endOfDayJsonUtil.loadJson(filePath);
        DataWrapper<EndOfDay> data = endOfDayJsonUtil.convertJson(json);

        return data.getData();
    }

    /**
     * Load data from JSON
     */
    @Override
    public List<List<EndOfDay>> loadAllEndOfDays() {
        List<List<EndOfDay>> data = new ArrayList<>();

        List<String> stockTags = stockServiceImpl.loadStockTags();
        stockTags.parallelStream()
                .forEach(
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
