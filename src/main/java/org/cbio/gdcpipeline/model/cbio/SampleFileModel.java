package org.cbio.gdcpipeline.model.cbio;

import java.util.List;

/**
 * Created by Dixit on 24/06/17.
 */
public class SampleFileModel {

    private String patient_id;
    private List<String> sample_id;
    private String oncotree_code;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public List<String> getSample_id() {
        return sample_id;
    }

    public void setSample_id(List<String> sample_id) {
        this.sample_id = sample_id;
    }

    public String getOncotree_code() {
        return oncotree_code;
    }

    public void setOncotree_code(String oncotree_code) {
        this.oncotree_code = oncotree_code;
    }

}
