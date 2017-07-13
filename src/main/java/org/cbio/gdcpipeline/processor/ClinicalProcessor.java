package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.ClinicalDataModel;
import org.springframework.batch.item.ItemProcessor;


public class ClinicalProcessor implements ItemProcessor<ClinicalDataModel, ClinicalDataModel> {


    @Override
    public ClinicalDataModel process(ClinicalDataModel patient) throws Exception {
        return patient;
    }

}
