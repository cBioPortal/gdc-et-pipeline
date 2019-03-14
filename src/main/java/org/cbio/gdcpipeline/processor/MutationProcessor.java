package org.cbio.gdcpipeline.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbioportal.annotator.Annotator;
import org.cbioportal.models.AnnotatedRecord;
import org.cbioportal.models.MutationRecord;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cbioportal.annotator.GenomeNexusAnnotationFailureException;

/**
 * @author Dixit Patel
 */
public class MutationProcessor implements ItemProcessor<MutationRecord,AnnotatedRecord> {
    private static Pattern pattern = Pattern.compile("^(TCGA-\\w\\w-\\w\\w\\w\\w-(\\d\\d|Tumor)).*$");
    private static Map<String,String> validChrValues = null;
    private static Log LOG = LogFactory.getLog(MutationProcessor.class);
    @Value("#{jobParameters[isoformOverrideSource]}")
    private String isoformOverrideSource;

    @Autowired
    private Annotator annotator;

    @Override
    public AnnotatedRecord process(MutationRecord mutationRecord) throws Exception {
        mutationRecord.setTUMOR_SAMPLE_BARCODE(extractSampleId(mutationRecord.getTUMOR_SAMPLE_BARCODE()));
        mutationRecord.setMATCHED_NORM_SAMPLE_BARCODE(extractSampleId(mutationRecord.getMATCHED_NORM_SAMPLE_BARCODE()));
        AnnotatedRecord annotatedRecord = annotateRecord(mutationRecord);
        annotatedRecord.setCHROMOSOME(normalizeChromosome(annotatedRecord.getCHROMOSOME()));
        return annotatedRecord;
    }

    private String extractSampleId(String record) {
        Matcher matcher = pattern.matcher(record);
        if(matcher.find()) {
            record = matcher.group();
        }
        return record;
    }

    private String normalizeChromosome(String chromosome){
        if (chromosome == null){
            return null;
        }
        if (validChrValues==null) {
            validChrValues = new HashMap<>();
            for (int lc = 1; lc<=24; lc++) {
                validChrValues.put(Integer.toString(lc),Integer.toString(lc));
                validChrValues.put("CHR" + Integer.toString(lc),Integer.toString(lc));
            }
            validChrValues.put("X","23");
            validChrValues.put("CHRX","23");
            validChrValues.put("Y","24");
            validChrValues.put("CHRY","24");
            validChrValues.put("NA","NA");
            validChrValues.put("MT","MT"); // mitochondria
        }
        return validChrValues.get(chromosome);
    }

    private AnnotatedRecord annotateRecord(MutationRecord record) throws GenomeNexusAnnotationFailureException {
        AnnotatedRecord annotatedRecord = new AnnotatedRecord();
        try {
            annotatedRecord = annotator.annotateRecord(record, false, isoformOverrideSource, true);
        } catch (HttpServerErrorException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Server Exception. Failed to annotate a record from json! Sample: " + record.getTUMOR_SAMPLE_BARCODE() + " Variant: " + record.getCHROMOSOME() + ":" + record.getSTART_POSITION() + record.getREFERENCE_ALLELE() + ">" + record.getTUMOR_SEQ_ALLELE2());
            }
        } catch (HttpClientErrorException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Client Error Exception. Failed to annotate a record from json! Sample: " + record.getTUMOR_SAMPLE_BARCODE() + " Variant: " + record.getCHROMOSOME() + ":" + record.getSTART_POSITION() + record.getREFERENCE_ALLELE() + ">" + record.getTUMOR_SEQ_ALLELE2());
            }
        }
        return annotatedRecord;
    }
}
