package org.cbio.gdcpipeline.reader;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.MutationRecord;
import org.cbio.gdcpipeline.util.DataFileUtils;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dixit
 */
public class MutationReader implements ItemStreamReader<MutationRecord> {
    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    private List<MutationRecord> mafRecords = new ArrayList<>();
    private static Log LOG = LogFactory.getLog(MutationReader.class);

    @Override
    public MutationRecord read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!mafRecords.isEmpty()) {
            return mafRecords.remove(0);
        }
        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        String maf_file = (String)executionContext.get("mafToProcess");
        File file = new File(sourceDir + File.separator + maf_file);

        FlatFileItemReader<MutationRecord> reader = new FlatFileItemReader<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
        MultiKeyMap mafFileMetadata = new MultiKeyMap();
        try {
            mafFileMetadata = DataFileUtils.loadDataFileMetadata(file);
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
        String[] mafHeader = (String[]) mafFileMetadata.get(file.getName(), "header");
        tokenizer.setNames(mafHeader);

        DefaultLineMapper<MutationRecord> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mutationFieldSetMapper());
        reader.setResource(new FileSystemResource(file));
        reader.setLineMapper(lineMapper);
        //TODO get from data file utils
        reader.setLinesToSkip(5);
        reader.open(new ExecutionContext());

        try {
            MutationRecord record = reader.read();
            while (record != null) {
                mafRecords.add(record);
                record = reader.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ItemStreamException("Error reading record");
        }
        reader.close();
        executionContext.put("maf_file", maf_file);
    }

    private FieldSetMapper<MutationRecord> mutationFieldSetMapper() {
        return (FieldSetMapper<MutationRecord>) (FieldSet fs) -> {
            MutationRecord record = new MutationRecord();
            for (String header : record.getHeader()) {
                try {
                    record.getClass().getMethod("set" + header, String.class).invoke(record, fs.readString(header));
                } catch (Exception e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.error(" Error in setting record for :" + header);
                    }
                    e.printStackTrace();
                }
            }
            return record;
        };
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }
}
