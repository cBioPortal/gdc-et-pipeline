package org.cbio.gdcpipeline.model.gdc.clinical;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class GDCClinicalFieldMapper implements FieldSetMapper<GDCClinicalDataModel> {

    @Override
    public GDCClinicalDataModel mapFieldSet(FieldSet fieldSet) throws BindException {
        return new GDCClinicalDataModel(
                fieldSet.readString("patient_id"),
                fieldSet.readString("sample_id"),
                fieldSet.readString("sex"),
                fieldSet.readInt("age"),
                fieldSet.readString("os_status"));

    }}
