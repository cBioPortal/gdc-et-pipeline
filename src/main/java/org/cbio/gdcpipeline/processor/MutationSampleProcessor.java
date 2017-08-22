package org.cbio.gdcpipeline.processor;

import org.cbioportal.models.MutationRecord;
import org.springframework.batch.item.ItemProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dixit Patel
 */
public class MutationSampleProcessor implements ItemProcessor<MutationRecord,MutationRecord> {
    private static Pattern numberPattern = Pattern.compile("(\\d+)");

    @Override
    public MutationRecord process(MutationRecord mutationRecord) throws Exception {
        mutationRecord.setTUMOR_SAMPLE_BARCODE(stripSample(mutationRecord.getTUMOR_SAMPLE_BARCODE()));
        mutationRecord.setMATCHED_NORM_SAMPLE_BARCODE(stripSample(mutationRecord.getMATCHED_NORM_SAMPLE_BARCODE()));
        processChromosome(mutationRecord.getCHROMOSOME());
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

    private String processChromosome(String chromosome){
        Matcher matcher = numberPattern.matcher(chromosome);
        if(matcher.find()){
            chromosome = matcher.group(1);
        }
        if(!chromosome.isEmpty()) {
            int chr = Integer.parseInt(chromosome);
            if (chr > 0 && chr < 24) {
                if (chr == 22) chromosome = "X";
                if (chr == 23) chromosome = "Y";
            } else {
                //TODO ?
            }
        } else {
            //TODO ?
        }
        return chromosome;
    }
}
