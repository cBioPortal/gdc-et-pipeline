package org.cbio.gdcpipeline.JobConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.tasklet.FileMappingTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class BatchConfiguration {

    private static Log LOG = LogFactory.getLog(BatchConfiguration.class);

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Resource(name = "runClinicalDataStep")
    Step runClinicalDataStep;

    @Resource(name = "runClinicalMetaDataStep")
    Step runClinicalMetaDataStep;


    @Value("${chunk.interval}")
    private int chunkInterval;

    @Bean
    public ExecutionContextPromotionListener jobExecutionListener() {
        String[] keys = new String[]{"barcodeToSamplesMap", "uuidToFilesMap"};
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        executionContextPromotionListener.setKeys(keys);
        return executionContextPromotionListener;

    }

    @Bean
    public Step fileMappingStep() {
        return stepBuilderFactory.get("fileMappingStep")
                .listener(jobExecutionListener())
                .tasklet(createFileMappings())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet createFileMappings(){
        return new FileMappingTasklet();
    }

    @Bean
    public Step runClinicalDataStep() {
        return runClinicalDataStep;
    }


    @Bean
    public Step runClinicalMetaDataStep() {
        return runClinicalMetaDataStep;
    }


    // Flow of All Steps

    @Bean
    public Job mainJob() {
        return jobBuilderFactory.get("mainJob")
                .start(fileMappingStep())
                .next(runClinicalDataStep())
                .next(runClinicalMetaDataStep())
                // .next(segmentedDataStep())
                // .next(mutationDataStep())
                // .next(cnaDataStep())
                .build();
    }

}
