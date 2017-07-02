package org.cbio.gdcpipeline.step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.ClinicalDataModel;
import org.cbio.gdcpipeline.processor.ClinicalDataProcessor;
import org.cbio.gdcpipeline.reader.ClinicalReader;
import org.cbio.gdcpipeline.writer.ClinicalDataWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Dixit on 16/06/17.
 */
@EnableBatchProcessing
@Configuration
public class ClinicalDataStep {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;


    private static Log LOG = LogFactory.getLog(ClinicalDataStep.class);

    @Value("${chunk.interval}")
    private int chunkInterval;

    @Bean
    @StepScope
    public ClinicalReader clinicalReader() {
        return new ClinicalReader();

    }

    @Bean
    @StepScope
    public ClinicalDataProcessor clinicalDataProcessor() {
        return new ClinicalDataProcessor();
    }

    @Bean
    @StepScope
    public ClinicalDataWriter clinicalDataWriter() {
        return new ClinicalDataWriter();
    }

    @Bean
    public Step runClinicalDataStep() {

        return stepBuilderFactory.get("clinicalDataStep")
                .<ClinicalDataModel, ClinicalDataModel>chunk(chunkInterval)
                .reader(clinicalReader())
                .processor(clinicalDataProcessor())
                .writer(clinicalDataWriter())
                .build();

    }


}
