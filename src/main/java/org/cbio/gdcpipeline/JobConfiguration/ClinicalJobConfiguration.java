package org.cbio.gdcpipeline.JobConfiguration;


import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.gdc.clinical.GDCClinicalDataModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class ClinicalJobConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Value("classpath*:/data/GDC/*.xml")
    private Resource[] inputFiles;

    @Bean
    public MultiResourceItemReader<GDCClinicalDataModel> multiResourceClinicalDataReader() {

        MultiResourceItemReader<GDCClinicalDataModel> reader = new MultiResourceItemReader<>();
        reader.setDelegate(readClinicalData());
        reader.setResources(inputFiles);
        return reader;
    }


    @Bean
    public StaxEventItemReader<GDCClinicalDataModel> readClinicalData(){

        XStreamMarshaller unmarshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("patient", GDCClinicalDataModel.class);

        unmarshaller.setAliases(aliases);

        StaxEventItemReader<GDCClinicalDataModel> reader = new StaxEventItemReader<>();

        reader.setFragmentRootElementName("patient");
        reader.setUnmarshaller(unmarshaller);

        return reader;


    }


    @Bean
    public ItemWriter<GDCClinicalDataModel> clinicalDataWriter() {
        return items -> {
            for (GDCClinicalDataModel item : items) {
                System.out.println(item.toString());
            }
        };
    }




    @Bean
    public Step extractAndTransformStep() {
        return stepBuilderFactory.get("extractAndTransformStep")
                .<GDCClinicalDataModel, GDCClinicalDataModel>chunk(1)
                .reader(multiResourceClinicalDataReader())
                //.processor(processClinicalData())
                .writer(clinicalDataWriter())
                .build();
    }


    @Bean
    public Job segmentedDatajob() {
        return jobBuilderFactory.get("segmentedDatajob")
                .start(extractAndTransformStep())
                .build();
    }

}
