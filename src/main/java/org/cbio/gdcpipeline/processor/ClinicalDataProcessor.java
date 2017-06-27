package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.cbio.SampleFileModel;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;


public class ClinicalDataProcessor implements ItemProcessor<CBioClinicalDataModel, CBioClinicalDataModel> {
    @Override
    public CBioClinicalDataModel process(CBioClinicalDataModel patient) throws Exception {

        //CBioClinicalDataModel modified = modifySampleList(patient);

        //return modified;

        return patient;

    }

    protected CBioClinicalDataModel modifySampleList(CBioClinicalDataModel patient) {

        //Exclude samples that end with '-10'
        SampleFileModel modifySample = patient.getSampleFileModel();
        List<String> sampleList = modifySample.getSample_id();
        if (sampleList == null || sampleList.isEmpty()) {
            return patient;
        }
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < sampleList.size(); i++) {
            String sample = sampleList.get(i);
            if (!sample.endsWith("-10")) {
                newList.add(sample);
            }
        }
        patient.getSampleFileModel().setSample_id(newList);
        return patient;
    }
}
