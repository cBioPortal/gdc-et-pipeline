package org.cbio.gdcpipeline.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.cbio.PatientFileModel;
import org.cbio.gdcpipeline.util.ClinicalPatientFileHeaders;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dixit on 14/06/17.
 */
public class ClinicalPatientDataWriter implements ResourceAwareItemWriterItemStream<CBioClinicalDataModel> {

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    private String dataOutputFile;

    private Resource resource;


    @Value("${clinical.data.patient.file.name}")
    private String CLINICAL_DATA_PATIENT_FILE_NAME;

    @Value("${clinical.metadata.patient.file.name}")
    private String CLINICAL_METADATA_PATIENT_FILE_NAME;

    FlatFileItemWriter<PatientFileModel> dataWriter = new FlatFileItemWriter<>();

    @Autowired
    ClinicalPatientFileHeaders patientFileHeaders;


    private static Log LOG = LogFactory.getLog(ClinicalPatientDataWriter.class);

    private String modifyRow(String[] row) {
        // Tab Delimited
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if (i == 0) {
                sb.append(row[i]);
            } else {
                sb.append("\t");
                sb.append(row[i]);
            }
        }
        return sb.toString();
    }


    private FlatFileHeaderCallback clinicalDataHeader() {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {


                StringBuilder sb = new StringBuilder();
                sb.append(modifyRow(patientFileHeaders.getRow1()));
                sb.append(System.lineSeparator());
                sb.append(modifyRow(patientFileHeaders.getRow2()));
                sb.append(System.lineSeparator());
                sb.append(modifyRow(patientFileHeaders.getRow3()));
                sb.append(System.lineSeparator());
                sb.append(modifyRow(patientFileHeaders.getRow4()));
                sb.append(System.lineSeparator());
                sb.append(modifyRow(patientFileHeaders.getRow5()));
                //sb.append(System.lineSeparator());

                writer.write(sb.toString());

            }
        };
    }

    public FieldExtractor<PatientFileModel> createFieldExtractor() {
        BeanWrapperFieldExtractor<PatientFileModel> ext = new BeanWrapperFieldExtractor<>();

        String[] fields = patientFileHeaders.getRow5().clone();

        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].toLowerCase();
        }
        ext.setNames(fields);
        return ext;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        //configure writer
        dataWriter.setAppendAllowed(true);
        dataWriter.setShouldDeleteIfExists(true);
        dataWriter.setHeaderCallback(clinicalDataHeader());
        dataWriter.setLineSeparator(System.lineSeparator());

        DelimitedLineAggregator<PatientFileModel> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<PatientFileModel> fe = createFieldExtractor();
        lineAggregator.setFieldExtractor(fe);
        dataWriter.setLineAggregator(lineAggregator);

        if (outputDir.isEmpty()) {

            outputDir = sourceDir + File.separator + cancer_study_id + File.separator + "output";
            LOG.info(" No Output directory specified. Output will be at " + outputDir);
            File dir = new File(outputDir);
            dir.mkdir();
            dataOutputFile = outputDir + File.separator + CLINICAL_DATA_PATIENT_FILE_NAME;

        } else {

            File dir = new File(outputDir);
            dir.mkdir();
            dataOutputFile = outputDir + File.separator + CLINICAL_DATA_PATIENT_FILE_NAME;

        }

        File outputFile = new File(dataOutputFile);
        dataWriter.setResource(new FileSystemResource(outputFile));
        dataWriter.open(executionContext);

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }

    @Override
    public void write(List<? extends CBioClinicalDataModel> cBioDataList) throws Exception {
        List<PatientFileModel> patientDataList = new ArrayList<>();

        for (CBioClinicalDataModel cBioData : cBioDataList) {
            patientDataList.add(cBioData.getPatientFileModel());
        }

        try {
            dataWriter.write(patientDataList);
        } catch (Exception e) {
            LOG.error(" Failed to Write Clinical Data to File");
            e.printStackTrace();
        }
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;

    }
}
