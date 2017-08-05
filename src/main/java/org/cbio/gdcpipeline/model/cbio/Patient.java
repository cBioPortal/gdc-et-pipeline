package org.cbio.gdcpipeline.model.cbio;

import org.cbio.gdcpipeline.model.ClinicalDataSourceImpl;
import org.cbio.gdcpipeline.model.ClinicalMetadataImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dixit Patel
 */

public class Patient extends ClinicalDataModel {
    private String patient_id;
    private String sex;
    private int age;
    private String os_status;
    private ClinicalDataSourceImpl clinicalDataSource = new ClinicalDataSourceImpl();

    public Patient() {
    }

    public Patient(String patient_id, String os_status, String sex, int age) {
        this.patient_id = patient_id;
        this.sex = sex;
        this.age = age;
        this.os_status = os_status;
    }

    @Override
    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        fields.add("Patient_id");
        fields.add("Os_status");
        fields.add("Age");
        fields.add("Sex");
        return fields;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        ClinicalMetadataImpl headers = new ClinicalMetadataImpl();
        return (headers.getFullHeader(getFields()));
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
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

    public String getOs_status() {
        return os_status;
    }

    public void setOs_status(String os_status) {
        this.os_status = this.clinicalDataSource.getNormalizedClinicalFieldValue(os_status);
    }

}
