package org.cbio.gdcpipeline.model.cbio;

import org.cbio.gdcpipeline.model.ClinicalMetadataImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dixit Patel
 */

public class Sample extends ClinicalDataModel {
    private String patient_id;
    private String sample_id;
    private String oncotree_code;

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
