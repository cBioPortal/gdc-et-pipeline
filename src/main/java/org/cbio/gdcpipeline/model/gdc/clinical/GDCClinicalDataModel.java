package org.cbio.gdcpipeline.model.gdc.clinical;


import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

public class GDCClinicalDataModel implements ResourceAware {

    private String patient_id;
    private String sample_id;
    private String sex;
    private int age;
    private String os_status;
    private Resource resource;

    public GDCClinicalDataModel() {
    }

    public GDCClinicalDataModel(String patient_id, String sample_id, String sex, int age, String os_status) {
        this.patient_id = patient_id;
        this.sample_id = sample_id;
        this.sex = sex;
        this.age = age;
        this.os_status = os_status;
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

    public String getOs_status() {
        return os_status;
    }

    public void setOs_status(String os_status) {
        this.os_status = os_status;
    }

    @Override
    public void setResource(Resource resource) {
        this.resource=resource;
    }
}
