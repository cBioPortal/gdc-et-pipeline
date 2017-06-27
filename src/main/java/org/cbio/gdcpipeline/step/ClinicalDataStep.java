package org.cbio.gdcpipeline.step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.processor.ClinicalDataProcessor;
import org.cbio.gdcpipeline.reader.ClinicalStreamReader;
import org.cbio.gdcpipeline.writer.ClinicalPatientDataWriter;
import org.cbio.gdcpipeline.writer.ClinicalSampleDataWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    //TODO Resolve Bean issue
    // Spring cannot get @Value from execution context's since this class is marked with @Configuration
    // and they are initialized only after AppContext is run
    // Cannot mark this step as step scope or jobscope as well : null pointer exception
    // possibility : can retrieve values from listener but how to pass to multi resource reader ?
    // configure spring job launcher code ?

    //@Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir = "/Users/Dixit/Documents/github/gsoc/gdc-et-pipeline/src/main/resources/data/GDC";

    // @Value("#{jobParameters[study]}")
    private String cancer_study_id = "TCGA_BRCA";

    // @Value("#{jobExecutionContext[uuidToFilesMap]}")
    private HashMap<String, List<String>> uuidToFilesMap;

    private String test;

    public Resource[] getXMLResource() {

        File projectDir = new File(sourceDir + File.separator + cancer_study_id);

        List<File> xmlFiles = new ArrayList<>();
        for (File file : projectDir.listFiles()) {
            if (!file.isDirectory()) {
                if (file.getName().endsWith(".xml") && !file.getName().contains("biospecimen")) {
                    xmlFiles.add(file);
                }
            }
        }

        FileSystemResource[] fr = new FileSystemResource[xmlFiles.size()];
        for (int i = 0; i < xmlFiles.size(); i++) {
            fr[i] = new FileSystemResource(xmlFiles.get(i));
        }

        return fr;

    }

    @Bean
    @StepScope
    public MultiResourceItemReader<CBioClinicalDataModel> multiResourceReader() {

        MultiResourceItemReader<CBioClinicalDataModel> reader = new MultiResourceItemReader<>();
        reader.setDelegate(clinicalXMLReader());
        reader.setResources(getXMLResource());
        return reader;

    }

    @Bean
    @StepScope
    public ClinicalStreamReader clinicalXMLReader() {
        return new ClinicalStreamReader();

    }

    @Bean
    @StepScope
    public ClinicalDataProcessor clinicalDataProcessor() {
        return new ClinicalDataProcessor();
    }

    @Bean
    public CompositeItemWriter<CBioClinicalDataModel> compositeItemWriter() {

        List<ItemWriter<? super CBioClinicalDataModel>> delegates = new ArrayList<>(2);

        delegates.add(clinicalPatientDataWriter());
        delegates.add(clinicalSampleDataWriter());

        CompositeItemWriter<CBioClinicalDataModel> compositeItemWriter =
                new CompositeItemWriter<>();

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
    public ClinicalPatientDataWriter clinicalPatientDataWriter() {
        return new ClinicalPatientDataWriter();
    }

    @Bean
    @StepScope
    public ClinicalSampleDataWriter clinicalSampleDataWriter() {
        return new ClinicalSampleDataWriter();
    }


    @Bean
    public Step runClinicalDataStep() {

        return stepBuilderFactory.get("clinicalDataStep")
                .<CBioClinicalDataModel, CBioClinicalDataModel>chunk(chunkInterval)
                .reader(multiResourceReader())
                .processor(clinicalDataProcessor())
                .writer(compositeItemWriter())
                .build();

    }


}
