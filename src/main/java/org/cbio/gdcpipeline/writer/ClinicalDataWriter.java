package org.cbio.gdcpipeline.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by Dixit on 14/06/17.
 */
public class ClinicalDataWriter implements ResourceAwareItemWriterItemStream<CBioClinicalDataModel> {

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    private String dataOutputFile;
    private Resource resource;


    public static final String CLINICAL_DATA_FILE_NAME = "data_clinical_samples.txt";
    public static final String CLINICAL_METADATA_FILE_NAME = "meta_clinical_samples.txt";
    FlatFileItemWriter<CBioClinicalDataModel> dataWriter = new FlatFileItemWriter<>();

    private static Log LOG = LogFactory.getLog(ClinicalDataWriter.class);


    public FlatFileHeaderCallback clinicalDataHeader() {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                //TODO
                StringBuffer header = new StringBuffer("#PATIENT IDENTIFIER\tSAMPLE_ID\tGENDER\tAGE\tOVERALL SURVIVAL STATUS");
                String row2 = "";
                String row3 = "";
                String row4 = "";
                String row5 = System.lineSeparator() + "PATIENT_ID\tSAMPLE_ID\tSEX\tAGE\tOS_STATUS";
                header.append(row5);
                writer.write(header.toString());

            }
        };
    }

    public FieldExtractor<CBioClinicalDataModel> createFieldExtractor() {
        BeanWrapperFieldExtractor<CBioClinicalDataModel> ext = new BeanWrapperFieldExtractor<>();
        ext.setNames(new String[]{"patient_id", "sample_id", "sex", "age", "os_status"});
        return ext;
    }

    public void writeData(List<CBioClinicalDataModel> cBioDataList) {

        dataWriter.open(new ExecutionContext());
        try {
            dataWriter.write(cBioDataList);
        } catch (Exception e) {
            LOG.error(" Failed to Write Clinical Data to File");
            e.printStackTrace();
        }

    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        //configure writer
        dataWriter.setAppendAllowed(true);
        dataWriter.setShouldDeleteIfExists(false);
        dataWriter.setHeaderCallback(clinicalDataHeader());
        dataWriter.setLineSeparator(System.lineSeparator());

        DelimitedLineAggregator<CBioClinicalDataModel> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<CBioClinicalDataModel> fe = createFieldExtractor();
        lineAggregator.setFieldExtractor(fe);
        dataWriter.setLineAggregator(lineAggregator);
        dataOutputFile = outputDir + File.separator + CLINICAL_DATA_FILE_NAME;
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
        writeData((List<CBioClinicalDataModel>)cBioDataList);
    }

    @Override
    public void setResource(Resource resource) {
        this.resource=resource;

    }
}
