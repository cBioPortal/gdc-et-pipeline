package org.cbio.gdcpipeline.model.gdc.clinical;



public class GDCClinicalDataModel {

    public String getAdditional_studies() {
        return additional_studies;
    }

    public void setAdditional_studies(String additional_studies) {
        this.additional_studies = additional_studies;
    }

    private String additional_studies;
    private String patient_id;
    private String sample_id;
    private String sex;
    private int age;

    public GDCClinicalDataModel(String additional_studies, String patient_id, String sample_id, String sex, int age) {
        this.additional_studies=additional_studies;
        this.patient_id = patient_id;
        this.sample_id = sample_id;
        this.sex = sex;
        this.age = age;
        this.additional_studies=additional_studies;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getSample_id() {
        return sample_id;
    }

    public void setSample_id(String sample_id) {
        this.sample_id = sample_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
