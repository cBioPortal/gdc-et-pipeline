package org.cbio.gdcpipeline.model.cbio;

import java.util.List;

public abstract class ClinicalDataModel {


    public abstract List<String> getFields();

    public abstract String getHeaders();
}
