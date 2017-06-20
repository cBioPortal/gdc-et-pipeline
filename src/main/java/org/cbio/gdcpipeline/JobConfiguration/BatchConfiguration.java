package org.cbio.gdcpipeline.JobConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.reader.ClinicalStreamReader;
import org.cbio.gdcpipeline.tasklet.FileMappingTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.cbio.gdcpipeline.writer.ClinicalDataWriter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

@Configuration
public class BatchConfiguration {
    private static Log LOG = LogFactory.getLog(BatchConfiguration.class);


    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Job clinicalDataJob;

    @Autowired
    private JobLauncher jobLauncher;

    // get from execution context
    private Resource[] inputFiles;


    @Bean
    public Step fileMappingStep() {
        return stepBuilderFactory.get("fileMappingStep")
                .tasklet(createFileMappings())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet createFileMappings(){
        return new FileMappingTasklet();
    }

    public Resource[] getXMLResource() {
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        try {
            inputFiles = resolver.getResources("classpath*:/data/GDC/clinical/*.xml");
        } catch (IOException e) {
            LOG.error("ERROR RESOLVING CLASSPATH");
            e.printStackTrace();
        }
        return inputFiles;

    }

    @Bean
    public MultiResourceItemReader<CBioClinicalDataModel> multiResourceReader() {
        MultiResourceItemReader<CBioClinicalDataModel> reader = new MultiResourceItemReader<>();
        reader.setDelegate(clinicalXMLReader());
        reader.setResources(getXMLResource());
        return reader;

    }

    @Bean
    public ClinicalStreamReader clinicalXMLReader() {
        return new ClinicalStreamReader();

    }

    @Bean
    @StepScope
    public ClinicalDataWriter clinicalDataWriter() {
        return new ClinicalDataWriter();
    }


    @Bean
    public Step clinicalDataStep() {
        // return new ClinicalDataStep().clinicalDataStep();
        return stepBuilderFactory.get("clinicalDataStep")
                .<CBioClinicalDataModel, CBioClinicalDataModel>chunk(100)
                .reader(multiResourceReader())
                //.processor(clinicalDataProcessor())
                .writer(clinicalDataWriter())
                //.stream(clinicalXMLReader())
                .build();
    }


    // Flow of All Steps

    @Bean
    public Job mainJob() {
        return jobBuilderFactory.get("mainJob")
                // .start(fileMappingStep())
                .start(clinicalDataStep())
                // .next(segmentedDataStep())
                // .next(mutationDataStep())
                // .next(cnaDataStep())
                .build();
    }

}
