package org.cbio.gdcpipeline.step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CnaRecord;
import org.cbio.gdcpipeline.processor.CnaProcessor;
import org.cbio.gdcpipeline.util.CommonDataUtil;
import org.cbio.gdcpipeline.writer.ClinicalWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.cbio.gdcpipeline.reader.CnaReader;
import org.cbio.gdcpipeline.writer.CnaWriter;

/**
 * @author heinsz
 */
@EnableBatchProcessing
@Configuration
public class CnaStep {
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    private static Log LOG = LogFactory.getLog(ClinicalStep.class);

    @Value("${chunk.interval}")
    private int chunkInterval;

    @Bean
    @StepScope
    public CnaReader cnaReader() {
        return new CnaReader();
    }

    @Bean
    @StepScope
    public CnaProcessor cnaProcessor() {
        return new CnaProcessor();
    }

    @Bean
    @StepScope
    public CnaWriter cnaWriter() {
        return new CnaWriter();
    }

    @Bean
    @StepScope
    public ClinicalWriter cnaPatientDataWriter() {
        return new ClinicalWriter(CommonDataUtil.CLINICAL_TYPE.PATIENT);
    }

    @Bean
    @StepScope
    public ClinicalWriter cnaSampleDataWriter() {
        return new ClinicalWriter(CommonDataUtil.CLINICAL_TYPE.SAMPLE);
    }

    @Bean
    public Step cnaDataStep() {
        return stepBuilderFactory.get("cnaDataStep")
                .<CnaRecord, CnaRecord>chunk(chunkInterval)
                .reader(cnaReader())
                .processor(cnaProcessor())
                .writer(cnaWriter())
                .build();
    }
}
