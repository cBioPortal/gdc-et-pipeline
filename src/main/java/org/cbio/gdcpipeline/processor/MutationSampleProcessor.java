package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.MutationRecord;
import org.springframework.batch.item.ItemProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dixit Patel
 */
public class MutationSampleProcessor implements ItemProcessor<MutationRecord,MutationRecord> {
    @Override
    public MutationRecord process(MutationRecord mutationRecord) throws Exception {
        mutationRecord.setTumor_Sample_Barcode(stripSample(mutationRecord.getTumor_Sample_Barcode()));
        mutationRecord.setMatched_Norm_Sample_Barcode(stripSample(mutationRecord.getMatched_Norm_Sample_Barcode()));
        return mutationRecord;
    }

    private String stripSample(String record) {
        Pattern pattern = Pattern.compile("(?:.*?-){3}[0-1]{2}");
        Matcher matcher = pattern.matcher(record);
        if(matcher.find()) {
            record=matcher.group();
        }
        return record;
    }
}
