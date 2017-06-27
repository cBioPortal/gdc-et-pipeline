package org.cbio.gdcpipeline.step;

import org.cbio.gdcpipeline.tasklet.ClinicalMetaDataTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Dixit on 27/06/17.
 */

@Configuration
@EnableBatchProcessing
public class ClinicalMetaDataStep {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public Tasklet clinicalMetaDataTasklet() {
        return new ClinicalMetaDataTasklet();
    }


    @Bean
    public Step runClinicalMetaDataStep() {

        return stepBuilderFactory.get("clinicalMetaDataStep")
                .tasklet(clinicalMetaDataTasklet())
                .build();

    }

}
