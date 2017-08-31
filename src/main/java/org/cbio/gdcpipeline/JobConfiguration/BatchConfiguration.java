package org.cbio.gdcpipeline.JobConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.decider.ClinicalFileTypeDecider;
import org.cbio.gdcpipeline.decider.StepDecider;
import org.cbio.gdcpipeline.tasklet.BiospecimenXmlDataTasklet;
import org.cbio.gdcpipeline.tasklet.ProcessManifestFileTasklet;
import org.cbio.gdcpipeline.tasklet.SetUpPipelineTasklet;
import org.cbio.gdcpipeline.util.CommonDataUtil;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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

/**
 * @author Dixit Patel
 */
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

    @Resource(name = "mutationDataStep")
    Step mutationDataStep;

    @Resource(name = "mutationMetaDataStep")
    Step mutationMetaDataStep;

    @Value("${chunk.interval}")
    private int chunkInterval;

    @Bean
    public Step setUpPipeline() {
        return stepBuilderFactory.get("setUpPipeline")
                .tasklet(setUpPipelineTasklet())
                .build();
    }

    @Bean
    public ExecutionContextPromotionListener processManifestFileListener() {
        String[] keys = new String[]{"gdcFileMetadatas"};
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        executionContextPromotionListener.setKeys(keys);
        return executionContextPromotionListener;

    }

    @Bean
    public Step processManifestFile() {
        return stepBuilderFactory.get("processManifestFile")
                .listener(processManifestFileListener())
                .tasklet(processManifestFileTasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet processManifestFileTasklet() {
        return new ProcessManifestFileTasklet();
    }

    @Bean
    @StepScope
    public Tasklet setUpPipelineTasklet() {
        return new SetUpPipelineTasklet();
    }


    @Bean
    @StepScope
    public Tasklet biospecimenXmlDataTasklet() {
        return new BiospecimenXmlDataTasklet();
    }

    @Bean
    public ExecutionContextPromotionListener biospecimenXmlDataListener() {
        String[] keys = new String[]{"barcodeToSamplesMap"};
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        executionContextPromotionListener.setKeys(keys);
        return executionContextPromotionListener;

    }

    @Bean
    public Step biospecimenXmlDataStep() {
        return stepBuilderFactory.get("biospecimenXmlDataStep")
                .listener(biospecimenXmlDataListener())
                .tasklet(biospecimenXmlDataTasklet())
                .build();
    }

    @Bean
    public Flow clinicalXmlDataFlow() {
        return new FlowBuilder<Flow>("clinicalXmlDataFlow")
                .start(biospecimenXmlDataStep())
                .next(clinicalDataStep)
                .next(clinicalMetaDataStep)
                .build();
    }

    @Bean
    public Flow mutationDataFlow() {
        return new FlowBuilder<Flow>("mutationDataFlow")
                .start(mutationDataStep)
                .from(mutationDataStep).on("CONTINUE").to(mutationDataStep)
                .next(mutationMetaDataStep)
                .build();
    }

    @Bean
    public JobExecutionDecider clinicalFileTypeDecider() {
        return new ClinicalFileTypeDecider();
    }

    @Bean
    public Flow clinicalFileTypeDeciderFlow() {
        return new FlowBuilder<Flow>("clinicalFileTypeDeciderFlow")
                .start(clinicalFileTypeDecider())
                .on(CommonDataUtil.GDC_DATAFORMAT.BCR_XML.toString()).to(clinicalXmlDataFlow())
                .on("FAIL").fail()
                .build();
    }

    @Bean
    public Flow gdcAllDatatypesFlow() {
        return new FlowBuilder<Flow>("gdcAllDatatypesFlow")
                .start(clinicalFileTypeDecider())
                .on(CommonDataUtil.GDC_DATAFORMAT.BCR_XML.toString()).to(clinicalXmlDataFlow())
                .next(mutationDataFlow())
                .build();
    }

    @Bean
    public Flow configurePipelineFlow() {
        return new FlowBuilder<Flow>("configurePipelineFlow")
                .start(setUpPipeline())
                .next(processManifestFile())
                .build();
    }

    public Flow stepDeciderFlow() {
        return new FlowBuilder<Flow>("stepDeciderFlow")
                .start(stepDecider())
                .on(StepDecider.STEP.ALL.toString()).to(gdcAllDatatypesFlow())
                .from(stepDecider()).on(StepDecider.STEP.CLINICAL.toString()).to(clinicalFileTypeDeciderFlow())
                .from(stepDecider()).on(StepDecider.STEP.MUTATION.toString()).to(mutationDataFlow())
                .build();
    }

    @Bean
    public JobExecutionDecider stepDecider() {
        return new StepDecider();
    }

    // Flow of All Steps
    @Bean
    public Job gdcJob() {
        return jobBuilderFactory.get("gdcJob")
                .start(configurePipelineFlow())
                .next(stepDeciderFlow())
                .end()
                .build();
    }
}
