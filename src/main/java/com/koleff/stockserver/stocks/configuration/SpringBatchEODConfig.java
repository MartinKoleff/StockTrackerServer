package com.koleff.stockserver.stocks.configuration;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.repository.impl.EndOfDayRepositoryImpl;
import com.koleff.stockserver.stocks.repository.impl.IntraDayRepositoryImpl;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.json.GsonJsonObjectMarshaller;
import org.springframework.batch.item.json.GsonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@AllArgsConstructor
@Configuration
public class SpringBatchEODConfig extends DefaultBatchConfiguration {

    private final static Logger logger = LogManager.getLogger(SpringBatchIntraDayConfig.class);

    private final String resourcePath = "src/main/resources/json/%s/%s";

    @Value("${koleff.versionAnnotation}")
    private String versionAnnotation;

    private final EntityManagerFactory entityManagerFactory;
    private final EndOfDayRepositoryImpl endOfDayRepositoryImpl;

    @Autowired
    public SpringBatchEODConfig(EntityManagerFactory entityManagerFactory,
                                     EndOfDayRepositoryImpl endOfDayRepositoryImpl) {
        this.entityManagerFactory = entityManagerFactory;
        this.endOfDayRepositoryImpl = endOfDayRepositoryImpl;
    }

    /**
     * Job configuration
     */
    @Bean("eodJob")
    @Autowired
    public Job getJob(JobRepository jobRepository) {
        return new JobBuilder("eodJob", jobRepository)
                .flow(getStep1())
                .end()
                .build();
    }

    /**
     * Step configuration
     * - processor is not needed
     */
    @Bean("eodStep")
    public Step getStep1() {
        return new StepBuilder("eod-json-step", getJobRepository())
                .<EndOfDay, EndOfDay>chunk(100, getTransactionManager())
                .reader(getMultiResourceReader())
                .writer(getRepositoryItemWriter())
                .taskExecutor(getTaskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor getTaskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager getTransactionManager() {
        return new JpaTransactionManager(); //this.entityManagerFactory
    }

    @Bean(name = "eodJobRepository")
    public JobRepository getJobRepository() {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();

        factory.setDataSource(getDataSource());
        factory.setTransactionManager(getTransactionManager());

        JobRepository jobRepository;
        try {
            factory.afterPropertiesSet();
            jobRepository = factory.getObject();
        } catch (Exception e) {
            logger.error("Job repository for EOD setup failed!");
            throw new RuntimeException(e);
        }

        return jobRepository;
    }

    /**
     * Get DataSource from Hibernate EntityManager
     */
//    @Bean
    public DataSource getDataSource() {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) entityManagerFactory;

        return info.getDataSource();
    }

    /**
     * Get JSON EOD directory
     */
    @Bean("eodJsonResources")
    protected Resource[] getJsonResources() {
        String selectedJsons = String.format(resourcePath, versionAnnotation, "eod");

        try {
            Resource[] resources = getPatternResolver().getResources(selectedJsons);

            if (resources.length == 0) {
                throw new IllegalStateException("No resources found for the specified pattern: " + selectedJsons);
            }

            logger.info("EOD Json resources SIZE: " + resources.length);
            return resources;
        } catch (IOException e) {
            logger.error("EOD Json resources setup failed!");
            throw new IllegalStateException("Error while setting up EOD Json resources.", e);
        }
    }

    /**
     * Used in jsonResources
     */
//    @Bean
    protected ResourcePatternResolver getPatternResolver() {
        return new PathMatchingResourcePatternResolver();
    }

    /**
     * Reads multiple files
     * - delegate reader -> JSON reader
     */
    @Bean
    public MultiResourceItemReader<EndOfDay> getMultiResourceReader() {
        return new MultiResourceItemReaderBuilder<EndOfDay>()
                .delegate(getJsonItemReader())
                .resources(getJsonResources())
                .saveState(false) //Fixes runtime error
                .build();
    }

    /**
     * Json file reader
     */
    @Bean
    protected JsonItemReader<EndOfDay> getJsonItemReader() {
        return new JsonItemReaderBuilder<EndOfDay>()
                .jsonObjectReader(new GsonJsonObjectReader<>(EndOfDay.class))
                .name("eodJsonItemReader")
                .build();
    }

    /**
     * Json file item writer
     * - currently write all data in 1 file (until l find multi file JSON writer support).
     */
    @Bean
    protected JsonFileItemWriter<EndOfDay> getJsonFileItemWriter() { //TODO: for the whole directory
        String selectedJson = String.format(resourcePath, versionAnnotation, "eod").concat("/, allJsons.json");

        return new JsonFileItemWriterBuilder<EndOfDay>()
                .jsonObjectMarshaller(new GsonJsonObjectMarshaller<>())
                .resource(new FileSystemResource(selectedJson))
                .name("eodJsonFileItemWriter")
                .build();
    }


    /**
     * Repository item writer
     * - inserts data to repository DB.
     */
    @Bean
    public RepositoryItemWriter<EndOfDay> getRepositoryItemWriter() {
        RepositoryItemWriter<EndOfDay> writer = new RepositoryItemWriter<>();
        writer.setRepository(endOfDayRepositoryImpl);
        writer.setMethodName("save");
        return writer;
    }
}
