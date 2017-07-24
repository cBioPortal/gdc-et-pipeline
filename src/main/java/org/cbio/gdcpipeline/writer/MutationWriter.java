package org.cbio.gdcpipeline.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.buf.StringUtils;
import org.cbio.gdcpipeline.model.cbio.MutationRecord;
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
import java.util.List;

/**
 * Created by Dixit
 */
public class MutationWriter implements ItemStreamWriter<MutationRecord> {
    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    private FlatFileItemWriter<MutationRecord> mutationWriter = new FlatFileItemWriter<>();
    private static Log LOG = LogFactory.getLog(MutationWriter.class);
    private ExecutionContext executionContext;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        String maf_filename = (String)executionContext.get("maf_file");
        configureWriter(maf_filename);
    }

    private void configureWriter(String maf_filename) {
        String outputFile = outputDir + File.separator + maf_filename;
        MutationRecord record = new MutationRecord();
        mutationWriter.setShouldDeleteIfExists(true);
        mutationWriter.setLineSeparator(System.lineSeparator());
        mutationWriter.setHeaderCallback(mutationHeader(record));
        DelimitedLineAggregator<MutationRecord> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<MutationRecord> fe = createFieldExtractor(record);
        lineAggregator.setFieldExtractor(fe);
        mutationWriter.setLineAggregator(lineAggregator);
        mutationWriter.setResource(new FileSystemResource(new File(outputFile)));
        mutationWriter.open(this.executionContext);
    }

    private FlatFileHeaderCallback mutationHeader(MutationRecord data) {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                List<String> headers = data.getHeader();
                StringBuilder sb = new StringBuilder();
                sb.append(StringUtils.join(headers,'\t'));
                writer.write(sb.toString());
            }
        };
    }

    private FieldExtractor<MutationRecord> createFieldExtractor(MutationRecord data)  {
        BeanWrapperFieldExtractor<MutationRecord> ext = new BeanWrapperFieldExtractor<>();
        List<String> fieldList = data.getHeader();
        String[] fields = new String[fieldList.size()];
        fields = data.getHeader().toArray(fields);
        ext.setNames(fields);
        return ext;
    }

    @Override
    public void write(List<? extends MutationRecord> list) throws Exception {
        mutationWriter.write(list);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }
}
