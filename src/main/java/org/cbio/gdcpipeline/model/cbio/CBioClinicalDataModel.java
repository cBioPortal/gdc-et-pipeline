package org.cbio.gdcpipeline.model.cbio;


public class CBioClinicalDataModel {


    private PatientFileModel patientFileModel;
    private SampleFileModel sampleFileModel;

    public PatientFileModel getPatientFileModel() {
        return patientFileModel;
    }

    public void setPatientFileModel(PatientFileModel patientFileModel) {
        this.patientFileModel = patientFileModel;
    }

    public SampleFileModel getSampleFileModel() {
        return sampleFileModel;
    }

    public void setSampleFileModel(SampleFileModel sampleFileModel) {
        this.sampleFileModel = sampleFileModel;
    }
}
