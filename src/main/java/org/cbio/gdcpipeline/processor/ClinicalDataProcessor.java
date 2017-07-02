package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.ClinicalDataModel;
import org.cbio.gdcpipeline.model.cbio.Patient;
import org.springframework.batch.item.ItemProcessor;


public class ClinicalDataProcessor implements ItemProcessor<ClinicalDataModel, ClinicalDataModel> {
    @Override
    public ClinicalDataModel process(ClinicalDataModel patient) throws Exception {

        ClinicalDataModel modified = modify(patient);

        return modified;

        //return patient;

    }

    protected ClinicalDataModel modify(ClinicalDataModel data) {


        if (data instanceof Patient) {
            if (((Patient) data).getOs_status().equalsIgnoreCase("Alive")) {
                ((Patient) data).setOs_status("LIVING");
            } else if (((Patient) data).getOs_status().equalsIgnoreCase("Dead")) {
                ((Patient) data).setOs_status("DECEASED");
            }
        }
        return data;
    }
}
