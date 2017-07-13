package org.cbio.gdcpipeline.model;

import java.util.*;

/**
 * Created by Dixit on 02/07/17.
 */
public class ClinicalMetadataImpl implements MetadataManager {

    Map<String, String> displayNames = new HashMap<>();
    Map<String, String> description = new HashMap<>();
    Map<String, String> datatype = new HashMap<>();
    Map<String, String> priority = new HashMap<>();

    public ClinicalMetadataImpl() {

        setDisplayNames();
        setDescription();
        setDatatype();
        setPriority();

    }


    @Override
    public Map<String, List<String>> getFullHeader(List<String> header) {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        List<String> displayNames = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<String> datatype = new ArrayList<>();
        List<String> priority = new ArrayList<>();

        for (String key : header) {
            key = key.toUpperCase();
            displayNames.add(this.displayNames.get(key));
            description.add(this.description.get(key));
            datatype.add(this.datatype.get(key));
            priority.add(this.priority.get(key));
        }


        headers.put("displayNames", displayNames);
        headers.put("description", description);
        headers.put("datatype", datatype);
        headers.put("priority", priority);
        headers.put("headers", header);

        return headers;
    }

    private void setDisplayNames() {
        this.displayNames.put("PATIENT_ID", "Patient Identifier");
        this.displayNames.put("SAMPLE_ID", "Sample Identifier");
        this.displayNames.put("ONCOTREE_CODE", "Onco Tree Code");
        this.displayNames.put("OS_STATUS", "Overall Survival Status");
        this.displayNames.put("SEX", "Sex");
        this.displayNames.put("AGE", "Age");
    }

    private void setDescription() {
        this.description.put("PATIENT_ID", "Patient_Identifier");
        this.description.put("SAMPLE_ID", "Sample Identifier");
        this.description.put("ONCOTREE_CODE", "Onco Tree Code");
        this.description.put("OS_STATUS", "Overall Survival Status");
        this.description.put("SEX", "Sex");
        this.description.put("AGE", "Age");
    }

    private void setDatatype() {
        this.datatype.put("PATIENT_ID", "STRING");
        this.datatype.put("SAMPLE_ID", "STRING");
        this.datatype.put("ONCOTREE_CODE", "STRING");
        this.datatype.put("OS_STATUS", "STRING");
        this.datatype.put("SEX", "STRING");
        this.datatype.put("AGE", "NUMBER");

    }

    private void setPriority() {
        this.priority.put("PATIENT_ID", "1");
        this.priority.put("SAMPLE_ID", "1");
        this.priority.put("ONCOTREE_CODE", "1");
        this.priority.put("OS_STATUS", "1");
        this.priority.put("SEX", "1");
        this.priority.put("AGE", "1");

    }

}
