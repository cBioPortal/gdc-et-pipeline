package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.MutationRecord;
import org.springframework.batch.item.ItemProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dixit Patel
 */
public class MutationSampleProcessor implements ItemProcessor<MutationRecord,MutationRecord> {

    private static Pattern pattern = Pattern.compile("^(TCGA-\\w\\w-\\w\\w\\w\\w-(\\d\\d|Tumor)).*$");

    @Override
    public MutationRecord process(MutationRecord mutationRecord) throws Exception {
        mutationRecord.setTumor_Sample_Barcode(extractSampleId(mutationRecord.getTumor_Sample_Barcode()));
        mutationRecord.setMatched_Norm_Sample_Barcode(extractSampleId(mutationRecord.getMatched_Norm_Sample_Barcode()));
        return mutationRecord;
    }

    private String extractSampleId(String record) {
        Matcher matcher = pattern.matcher(record);
        if(matcher.find()) {
            record=matcher.group();
        }
        return record;
    }
}
