package org.cbio.gdcpipeline.processor;

import org.cbioportal.models.MutationRecord;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dixit Patel
 */
public class MutationDataProcessor implements ItemProcessor<MutationRecord,MutationRecord> {
    private static Pattern pattern = Pattern.compile("^(TCGA-\\w\\w-\\w\\w\\w\\w-(\\d\\d|Tumor)).*$");
    private static Map<String,String> validChrValues = null;

    @Override
    public MutationRecord process(MutationRecord mutationRecord) throws Exception {
        mutationRecord.setTUMOR_SAMPLE_BARCODE(extractSampleId(mutationRecord.getTUMOR_SAMPLE_BARCODE()));
        mutationRecord.setMATCHED_NORM_SAMPLE_BARCODE(extractSampleId(mutationRecord.getMATCHED_NORM_SAMPLE_BARCODE()));
        normalizeChromosome(mutationRecord.getCHROMOSOME());
        return mutationRecord;
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
            validChrValues = new HashMap<String,String>();
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
}
