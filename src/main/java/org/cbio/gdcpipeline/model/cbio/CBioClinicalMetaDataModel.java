package org.cbio.gdcpipeline.model.cbio;

/**
 * Created by Dixit on 31/05/17.
 */
public class CBioClinicalMetaDataModel {

    private String cancer_study_identifier;
    private String genetic_alteration_type;
    private String datatype;
    private String data_filename;

    public CBioClinicalMetaDataModel() {
    }

    public CBioClinicalMetaDataModel(String cancer_study_identifier, String genetic_alteration_type, String datatype, String data_filename) {
        this.cancer_study_identifier = cancer_study_identifier;
        this.genetic_alteration_type = genetic_alteration_type;
        this.datatype = datatype;
        this.data_filename = data_filename;
    }

    public String getCancer_study_identifier() {
        return cancer_study_identifier;
    }

    public void setCancer_study_identifier(String cancer_study_identifier) {
        this.cancer_study_identifier = cancer_study_identifier;
    }

    public String getGenetic_alteration_type() {
        return genetic_alteration_type;
    }

    public void setGenetic_alteration_type(String genetic_alteration_type) {
        this.genetic_alteration_type = genetic_alteration_type;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getData_filename() {
        return data_filename;
    }

    public void setData_filename(String data_filename) {
        this.data_filename = data_filename;
    }
}
