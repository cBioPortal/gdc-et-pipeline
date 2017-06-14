package org.cbio.gdcpipeline.JobConfiguration;


import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;

import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.brca._2.TcgaBcr;
import org.cbio.gdcpipeline.processor.ClinicalDataProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
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

    public MultiResourceItemReader<TcgaBcr> multiResourceClinicalDataReader() {

        MultiResourceItemReader<TcgaBcr> reader = new MultiResourceItemReader<>();
        reader.setDelegate(readClinicalData());
        reader.setResources(inputFiles);
        return reader;
    }


    //@Bean
    public StaxEventItemReader<TcgaBcr> readClinicalData(){

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("org.cbio.gdcpipeline.model.cbio.nci.tcga.bcr.xml.clinical.brca._2");
        //jaxb2Marshaller.setClassesToBeBound(TcgaBcr.class);
        try {
            jaxb2Marshaller.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileReader r = null;
        try {
            r = new FileReader("/Users/Dixit/Documents/github/gsoc/gdc-et-pipeline/src/main/resources/data/GDC/nationwidechildrens.org_clinical.TCGA-3C-AAAU.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JAXBContext context = null;
        Unmarshaller unmarshaller = null;
        try {
            context = JAXBContext.newInstance(TcgaBcr.class);
            unmarshaller = context.createUnmarshaller();

            TcgaBcr tcgaBcr = (TcgaBcr)unmarshaller.unmarshal(r);
            System.out.println(tcgaBcr.getPatient());
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        XStreamMarshaller xstream = new XStreamMarshaller();
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("brca:tcga_bcr", TcgaBcr.class);

        xstream.setAliases(aliases);
        StaxEventItemReader<TcgaBcr> reader = new StaxEventItemReader<>();

        reader.setFragmentRootElementName("brca:tcga_bcr");
        reader.setResource(new ClassPathResource("/data/GDC/clinical/nationwidechildrens.org_clinical.TCGA-3C-AAAU.xml"));
        reader.setUnmarshaller(jaxb2Marshaller);
        try {
            reader.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;


    }


    public FlatFileHeaderCallback clinicalDataHeader(){
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                String header="patient_id\tsample_id\tsex\tage\tos_status";
                writer.write(header);

            }
        };
    }



    //@Bean

    public static ClinicalDataProcessor processClinicalData() {
        System.out.print("======================= ABOUT TO CALL PROCESSOR===========");
        System.out.print("======================= ABOUT TO CALL PROCESSOR===========");
        System.out.print("======================= ABOUT TO CALL PROCESSOR===========");
        ClinicalDataProcessor dp = new ClinicalDataProcessor();
        System.out.println(dp.toString());

        return new ClinicalDataProcessor(); }


    public FieldExtractor<CBioClinicalDataModel> createFieldExtractor(){
        BeanWrapperFieldExtractor<CBioClinicalDataModel> ext = new BeanWrapperFieldExtractor<>();
        ext.setNames(new String[]{"patient_id", "sample_id", "sex", "age","os_status"});
        //ext.setNames(new String[]{"patient", "admin", "sex", "age","os_status"});

        return  ext;
    }

    //@Bean
    public ItemWriter<TcgaBcr> clinicalDataWriter2() {
        return items -> {
            for (TcgaBcr item : items) {
                System.out.println(item.toString());
            }
        };
    }

   // @Bean
    public FlatFileItemWriter<CBioClinicalDataModel> clinicalDataWriter() {

        FlatFileItemWriter<CBioClinicalDataModel> dataWriter = new FlatFileItemWriter<>();

        dataWriter.setAppendAllowed(true);
        dataWriter.setHeaderCallback(clinicalDataHeader());
        dataWriter.setLineSeparator(System.lineSeparator());

        DelimitedLineAggregator<CBioClinicalDataModel> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<CBioClinicalDataModel> fe = createFieldExtractor();
        lineAggregator.setFieldExtractor(fe);
        dataWriter.setLineAggregator(lineAggregator);
        dataWriter.setResource(new FileSystemResource("/Users/Dixit/Documents/github/gsoc/gdc-et-pipeline/src/main/resources/data/GDC/output.txt"));

        return dataWriter;



    }


    //@Bean
    public Step extractAndTransformStep() {
        return stepBuilderFactory.get("extractAndTransformStep")
                .<TcgaBcr, CBioClinicalDataModel>chunk(1)
                //.reader(multiResourceClinicalDataReader())
                .reader(readClinicalData())
                .processor(processClinicalData())
                .writer(clinicalDataWriter())
                .build();
    }


    //@Bean
    public Job clinicalDataJob() {
        return jobBuilderFactory.get("clinicalDataJob")
                .start(extractAndTransformStep())
                .build();
    }

}
