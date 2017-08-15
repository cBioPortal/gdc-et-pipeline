package org.cbio.gdcpipeline.reader;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.buf.StringUtils;
import org.cbio.gdcpipeline.model.cbio.AnnotatedRecord;
import org.cbio.gdcpipeline.model.cbio.MutationRecord;
import org.cbio.gdcpipeline.util.CommonDataUtil;
import org.cbio.gdcpipeline.util.MutationDataFileUtils;
import org.cbioportal.annotator.Annotator;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.client.HttpServerErrorException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dixit Patel
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

    @Autowired
    private Annotator annotator;

    private List<MutationRecord> mafRecords = new ArrayList<>();
    private static Log LOG = LogFactory.getLog(MutationReader.class);
    public static String DEFAULT_MERGED_MAF_FILENAME = "merged_maf_file.maf";
    private Map<MutationRecord, Set<String>> seenMafRecord = new HashMap<>();
    private ExecutionContext executionContext;

    @Override
    public MutationRecord read() throws Exception {
        if (!mafRecords.isEmpty()) {
            return mafRecords.remove(0);
        }
        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        if (!separate_maf.isEmpty()) {
            if (separate_maf.equalsIgnoreCase("true")) {
                File maf_file = (File) executionContext.get("mafToProcess");
                if (LOG.isInfoEnabled()) {
                    LOG.info("Processing MAF File : " + maf_file.getAbsolutePath());
                }
                readFile(maf_file);
                File output_file = new File(outputDir,maf_file.getName());
                executionContext.put("maf_file_to_write", output_file);
            } else {
                List<File> maf_files = (List<File>) executionContext.get("maf_files");
                for (File maf_file : maf_files) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Processing MAF File : " + maf_file.getAbsolutePath());
                    }
                    readFile(maf_file);
                }
                File MERGED_MAF_FILE_NAME = new File(outputDir, DEFAULT_MERGED_MAF_FILENAME);
                executionContext.put("maf_file_to_write", MERGED_MAF_FILE_NAME);
            }
            for (Map.Entry<MutationRecord, Set<String>> entry : seenMafRecord.entrySet()) {
                MutationRecord record = entry.getKey();
                Set<String> caller = entry.getValue();
                List<String> list = caller.stream().collect(Collectors.toList());
                record.setCaller(StringUtils.join(list, '|'));
                mafRecords.add(record);
            }
        }
    }

    private void readFile(File maf_file) {
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
    }

    private void addRecord(MutationRecord newRecord, String maf_filename) {
        maf_filename = MutationDataFileUtils.getCallerName(maf_filename);
        if (seenMafRecord.isEmpty()) {
            Set<String> caller = new HashSet<>();
            caller.add(maf_filename);
            seenMafRecord.put(newRecord, caller);
        } else {
            Iterator<Map.Entry<MutationRecord, Set<String>>> iterator = seenMafRecord.entrySet().iterator();
            Map.Entry<MutationRecord, Set<String>> entry = iterator.next();
            while (iterator.hasNext() && !identicalVariant(entry.getKey(),newRecord)){
                entry = iterator.next();
            }
            if (!identicalVariant(entry.getKey(), newRecord)) {
                Set<String> caller = new HashSet<>();
                caller.add(maf_filename);
                seenMafRecord.put(newRecord, caller);
            } else {
                entry.getValue().add(maf_filename);
            }
        }
    }

    private boolean identicalVariant(MutationRecord record, MutationRecord newRecord) {
        return record.getTUMOR_SAMPLE_BARCODE().equals(newRecord.getTUMOR_SAMPLE_BARCODE()) &&
                record.getCHROMOSOME().equals(newRecord.getCHROMOSOME()) &&
                record.getSTART_POSITION().equals(newRecord.getSTART_POSITION()) &&
                record.getEND_POSITION().equals(newRecord.getEND_POSITION()) &&
                record.getSTRAND().equals(newRecord.getSTRAND()) &&
                record.getREFERENCE_ALLELE().equals(newRecord.getREFERENCE_ALLELE()) &&( sameTumourSequence(record,newRecord));
    }

    private boolean sameTumourSequence(MutationRecord record,MutationRecord newRecord) {
        if (!(record.getTUMOR_SEQ_ALLELE1().equals(record.getREFERENCE_ALLELE()) || record.getTUMOR_SEQ_ALLELE1().isEmpty() || CommonDataUtil.isIgnore(record.getTUMOR_SEQ_ALLELE1()))) {
            return record.getTUMOR_SEQ_ALLELE1().equals(newRecord.getTUMOR_SEQ_ALLELE1());
        }
        return record.getTUMOR_SEQ_ALLELE2().equals(newRecord.getTUMOR_SEQ_ALLELE2());
    }

    private FieldSetMapper<MutationRecord> mutationFieldSetMapper() {
        return (FieldSetMapper<MutationRecord>) (FieldSet fs) -> {
            MutationRecord record = new MutationRecord();
            for (String header : record.getHeader()) {
                try {
                    String a = fs.readString(header);
                    record.getClass().getMethod("set" + header.toUpperCase(), String.class).invoke(record, fs.readString(header));
                } catch (Exception e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.error(" Error in setting record for :" + header);
                    }
                    e.printStackTrace();
                }
            }
            //annotate
            org.cbioportal.models.AnnotatedRecord ar = null;
            AnnotatedRecord annotatedRecord = new AnnotatedRecord();
            try {
                ar = annotator.annotateRecord(record, false, "uniprot", true);
            } catch (HttpServerErrorException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Failed to annotate a record from json! Sample: " + record.getTUMOR_SAMPLE_BARCODE() + " Variant: " + record.getCHROMOSOME() + ":" + record.getSTART_POSITION() + record.getREFERENCE_ALLELE() + ">" + record.getTUMOR_SEQ_ALLELE2());
                }
            }

            for(String header : ar.getHeader()){
                try {
                    annotatedRecord.getClass().getMethod("set"+header.toUpperCase(),String.class).invoke(annotatedRecord,ar.getClass().getMethod("get"+header.toUpperCase()).invoke(ar));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return (MutationRecord)annotatedRecord;
        };
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }
}