package org.cbio.gdcpipeline.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 13/07/17.
 */
public interface ClinicalDataSource {

    List<Map<String, String>> getClinicalData();

    List<String> getSampleHeader();

    List<String> getPatientHeader();

    List<String> getTimelineHeader();

    List<Map<String, String>> getTimelineData();

    String getNextClinicalStudyId();

    String getNextTimelineStudyId();

    boolean hasMoreTimelineData();

    boolean hasMoreClinicalData();

    Map<String, List<String>> getFullPatientHeader(Map<String, List<String>> fullHeader);

    Map<String, List<String>> getFullSampleHeader(Map<String, List<String>> fullHeader);

    String getAcceptableClinicalFieldValue(String field);
}

