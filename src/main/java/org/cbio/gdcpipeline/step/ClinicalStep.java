package org.cbio.gdcpipeline.step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.ClinicalDataModel;
import org.cbio.gdcpipeline.processor.ClinicalProcessor;
import org.cbio.gdcpipeline.reader.ClinicalReader;
import org.cbio.gdcpipeline.tasklet.ClinicalMetaDataTasklet;
import org.cbio.gdcpipeline.writer.ClinicalWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dixit on 16/06/17.
 */
@EnableBatchProcessing
@Configuration
public class ClinicalStep {
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    private static Log LOG = LogFactory.getLog(ClinicalStep.class);

    public enum CLINICAL_TYPE{PATIENT,SAMPLE}

    @Value("${chunk.interval}")
    private int chunkInterval;

    @Bean
    @StepScope
    public ClinicalReader clinicalReader() {
        return new ClinicalReader();
    }

    @Bean
    @StepScope
    public ClinicalProcessor clinicalDataProcessor() {
        return new ClinicalProcessor();
    }

    @Bean
    public CompositeItemWriter<ClinicalDataModel> compositeItemWriter() {
        List<ItemWriter<? super ClinicalDataModel>> delegates = new ArrayList<>(2);

        delegates.add(clinicalPatientDataWriter());
        delegates.add(clinicalSampleDataWriter());

        CompositeItemWriter<ClinicalDataModel> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(delegates);
        try {
            compositeItemWriter.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compositeItemWriter;
    }

    @Bean
    @StepScope
    public ClinicalWriter clinicalPatientDataWriter() {
        return new ClinicalWriter(CLINICAL_TYPE.PATIENT);
    }

    @Bean
    @StepScope
    public ClinicalWriter clinicalSampleDataWriter() {
        return new ClinicalWriter(CLINICAL_TYPE.SAMPLE);
    }

    @Bean
    public Step clinicalDataStep() {
        return stepBuilderFactory.get("clinicalDataStep")
                .<ClinicalDataModel, ClinicalDataModel>chunk(chunkInterval)
                .reader(clinicalReader())
                .processor(clinicalDataProcessor())
                .writer(compositeItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet clinicalMetaDataTasklet() {
        return new ClinicalMetaDataTasklet();
    }

    @Bean
    public Step clinicalMetaDataStep() {
        return stepBuilderFactory.get("clinicalMetaDataStep")
                .tasklet(clinicalMetaDataTasklet())
                .build();
    }
}
