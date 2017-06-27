package org.cbio.gdcpipeline.model.cbio;

/**
 * Created by Dixit on 24/06/17.
 */
public class PatientFileModel {

    private String patient_id;
    private String sex;
    private int age;
    private String os_status;

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
        this.os_status = os_status;
    }


}
