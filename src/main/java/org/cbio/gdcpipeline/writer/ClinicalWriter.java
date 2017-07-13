package org.cbio.gdcpipeline.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.buf.StringUtils;
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
import java.util.Map;

/**
 * Created by Dixit on 02/07/17.
 */
public class ClinicalWriter implements ItemStreamWriter<ClinicalDataModel> {

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    @Value("${clinical.data.patient.file}")
    private String patientFile;
    @Value("${clinical.data.sample.file}")
    private String sampleFile;

    private ExecutionContext executionContext;
    private String writerType;
    private FlatFileItemWriter<ClinicalDataModel> clinicalWriter = new FlatFileItemWriter<>();


    private static Log LOG = LogFactory.getLog(ClinicalWriter.class);


    public ClinicalWriter(String writerType) {
        this.writerType = writerType;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        configureWriter(writerType);
    }


    @Override
    public void write(List<? extends ClinicalDataModel> list) throws Exception {
        //configure writer
        List<ClinicalDataModel> filterList = new ArrayList<>();
        if (writerType.equals("patient")) {
            for (ClinicalDataModel data : list) {
                if (data instanceof Patient) {
                    filterList.add(data);
                }
            }
        } else if (writerType.equals("sample")) {
            for (ClinicalDataModel data : list) {
                if (data instanceof Sample) {
                    filterList.add(data);
                }
            }
        }
        clinicalWriter.write(filterList);
    }


    private void configureWriter(String writerType) {
        ClinicalDataModel data = null;
        String outputFile = outputDir + File.separator;
        if (writerType.equals("patient")) {
            data = new Patient();
            outputFile += patientFile;
        } else {
            data = new Sample();
            outputFile += sampleFile;
        }

        clinicalWriter.setShouldDeleteIfExists(true);
        clinicalWriter.setLineSeparator(System.lineSeparator());
        clinicalWriter.setHeaderCallback(clinicalDataHeader(data));
        DelimitedLineAggregator<ClinicalDataModel> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<ClinicalDataModel> fe = createFieldExtractor(data);
        lineAggregator.setFieldExtractor(fe);
        clinicalWriter.setLineAggregator(lineAggregator);
        clinicalWriter.setResource(new FileSystemResource(new File(outputFile)));
        clinicalWriter.open(this.executionContext);


    }

    private FlatFileHeaderCallback clinicalDataHeader(ClinicalDataModel data) {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                Map<String, List<String>> headers = data.getHeaders();
                StringBuilder sb = new StringBuilder();
                sb.append(StringUtils.join(headers.get("displayNames"), '\t')).append("\n");
                sb.append(StringUtils.join(headers.get("description"), '\t')).append("\n");
                sb.append(StringUtils.join(headers.get("datatype"), '\t')).append("\n");
                sb.append(StringUtils.join(headers.get("priority"), '\t')).append("\n");
                List<String> list = headers.get("headers");
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, list.get(i).toUpperCase());
                }
                sb.append(StringUtils.join(list, '\t'));
                writer.write(sb.toString());

            }
        };
    }


    private FieldExtractor<ClinicalDataModel> createFieldExtractor(ClinicalDataModel data) {
        BeanWrapperFieldExtractor<ClinicalDataModel> ext = new BeanWrapperFieldExtractor<>();
        List<String> fieldList = data.getFields();
        String[] fields = new String[fieldList.size()];
        fields = data.getFields().toArray(fields);
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
