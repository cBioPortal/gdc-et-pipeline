package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.MutationRecord;
import org.springframework.batch.item.ItemProcessor;

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
        String[] codes = record.split("-");
        if (codes.length != 0) {
            record = codes[0] + "-" + codes[1] + "-" + codes[2] + "-" + codes[3].substring(0, 2);
        }
        return record;
    }
}
