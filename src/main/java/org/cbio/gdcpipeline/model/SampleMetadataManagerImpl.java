package org.cbio.gdcpipeline.model;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 02/07/17.
 */
public class SampleMetadataManagerImpl implements MetadataManager {

    private List<String> row1 = Arrays.asList("Patient Identifier", "Sample Identifier", "Onco Tree Code");
    private List<String> row2 = Arrays.asList("Patient Identifier", "Sample Identifier", "Onco Tree Code");
    private List<String> row3 = Arrays.asList("STRING", "STRING", "STRING");
    private List<String> row4 = Arrays.asList("1", "1", "1");
    private List<String> row5 = Arrays.asList("PATIENT_ID", "SAMPLE_ID", "ONCOTREE_CODE");


    @Override
    public Map<String, List<String>> getFullHeader() {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        headers.put("row1", row1);
        headers.put("row2", row2);
        headers.put("row3", row3);
        headers.put("row4", row4);
        headers.put("row5", row5);

        return headers;
    }
}
