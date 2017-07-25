package org.cbio.gdcpipeline.JobConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.decider.ClinicalFileTypeDecider;
import org.cbio.gdcpipeline.decider.StepDecider;
import org.cbio.gdcpipeline.tasklet.SetUpPipelineTasklet;
import org.cbio.gdcpipeline.tasklet.XmlFileMappingTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {
    private static Log LOG = LogFactory.getLog(BatchConfiguration.class);
    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Resource(name = "clinicalDataStep")
    Step clinicalDataStep;

    @Resource(name = "clinicalMetaDataStep")
    Step clinicalMetaDataStep;

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
    public Step xmlFileMappingStep() {
        return stepBuilderFactory.get("xmlFileMappingStep")
                .listener(jobExecutionListener())
                .tasklet(xmlFileMappingTasklet())
                .build();
    }

    @Bean
    public Step setUpPipeline() {
        return stepBuilderFactory.get("setUpPipeline")
                .tasklet(setUpPipelineTasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet setUpPipelineTasklet() {
        return new SetUpPipelineTasklet();
    }


    @Bean
    @StepScope
    public Tasklet xmlFileMappingTasklet() {
        return new XmlFileMappingTasklet();
    }

    @Bean
    @JobScope
    public JobExecutionDecider clinicalFileTypeDecider() {
        return new ClinicalFileTypeDecider();
    }

    @Bean
    public Flow configurePipelineFlow() {
        return new FlowBuilder<Flow>("configurePipelineFlow")
                .start(setUpPipeline())
                .on("COMPLETED").to(clinicalFileTypeDecider())
                .from(clinicalFileTypeDecider()).on("XML").to(xmlFileMappingStep())
                .from(clinicalFileTypeDecider()).on("XLSX").fail()
                .from(clinicalFileTypeDecider()).on("FAIL").fail()
                .build();
    }

    @Bean
    public Flow clinicalDataFlow() {
        return new FlowBuilder<Flow>("clinicalDataFlow")
                .start(clinicalDataStep)
                .next(clinicalMetaDataStep)
                .build();
    }

    @JobScope
    public JobExecutionDecider stepDecider() {
        return new StepDecider();
    }

    @Bean
    public Flow gdcPipelineFlow() {
        return new FlowBuilder<Flow>("gdcPipelineFlow")
                .start(clinicalDataFlow())
                .build();
    }

    @Bean
    public Flow buildFlow() {
        return new FlowBuilder<Flow>("buildFlow")
                .start(stepDecider())
                .on(StepDecider.STEP.ALL.toString()).to(gdcPipelineFlow())
                .on(StepDecider.STEP.CLINICAL.toString()).to(clinicalDataFlow())
                .on(StepDecider.STEP.MUTATION.toString()).fail()
                .build();
    }

    // Flow of All Steps
    @Bean
    public Job gdcJob() {
        return jobBuilderFactory.get("gdcJob")
                .start(configurePipelineFlow())
                .next(buildFlow())
                .end()
                .build();
    }
}
