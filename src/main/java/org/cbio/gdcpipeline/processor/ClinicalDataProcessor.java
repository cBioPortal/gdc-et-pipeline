package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.gdc.clinical.GDCClinicalDataModel;
import org.springframework.batch.item.ItemProcessor;


public class ClinicalDataProcessor implements ItemProcessor<GDCClinicalDataModel,CBioClinicalDataModel> {
    @Override
    public CBioClinicalDataModel process(GDCClinicalDataModel gdcClinicalDataModel) throws Exception {
        CBioClinicalDataModel cBio = new CBioClinicalDataModel();
        cBio.setPatient_id(gdcClinicalDataModel.getPatient_id());
        cBio.setSample_id(gdcClinicalDataModel.getSample_id());
        cBio.setAge(gdcClinicalDataModel.getAge());
        cBio.setSex(gdcClinicalDataModel.getSex());
        cBio.setOs_status(gdcClinicalDataModel.getOs_status());
        return cBio;


    }
}
