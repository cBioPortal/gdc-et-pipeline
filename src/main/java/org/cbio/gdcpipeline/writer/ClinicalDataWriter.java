package org.cbio.gdcpipeline.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.PatientMetadataManagerImpl;
import org.cbio.gdcpipeline.model.SampleMetadataManagerImpl;
import org.cbio.gdcpipeline.model.cbio.ClinicalDataModel;
import org.cbio.gdcpipeline.model.cbio.Patient;
import org.cbio.gdcpipeline.model.cbio.Sample;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dixit on 02/07/17.
 */
public class ClinicalDataWriter implements ItemStreamWriter<ClinicalDataModel> {

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    private ExecutionContext executionContext;

    private FlatFileItemWriter<ClinicalDataModel> patientWriter = new FlatFileItemWriter<>();
    private FlatFileItemWriter<ClinicalDataModel> sampleWriter = new FlatFileItemWriter<>();

    private static Log LOG = LogFactory.getLog(ClinicalDataWriter.class);


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        this.executionContext = executionContext;
    }


    @Override
    public void write(List<? extends ClinicalDataModel> list) throws Exception {
        //configure writer
        List<ClinicalDataModel> patientList = new ArrayList<>();
        List<ClinicalDataModel> sampleList = new ArrayList<>();

        for (ClinicalDataModel data : list) {
            if (data instanceof Patient) {
                patientList.add(data);

            } else if (data instanceof Sample) {
                sampleList.add(data);
            }
        }

        configurePatientWriter();
        configureSampleWriter();


        patientWriter.write(patientList);
        sampleWriter.write(sampleList);

    }

    private void configureSampleWriter() throws Exception {
        Sample sample = new Sample();

        sampleWriter.setAppendAllowed(true);
        sampleWriter.setLineSeparator(System.lineSeparator());
        sampleWriter.setHeaderCallback(clinicalDataHeader(sample));

        DelimitedLineAggregator<ClinicalDataModel> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<ClinicalDataModel> fe = createFieldExtractor(sample);
        lineAggregator.setFieldExtractor(fe);
        sampleWriter.setLineAggregator(lineAggregator);

        String filename = (String) sample.getClass().getMethod("getData_file_name").invoke(sample);
        String outputFile = outputDir + File.separator + filename;
        sampleWriter.setResource(new FileSystemResource(new File(outputFile)));
        sampleWriter.open(this.executionContext);
    }


    private void configurePatientWriter() throws Exception {

        Patient patient = new Patient();

        patientWriter.setAppendAllowed(true);
        patientWriter.setLineSeparator(System.lineSeparator());
        patientWriter.setHeaderCallback(clinicalDataHeader(patient));

        DelimitedLineAggregator<ClinicalDataModel> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<ClinicalDataModel> fe = createFieldExtractor(patient);
        lineAggregator.setFieldExtractor(fe);
        patientWriter.setLineAggregator(lineAggregator);

        String filename = (String) patient.getClass().getMethod("getData_file_name").invoke(patient);
        String outputFile = outputDir + File.separator + filename;
        patientWriter.setResource(new FileSystemResource(new File(outputFile)));
        patientWriter.open(this.executionContext);


    }


    private FlatFileHeaderCallback clinicalDataHeader(ClinicalDataModel data) {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write(data.getHeaders());
            }
        };
    }


    private FieldExtractor<ClinicalDataModel> createFieldExtractor(ClinicalDataModel data) throws Exception {
        BeanWrapperFieldExtractor<ClinicalDataModel> ext = new BeanWrapperFieldExtractor<>();
        String[] fields = null;

        if (data instanceof Patient) {
            fields = (String[]) new PatientMetadataManagerImpl().getFullHeader().get("row5").toArray();
        } else if (data instanceof Sample) {
            fields = (String[]) new SampleMetadataManagerImpl().getFullHeader().get("row5").toArray();
        } else {
            if (LOG.isErrorEnabled()) {
                LOG.error("Unknown instance type");
            }
            throw new Exception();
        }
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].toLowerCase();
        }
        ext.setNames(fields);
        return ext;

    }


    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }


}
