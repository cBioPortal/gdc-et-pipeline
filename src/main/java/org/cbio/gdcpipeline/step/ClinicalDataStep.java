//package org.cbio.gdcpipeline.step;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.cbio.gdcpipeline.GDCPipelineApplication;
//import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
//import org.cbio.gdcpipeline.processor.ClinicalDataProcessor;
//import org.cbio.gdcpipeline.reader.ClinicalStreamReader;
//import org.cbio.gdcpipeline.writer.ClinicalDataWriter;
//import org.omg.CORBA.Object;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.MultiResourceItemReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.Resource;
//
///**
// * Created by Dixit on 16/06/17.
// */
//public class ClinicalDataStep {
//
//
//    @Autowired
//    JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    StepBuilderFactory stepBuilderFactory;
//
//    private static Log LOG = LogFactory.getLog(ClinicalDataStep.class);
//
//
//
//    //TODO get from execution context
//    @Value("classpath*:/data/GDC/*.xml")
//    private Resource[] inputFiles;
//
//    @Bean
//    public MultiResourceItemReader<CBioClinicalDataModel> multiResourceReader(){
//
//        MultiResourceItemReader<CBioClinicalDataModel> reader = new MultiResourceItemReader<>();
//        reader.setDelegate(clinicalXMLReader());
//        reader.setResources(inputFiles);
//        return reader;
//
//
//    }
//
//    @Bean
//    public ClinicalStreamReader clinicalXMLReader(){
//        return new ClinicalStreamReader();
//
//    }
//    //TODO
//    // How to write a single item got from reader's read method  ?
//    @Bean
//    public ClinicalDataWriter clinicalDataWriter(){
//        return new ClinicalDataWriter();
//    }
//
//    @Bean
//    public Step clinicalDataStep(){
//
//        return stepBuilderFactory.get("clinicalDataStep")
//                .<CBioClinicalDataModel,CBioClinicalDataModel>chunk(1)
//                .reader(multiResourceReader())
//                //.processor(clinicalDataProcessor())
//                .writer(clinicalDataWriter())
//                //.stream(clinicalXMLReader())
//                .build();
//
//    }
//}
