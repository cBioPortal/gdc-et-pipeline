package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.MutationRecord;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by Dixit
 */
public class MutationProcessor implements ItemProcessor<MutationRecord,MutationRecord> {
    @Override
    public MutationRecord process(MutationRecord mutationRecord) throws Exception {
        normalize(mutationRecord.getTumor_Sample_Barcode());
        normalize(mutationRecord.getMatched_Norm_Sample_Barcode());
        return mutationRecord;
    }

    private void normalize(String record){
       // record.s
    }
}
