package org.cbio.gdcpipeline.writer;

import org.apache.tomcat.util.buf.StringUtils;
import org.cbioportal.models.MutationRecord;
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
 * @author Dixit Patel
 */
public class MutationWriter implements ItemStreamWriter<MutationRecord> {
    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    @Value("#{jobExecutionContext[separate_maf]}")
    private String separate_maf;
    private FlatFileItemWriter<MutationRecord> mutationWriter = new FlatFileItemWriter<>();
    private ExecutionContext executionContext;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        File maf_filename = (File) executionContext.get("maf_file_to_write");
        configureWriter(maf_filename);
    }

    private void configureWriter(File maf_filename) {
        MutationRecord record = new MutationRecord();
        mutationWriter.setShouldDeleteIfExists(true);
        mutationWriter.setLineSeparator(System.lineSeparator());
        mutationWriter.setHeaderCallback(mutationHeader(record));
        DelimitedLineAggregator<MutationRecord> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<MutationRecord> fe = createFieldExtractor(record);
        lineAggregator.setFieldExtractor(fe);
        mutationWriter.setLineAggregator(lineAggregator);
        mutationWriter.setResource(new FileSystemResource(maf_filename));
        mutationWriter.open(this.executionContext);
    }

    private FlatFileHeaderCallback mutationHeader(MutationRecord data) {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                List<String> headers = data.getHeaderWithAdditionalFields();
                StringBuilder sb = new StringBuilder();
                sb.append(StringUtils.join(headers, '\t'));
                writer.write(sb.toString());
            }
        };
    }

    private FieldExtractor<MutationRecord> createFieldExtractor(MutationRecord data) {
        BeanWrapperFieldExtractor<MutationRecord> ext = new BeanWrapperFieldExtractor<>();
        List<String> fieldList = data.getHeaderWithAdditionalFields();
        String[] fields = new String[fieldList.size()];
        fields = fieldList.toArray(fields);
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
