package org.cbio.gdcpipeline.processor;

import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.brca._2.Patient;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.brca._2.TcgaBcr;
import org.springframework.batch.item.ItemProcessor;


public class ClinicalDataProcessor<Object> implements ItemProcessor<TcgaBcr,CBioClinicalDataModel> {
    @Override
    public CBioClinicalDataModel process(TcgaBcr patient) throws Exception {

        System.out.print("======================= INSIDE PROCESSOR===========");
        System.out.print("======================= INSIDE PROCESSOR===========");
        System.out.print("======================= INSIDE PROCESSOR===========");
        System.out.print("======================= INSIDE PROCESSOR===========");System.out.print("======================= INSIDE PROCESSOR===========");
        System.out.print("======================= INSIDE PROCESSOR===========");
        System.out.print(patient.getPatient().toString());

        CBioClinicalDataModel cBio = new CBioClinicalDataModel();
        cBio.setPatient_id(patient.getPatient().getBcrPatientBarcode().getValue());
        cBio.setSample_id(patient.getPatient().getBcrPatientBarcode().getValue());
        cBio.setAge(Integer.parseInt(patient.getPatient().getAgeAtInitialPathologicDiagnosis().getValue()));
        cBio.setSex(patient.getPatient().getGender().getValue());
        cBio.setOs_status(patient.getPatient().getVitalStatus().getValue());
        return cBio;
        //return patient;


    }
}
