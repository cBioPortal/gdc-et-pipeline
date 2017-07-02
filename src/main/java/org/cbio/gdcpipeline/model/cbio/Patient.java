package org.cbio.gdcpipeline.model.cbio;

import org.apache.tomcat.util.buf.StringUtils;
import org.cbio.gdcpipeline.model.PatientMetadataManagerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 24/06/17.
 */
public class Patient extends ClinicalDataModel {

    private String patient_id;
    private String sex;
    private int age;
    private String os_status;
    private String data_file_name = "data_clinical_patient.txt";
    private String metadata_file_name = "metadata_clinical_patient.txt";


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
        fields.add("Sex");
        fields.add("Age");
        fields.add("Os_status");
        fields.add("Data_file_name");
        fields.add("Metadata_file_name");

        return fields;
    }

    @Override
    public String getHeaders() {
        PatientMetadataManagerImpl headers = new PatientMetadataManagerImpl();
        return this.makeHeader(headers.getFullHeader());
    }

    private String makeHeader(Map<String, List<String>> fullHeader) {
        StringBuilder header = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : fullHeader.entrySet()) {
            header.append(StringUtils.join(entry.getValue(), ','));
            header.append("\n");
        }
        return header.toString();
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }


    public String getMetadata_file_name() {
        return metadata_file_name;
    }

    public void setMetadata_file_name(String metadata_file_name) {
        this.metadata_file_name = metadata_file_name;
    }


    public String getData_file_name() {
        return data_file_name;
    }

    public void setData_file_name(String data_file_name) {
        this.data_file_name = data_file_name;
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
        this.os_status = os_status;
    }

}
