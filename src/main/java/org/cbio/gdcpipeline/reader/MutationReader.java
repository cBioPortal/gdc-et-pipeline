package org.cbio.gdcpipeline.reader;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.buf.StringUtils;
import org.cbio.gdcpipeline.model.cbio.MutationRecord;
import org.cbio.gdcpipeline.util.MutationDataFileUtils;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dixit
 */
public class MutationReader implements ItemStreamReader<MutationRecord> {
    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    @Value("#{jobParameters[cancer_study_id]}")
    private String cancer_study_id;

    @Value("#{jobParameters[separate_maf]}")
    private String separate_maf;

    private List<MutationRecord> mafRecords = new ArrayList<>();
    private static Log LOG = LogFactory.getLog(MutationReader.class);
    public static String DEFAULT_MERGED_MAF_FILENAME = "merged_maf_file.maf";
    private Map<MutationRecord, Set<String>> seenMafRecord = new HashMap<>();

    @Override
    public MutationRecord read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!mafRecords.isEmpty()) {
            return mafRecords.remove(0);
        }
        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        File maf_file = (File) executionContext.get("mafToProcess");
        FlatFileItemReader<MutationRecord> reader = new FlatFileItemReader<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
        MultiKeyMap mafFileMetadata = new MultiKeyMap();
        try {
            mafFileMetadata = MutationDataFileUtils.loadDataFileMetadata(maf_file);
        } catch (IOException e) {
            e.printStackTrace();

        }
        String[] mafHeader = (String[]) mafFileMetadata.get(maf_file.getName(), "header");
        tokenizer.setNames(mafHeader);
        DefaultLineMapper<MutationRecord> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mutationFieldSetMapper());
        reader.setResource(new FileSystemResource(maf_file));
        reader.setLineMapper(lineMapper);
        int metadataCount = (int) mafFileMetadata.get(maf_file.getName(), "metadataCount");
        //include the header row
        reader.setLinesToSkip(metadataCount + 1);
        reader.open(new ExecutionContext());
        try {
            //TODO for very large files, there can be performance issues ?
            MutationRecord record = reader.read();
            while (record != null) {
                addRecord(record, maf_file.getName());
                record = reader.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ItemStreamException("Error reading record");
        }
        reader.close();

        if (!separate_maf.isEmpty()) {
            if (separate_maf.equalsIgnoreCase("true")) {
                executionContext.put("maf_file_to_write", maf_file);
            } else {
                File MERGED_MAF_FILE_NAME = new File(outputDir, DEFAULT_MERGED_MAF_FILENAME);
                executionContext.put("maf_file_to_write", MERGED_MAF_FILE_NAME);
            }
        }
        for (Map.Entry<MutationRecord, Set<String>> entry : seenMafRecord.entrySet()) {
            MutationRecord record = entry.getKey();
            Set<String> caller = entry.getValue();
            List<String> list = caller.stream().collect(Collectors.toList());
            record.setCaller(StringUtils.join(list, '|'));
            mafRecords.add(record);
        }
    }

    private void addRecord(MutationRecord newRecord, String maf_filename) {
        if (seenMafRecord.isEmpty()) {
            Set<String> caller = new HashSet<>();
            caller.add(maf_filename);
            seenMafRecord.put(newRecord, caller);
        } else {
            Iterator<Map.Entry<MutationRecord, Set<String>>> iterator = seenMafRecord.entrySet().iterator();
            Map.Entry<MutationRecord, Set<String>> record = iterator.next();
            while (iterator.hasNext() && !identicalVariant(record.getKey(), newRecord)) {
                record = iterator.next();
            }
            if (!identicalVariant(record.getKey(), newRecord)) {
                Set<String> caller = new HashSet<>();
                caller.add(maf_filename);
                seenMafRecord.put(newRecord, caller);
            } else {
                record.getValue().add(maf_filename);
            }
        }
    }
    // Identical if same : chr, start, end, strand, reference allele, tumor allele, barcode
    //TODO confirm --> which tumor allele ? which barcode ?
    private boolean identicalVariant(MutationRecord record, MutationRecord newRecord) {
        return record.getTumor_Sample_Barcode().equals(newRecord.getTumor_Sample_Barcode()) &&
                record.getChromosome().equals(newRecord.getChromosome()) &&
                record.getStart_Position().equals(newRecord.getStart_Position()) &&
                record.getEnd_Position().equals(newRecord.getEnd_Position()) &&
                record.getStrand().equals(newRecord.getStrand()) &&
                record.getReference_Allele().equals(newRecord.getReference_Allele()); //&&
                //record.getTumor_Seq_Allele1().equals(newRecord.getTumor_Seq_Allele1()) &&
                //record.getTumor_Seq_Allele2().equals((newRecord.getTumor_Seq_Allele2()));
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
