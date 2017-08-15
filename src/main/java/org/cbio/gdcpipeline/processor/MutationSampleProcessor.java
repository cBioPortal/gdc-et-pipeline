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
        mutationRecord.setTUMOR_SAMPLE_BARCODE(stripSample(mutationRecord.getTUMOR_SAMPLE_BARCODE()));
        mutationRecord.setMATCHED_NORM_SAMPLE_BARCODE(stripSample(mutationRecord.getMATCHED_NORM_SAMPLE_BARCODE()));
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
