package org.cbio.gdcpipeline.model;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 02/07/17.
 */
public class PatientMetadataManagerImpl implements MetadataManager {

    private final List<String> row1 = Arrays.asList("Patient Identifier", "Overall Survival Status", "Sex", "Age");
    private final List<String> row2 = Arrays.asList("Patient Identifier", "Overall Survival Status", "Sex", "Age");
    private final List<String> row3 = Arrays.asList("STRING", "STRING", "STRING", "NUMBER");
    private final List<String> row4 = Arrays.asList("1", "1", "1", "1");
    private final List<String> row5 = Arrays.asList("PATIENT_ID", "OS_STATUS", "SEX", "AGE");

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
