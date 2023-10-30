package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.dto.mapper.IntraDayDtoMapper;
import com.koleff.stockserver.stocks.exceptions.IntraDayNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.IntraDayRepositoryImpl;
import com.koleff.stockserver.stocks.service.IntraDayService;
import com.koleff.stockserver.stocks.utils.jsonUtil.IntraDayJsonUtil;
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
public class IntraDayServiceImpl implements IntraDayService {

    private final static Logger logger = LogManager.getLogger(IntraDayServiceImpl.class);

    @Value("${koleff.versionAnnotation}") //Configuring version annotation for Json loading / exporting
    private String versionAnnotation;
    private final IntraDayRepositoryImpl intraDayRepositoryImpl;
    private final StockServiceImpl stockServiceImpl;

    private final IntraDayDtoMapper intraDayDtoMapper;
    private final IntraDayJsonUtil intraDayJsonUtil;

    private final JobLauncher jobLauncher;
    private final Job job;
    @Autowired
    public IntraDayServiceImpl(IntraDayRepositoryImpl intraDayRepositoryImpl,
                               IntraDayDtoMapper intraDayDtoMapper,
                               StockServiceImpl stockServiceImpl,
                               IntraDayJsonUtil intraDayJsonUtil,
                               JobLauncher jobLauncher,
                               @Qualifier("intraDayJob") Job job) {
        this.intraDayRepositoryImpl = intraDayRepositoryImpl;
        this.intraDayDtoMapper = intraDayDtoMapper;
        this.stockServiceImpl = stockServiceImpl;
        this.intraDayJsonUtil = intraDayJsonUtil;
        this.jobLauncher = jobLauncher;
        this.job = job;
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
        stockTags.parallelStream()
                .forEach(
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
        intraDayRepositoryImpl.saveAll(data);
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveAllIntraDays(List<List<IntraDay>> data) {
        //Save data entities to DB
        data.forEach(intraDayRepositoryImpl::saveAll);
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
        String filePath = String.format("intraday%s%s.json", stockTag, versionAnnotation);

        //Load data from json
        String json = intraDayJsonUtil.loadJson(filePath);
        DataWrapper<IntraDay> data = intraDayJsonUtil.convertJson(json);

        return data.getData();
    }


    /**
     * Load data from JSON
     */
    @Override
    public List<List<IntraDay>> loadAllIntraDays() {
        List<List<IntraDay>> data = new ArrayList<>();

        List<String> stockTags = stockServiceImpl.loadStockTags();
        stockTags.parallelStream()
                .forEach(
                        stockTag -> {
                            try {
                                List<IntraDay> entry = loadIntraDay(stockTag);

                                data.add(entry);
                            } catch (NullPointerException e) {
                                logger.error(String.format("JSON file for stock %s is corrupted!\n", stockTag));
                            }
                        }
                );
        return data;
    }
}

