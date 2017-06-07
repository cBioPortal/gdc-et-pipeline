package org.cbio.gdcpipeline.JobConfiguration;


import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.gdc.clinical.GDCClinicalDataModel;

import org.cbio.gdcpipeline.processor.ClinicalDataProcessor;
import org.cbio.gdcpipeline.util.XMLConvert;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.oxm.castor.CastorMappingException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.support.MarshallingSource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.xml.sax.SAXException;

import java.io.*;



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

        CastorMarshaller unmarshaller = new CastorMarshaller();
        try {

            //unmarshaller.setMappingLocation(new FileSystemResource("/Users/Dixit/Documents/github/gsoc/gdc-et-pipeline/src/main/resources/mapping.xml"));
            unmarshaller.setRootElement("patient");
            unmarshaller.afterPropertiesSet();
        }catch (CastorMappingException cm){
            cm.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StaxEventItemReader<GDCClinicalDataModel> reader = new StaxEventItemReader<>();

        reader.setFragmentRootElementName("patient");

        reader.setUnmarshaller(unmarshaller);
        /*try {
            Object ob = reader.read();
            System.out.println(ob.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //==================== Sample Code ===================
      /*  XMLConvert xml = new XMLConvert();
        xml.setUnmarshaller(unmarshaller);
        try {
           Object ob =  xml.convertFromXMLToObject("/Users/Dixit/Documents/github/gsoc/gdc-et-pipeline/src/main/resources/mapping.xml");
            System.out.println(ob.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }*/



/*

        Mapping map = new Mapping();
        XMLContext xmlContext = new XMLContext();

        try {
            map.loadMapping("/Users/Dixit/Documents/github/gsoc/gdc-et-pipeline/src/main/resources/mapping.xml");
            xmlContext.addMapping(map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MappingException e) {
            e.printStackTrace();
        }
        Reader r = null;
         try {
             r = new FileReader("/Users/Dixit/Documents/github/gsoc/gdc-et-pipeline/src/main/resources/data/GDC/nationwidechildrens.org_clinical.TCGA-3C-AAAU.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Unmarshaller unm = xmlContext.createUnmarshaller();

        unm.setClass(GDCClinicalDataModel.class);

        try {
            GDCClinicalDataModel gd = (GDCClinicalDataModel)unm.unmarshal(r);
            System.out.println("======================SAMPLE===============");
            System.out.println(gd.getPatient_id());
        } catch (MarshalException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
*/

        //===================================================



        return reader;


    }



    @Bean
    public FlatFileHeaderCallback clinicalDataHeader(){
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                String header="patient_id\tsample_id\tsex\tage\tos_status";
                writer.write(header);

            }
        };
    }


    /*@Bean
    public ItemWriter<GDCClinicalDataModel> clinicalDataWriter2() {
        System.out.print("====== NOW PRINITNING=========");

        return items -> {
            for (GDCClinicalDataModel item : items) {
                System.out.println(item.toString());
            }
        };
    }*/

    @Bean
    public ClinicalDataProcessor processClinicalData() { return new ClinicalDataProcessor(); }


    public FieldExtractor<CBioClinicalDataModel> createFieldExtractor(){
        BeanWrapperFieldExtractor<CBioClinicalDataModel> ext = new BeanWrapperFieldExtractor<>();
        ext.setNames(new String[]{"patient_id", "sample_id", "sex", "age","os_status"});
        return  ext;
    }

    @Bean
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


    @Bean
    public Step extractAndTransformStep() {
        return stepBuilderFactory.get("extractAndTransformStep")
                .<GDCClinicalDataModel, CBioClinicalDataModel>chunk(1)
                .reader(multiResourceClinicalDataReader())
                .processor(processClinicalData())
                .writer(clinicalDataWriter())
                .build();
    }


    @Bean
    public Job clinicalDataJob() {
        return jobBuilderFactory.get("clinicalDataJob")
                .start(extractAndTransformStep())
                .build();
    }

}
