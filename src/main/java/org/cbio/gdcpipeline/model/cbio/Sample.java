package org.cbio.gdcpipeline.model.cbio;

import org.apache.tomcat.util.buf.StringUtils;
import org.cbio.gdcpipeline.model.SampleMetadataManagerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 24/06/17.
 */
public class Sample extends ClinicalDataModel {

    private String patient_id;
    private String sample_id;
    private String oncotree_code;
    private String data_file_name = "data_clinical_sample.txt";
    private String metadata_file_name = "metadata_clinical_sample.txt";

    public String getData_file_name() {
        return data_file_name;
    }

    public void setData_file_name(String data_file_name) {
        this.data_file_name = data_file_name;
    }

    public Sample() {
    }

    public Sample(String patient_id, String sample_id, String oncotree_code) {
        this.patient_id = patient_id;
        this.sample_id = sample_id;
        this.oncotree_code = oncotree_code;
    }

    @Override
    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        fields.add("Patient_id");
        fields.add("Sample_id");
        fields.add("Oncotree_code");

        return fields;
    }

    @Override
    public String getHeaders() {
        SampleMetadataManagerImpl headers = new SampleMetadataManagerImpl();
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


    public String getSample_id() {
        return sample_id;
    }

    public void setSample_id(String sample_id) {
        this.sample_id = sample_id;
    }

    public String getOncotree_code() {
        return oncotree_code;
    }

    public void setOncotree_code(String oncotree_code) {
        this.oncotree_code = oncotree_code;
    }

}
